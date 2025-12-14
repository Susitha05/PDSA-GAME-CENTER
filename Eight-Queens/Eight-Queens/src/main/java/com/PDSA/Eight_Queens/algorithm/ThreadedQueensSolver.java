package com.PDSA.Eight_Queens.algorithm;

import java.util.*;
import java.util.concurrent.*;

/**
 * Threaded algorithm to find all 8-Queens solutions using parallel processing
 * Matches Python threaded.py implementation with threads per first column
 */
public class ThreadedQueensSolver {

    /**
     * Find all solutions to the 8 queens problem using threading
     * 
     * @return List of solutions (each solution is an array of queen column
     *         positions)
     */
    public List<int[]> findAllSolutions() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(8);
        List<Future<List<int[]>>> futures = new ArrayList<>();

        // Start a thread for each possible first queen position (like Python version)
        for (int startCol = 0; startCol < 8; startCol++) {
            final int col = startCol;
            Future<List<int[]>> future = executor.submit(() -> workerThread(col));
            futures.add(future);
        }

        // Collect all solutions from threads
        List<int[]> allSolutions = new ArrayList<>();
        for (Future<List<int[]>> future : futures) {
            allSolutions.addAll(future.get());
        }

        executor.shutdown();
        return allSolutions;
    }

    /**
     * Worker thread that finds solutions starting with queen in specified column
     */
    private List<int[]> workerThread(int startCol) {
        List<int[]> localSolutions = new ArrayList<>();

        // Each thread has its own sets
        Set<Integer> cols = new HashSet<>();
        Set<Integer> posDiag = new HashSet<>(); // (r + c)
        Set<Integer> negDiag = new HashSet<>(); // (r - c)
        int[][] board = new int[8][8];

        // Place first queen in the specified column
        cols.add(startCol);
        posDiag.add(0 + startCol);
        negDiag.add(0 - startCol);
        board[0][startCol] = 1;

        // Continue from row 1
        backtrack(1, cols, posDiag, negDiag, board, localSolutions);

        return localSolutions;
    }

    /**
     * Backtracking algorithm using sets for conflict tracking
     */
    private void backtrack(int r, Set<Integer> cols, Set<Integer> posDiag,
            Set<Integer> negDiag, int[][] board, List<int[]> solutions) {
        if (r == 8) {
            // Add the solution - convert 2D board to 1D array format
            int[] solution = new int[8];
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board[i][j] == 1) {
                        solution[i] = j;
                        break;
                    }
                }
            }
            solutions.add(solution);
            return;
        }

        for (int c = 0; c < 8; c++) {
            // Check if this position conflicts (same as Python: if c in cols or ...)
            if (cols.contains(c) || posDiag.contains(r + c) || negDiag.contains(r - c)) {
                continue;
            }

            // Place queen
            cols.add(c);
            posDiag.add(r + c);
            negDiag.add(r - c);
            board[r][c] = 1;

            backtrack(r + 1, cols, posDiag, negDiag, board, solutions);

            // Remove queen (backtrack)
            cols.remove(c);
            posDiag.remove(r + c);
            negDiag.remove(r - c);
            board[r][c] = 0;
        }
    }
}
