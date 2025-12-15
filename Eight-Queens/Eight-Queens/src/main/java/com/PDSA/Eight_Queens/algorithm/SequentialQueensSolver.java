package com.PDSA.Eight_Queens.algorithm;

import java.util.*;

/**
 * Sequential algorithm to find all 8-Queens solutions using backtracking
 * Matches Python sequential.py implementation using sets for tracking
 */
public class SequentialQueensSolver {

    /**
     * Find all solutions to the 8 queens problem using sequential backtracking
     * 
     * @return List of solutions (each solution is an array of queen column
     *         positions)
     */
    public List<int[]> findAllSolutions() {
        List<int[]> solutions = new ArrayList<>();

        // Use sets to track conflicts like Python version
        Set<Integer> cols = new HashSet<>();
        Set<Integer> posDiag = new HashSet<>(); // (r + c)
        Set<Integer> negDiag = new HashSet<>(); // (r - c)
        int[][] board = new int[8][8];

        backtrack(0, cols, posDiag, negDiag, board, solutions);

        return solutions;
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
