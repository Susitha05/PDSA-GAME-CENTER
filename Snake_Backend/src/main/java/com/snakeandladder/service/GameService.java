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

        // Requirements: 
        // Set the number of ladders in the board as N-2
        // Set the number of snakes in the board as N-2
        int numSnakes = n - 2; 
        int numLadders = n - 2;

        // Snakes: 3 should be snakeone (drop 1 row)
        int snakeOneCount = 0;
        int maxSnakeOnes = 3;

        for (int i = 0; i < numSnakes; i++) {
            boolean forceSnakeOne = snakeOneCount < maxSnakeOnes;
            if (createSnake(n, totalCells, occupied, snakes, forceSnakeOne)) {
                if (forceSnakeOne) snakeOneCount++;
            }
        }

        // Ladders: 2 should be ladderup (vertical)
        int ladderUpCount = 0;
        int maxLadderUps = 2;

        for (int i = 0; i < numLadders; i++) {
            boolean forceVertical = ladderUpCount < maxLadderUps;
            if (createLadder(n, totalCells, occupied, ladders, forceVertical)) {
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
                // Random drop, but maybe 3 rows for "snakethree"? 
                // Let's randomize between 2 and max possible
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
            // Warning: Diagonal moves might be too far? 
            // Ideally keep it within reasonable horizontal distance
            
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
                // Try to ensure it leans (not vertical) if not forced, 
                // but random is fine, just re-roll if vertical?
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
