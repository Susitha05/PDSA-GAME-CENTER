package com.snakeandladder.service;

import com.snakeandladder.model.Board;
import com.snakeandladder.model.Ladder;
import com.snakeandladder.model.Snake;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameService {

    private final Random random = new Random();

    public Board generateBoard(int n) {
        int size = n * n;
        // Retry logic if generated board is invalid (has cycles or unreachable)
        while (true) {
            Board board = tryGenerateBoard(n, size);
            if (isValidBoard(board)) {
                return board;
            }
        }
    }

    private Board tryGenerateBoard(int n, int totalCells) {
        Board board = new Board();
        board.setSize(n);
        board.setTotalCells(totalCells);
        
        List<Snake> snakes = new ArrayList<>();
        List<Ladder> ladders = new ArrayList<>();
        
        Set<Integer> occupied = new HashSet<>();
        occupied.add(1);
        occupied.add(totalCells);

         
        // Set the number of ladders in the board as N-2
        // Set the number of snakes in the board as N-2
        int numSnakes = n - 2; 
        int numLadders = n - 2;

        // === Generate Snakes ===
        // Configuration: We want a specific mix of snake "shapes" to enhance the 3D board aesthetics.
        // Specifically, we expect 3 "short" snakes that drop exactly 1 row.
        // These are rendered as simple, tight curves in the 3D view (React Three Fiber).
        int snakeOneCount = 0;
        int maxSnakeOnes = 3;

        for (int i = 0; i < numSnakes; i++) {
            // If we haven't met the quota for 1-row snakes, force the generator to create one.
            // These short snakes add variety to the procedural 3D generation.
            boolean forceSnakeOne = snakeOneCount < maxSnakeOnes;
            
            if (createSnake(n, totalCells, occupied, snakes, forceSnakeOne)) {
                // If we successfully created a forced 1-row snake, count it.
                if (forceSnakeOne) snakeOneCount++;
            }
        }

        // === Generate Ladders ===
        // Configuration: We want a specific mix of ladder "shapes" for the 3D board.
        // We expect 2 perfectly vertical ladders.
        // These are rendered as straight vertical cylinder groups in the 3D view.
        int ladderUpCount = 0;
        int maxLadderUps = 2;

        for (int i = 0; i < numLadders; i++) {
            // If we haven't met the quota for vertical ladders, force the generator to create one.
            // Vertical ladders look distinct from the slanted/angled ones in 3D.
            boolean forceVertical = ladderUpCount < maxLadderUps;
            
            if (createLadder(n, totalCells, occupied, ladders, forceVertical)) {
                // If we successfully created a forced vertical ladder, count it.
                if (forceVertical) ladderUpCount++;
            }
        }

        board.setSnakes(snakes);
        board.setLadders(ladders);
        return board;
    }

    private boolean createSnake(int n, int totalCells, Set<Integer> occupied, List<Snake> snakes, boolean forceOneRow) {
        for (int i = 0; i < 200; i++) {
            int start = random.nextInt(totalCells - n) + n; // Start at least from row 2
            if (start >= totalCells) continue;
            
            // Get Row/Col for Start
            int startRow = (start - 1) / n; // 0-based
            int startCol = (start - 1) % n;

            int endRow;
            if (forceOneRow) {
                endRow = startRow - 1;
            } else {
            
                if (startRow < 2) continue;
                // 50% chance for 3-row drop if possible
                if (startRow >= 3 && random.nextBoolean()) {
                    endRow = startRow - 3; 
                } else {
                   endRow = random.nextInt(startRow - 1); 
                }
            }
            
            if (endRow < 0) continue;

            // Generate End Col - Randomize direction (Left/Right/Straight)
            int endCol = random.nextInt(n); 
        
            
            // Reconstruct End Cell ID
            int end = endRow * n + endCol + 1;

            if (start <= end) continue; 
            if (occupied.contains(start) || occupied.contains(end)) continue;
            
            occupied.add(start);
            occupied.add(end);
            snakes.add(new Snake(start, end));
            return true;
        }
        return false;
    }

    private boolean createLadder(int n, int totalCells, Set<Integer> occupied, List<Ladder> ladders, boolean forceVertical) {
        for (int i = 0; i < 200; i++) {
            int start = random.nextInt(totalCells - n) + 1;
            
            int startRow = (start - 1) / n;
            int startCol = (start - 1) % n;
            
            if (startRow >= n - 1) continue; // Can't start at top row

            int endRow = startRow + random.nextInt(n - 1 - startRow) + 1;
            int endCol;
            
            if (forceVertical) {
                endCol = startCol;
            } else {
                endCol = random.nextInt(n);
                // Ensure not vertical
                if (endCol == startCol) endCol = (startCol + 1) % n;
            }

            int end = endRow * n + endCol + 1;

            if (start >= end) continue;
            if (occupied.contains(start) || occupied.contains(end)) continue;

            occupied.add(start);
            occupied.add(end);
            ladders.add(new Ladder(start, end));
            return true;
        }
        return false;
    }

    // Check if board is valid (reachable and no infinite loops)
    // A simple BFS from 1 to N^2 implies reachability.
    // Cycle detection is stricter but for Snake & Ladder, strictly forward movement 
    // is broken only by Snakes? No, Snakes go back.
    // Cycles can form: Snake 20->5, Ladder 5->20.
    private boolean isValidBoard(Board board) {
        int[] jumps = new int[board.getTotalCells() + 1];
        for (Snake s : board.getSnakes()) jumps[s.getStart()] = s.getEnd();
        for (Ladder l : board.getLadders()) jumps[l.getStart()] = l.getEnd();

        // Detect Cycle using DFS
        boolean[] visited = new boolean[board.getTotalCells() + 1];
        boolean[] recStack = new boolean[board.getTotalCells() + 1];

        // We only care about cycles in the jump graph itself? 
        // Or effective cycles during play?
        // Simpler: Just check if we can reach end.
        // And check if there is any "jump" cycle (e.g. 5->20->5).
        // A jump cycle is fatal.
        
        for (int i = 1; i <= board.getTotalCells(); i++) {
             if (jumps[i] != 0) {
                 // Trace the jump chain
                 int curr = i;
                 Set<Integer> visitedInChain = new HashSet<>();
                 while (curr != 0 && jumps[curr] != 0) {
                     if (visitedInChain.contains(curr)) return false; // Cycle detected
                     visitedInChain.add(curr);
                     curr = jumps[curr];
                 }
             }
        }
        
        return true; 
    }
}
