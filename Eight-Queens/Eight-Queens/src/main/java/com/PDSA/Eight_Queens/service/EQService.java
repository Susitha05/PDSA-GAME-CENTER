package com.PDSA.Eight_Queens.service;

import com.PDSA.Eight_Queens.data.*;
import com.PDSA.Eight_Queens.dto.AnswerDTO;
import com.PDSA.Eight_Queens.dto.ComputationResultDTO;
import com.PDSA.Eight_Queens.dto.GameSubmissionRequest;
import com.PDSA.Eight_Queens.dto.GameSubmissionResponse;
import com.PDSA.Eight_Queens.dto.ScoreboardEntry;
import com.PDSA.Eight_Queens.exception.DuplicateSolutionException;
import com.PDSA.Eight_Queens.exception.InvalidSolutionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class EQService {

    private static final int BOARD_SIZE = 8;
    private static final int TOTAL_SOLUTIONS = 92; // Known number of solutions for 8-Queens

    @Autowired
    private EightQueensRepository gameRepository;

    @Autowired
    private ComputationResultRepository computationRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Sequential algorithm to find all 8-Queens solutions using backtracking
     */
    public ComputationResultDTO findAllSolutionsSequential() {
        long startTime = System.currentTimeMillis();
        List<int[]> solutions = new ArrayList<>();

        int[] board = new int[BOARD_SIZE];
        solveNQueensSequential(board, 0, solutions);

        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;

        // Save solutions to answers table
        saveAnswers(solutions, "SEQUENTIAL");

        // Save computation result
        ComputationResult result = new ComputationResult("SEQUENTIAL", solutions.size(), timeTaken);
        computationRepository.save(result);

        return new ComputationResultDTO("SEQUENTIAL", solutions.size(), timeTaken, result.getCreatedAt().toString());
    }

    /**
     * Threaded algorithm to find all 8-Queens solutions using parallel processing
     */
    public ComputationResultDTO findAllSolutionsThreaded() throws InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();

        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<List<int[]>>> futures = new ArrayList<>();

        // Divide work by first queen position
        for (int firstCol = 0; firstCol < BOARD_SIZE; firstCol++) {
            final int col = firstCol;
            Future<List<int[]>> future = executor.submit(() -> {
                List<int[]> threadSolutions = new ArrayList<>();
                int[] board = new int[BOARD_SIZE];
                board[0] = col;
                solveNQueensSequential(board, 1, threadSolutions);
                return threadSolutions;
            });
            futures.add(future);
        }

        // Collect all solutions from threads
        List<int[]> allSolutions = new ArrayList<>();
        for (Future<List<int[]>> future : futures) {
            allSolutions.addAll(future.get());
        }

        executor.shutdown();
        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;

        // Save solutions to answers table
        saveAnswers(allSolutions, "THREADED");

        // Save computation result
        ComputationResult result = new ComputationResult("THREADED", allSolutions.size(), timeTaken);
        computationRepository.save(result);

        return new ComputationResultDTO("THREADED", allSolutions.size(), timeTaken, result.getCreatedAt().toString());
    }

    /**
     * Backtracking algorithm to solve N-Queens
     */
    private void solveNQueensSequential(int[] board, int row, List<int[]> solutions) {
        if (row == BOARD_SIZE) {
            solutions.add(board.clone());
            return;
        }

        for (int col = 0; col < BOARD_SIZE; col++) {
            if (isSafe(board, row, col)) {
                board[row] = col;
                solveNQueensSequential(board, row + 1, solutions);
            }
        }
    }

    /**
     * Check if placing a queen at (row, col) is safe
     */
    private boolean isSafe(int[] board, int row, int col) {
        for (int i = 0; i < row; i++) {
            int placedCol = board[i];
            // Check column conflict
            if (placedCol == col) {
                return false;
            }
            // Check diagonal conflict
            if (Math.abs(placedCol - col) == Math.abs(i - row)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validate if a board configuration is a valid solution
     */
    public boolean isValidSolution(int[] board) {
        if (board == null || board.length != BOARD_SIZE) {
            return false;
        }

        // Check if all values are within valid range
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i] < 0 || board[i] >= BOARD_SIZE) {
                return false;
            }
        }

        // Check for conflicts
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = i + 1; j < BOARD_SIZE; j++) {
                // Check column conflict
                if (board[i] == board[j]) {
                    return false;
                }
                // Check diagonal conflict
                if (Math.abs(board[i] - board[j]) == Math.abs(i - j)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Submit a player's solution
     */
    @Transactional
    public GameSubmissionResponse submitSolution(GameSubmissionRequest request) {
        int[] board = request.getBoard();
        String name = request.getName();

        // Validate solution
        if (!isValidSolution(board)) {
            throw new InvalidSolutionException(
                    "Invalid solution: Queens are attacking each other or invalid board configuration");
        }

        // Convert board to JSON string
        String boardJson = convertBoardToJson(board);

        // Get time and moves with defaults if null
        int timeTaken = (request.getTimeTakenSeconds() != null) ? request.getTimeTakenSeconds() : 0;
        int moves = (request.getMovesCount() != null) ? request.getMovesCount() : 0;

        // Calculate score (lower is better: faster time + fewer moves = higher score)
        int score = calculateScore(timeTaken, moves);

        // Check if this is a valid answer (one of the 92 solutions)
        if (!answerRepository.existsByBoard(boardJson)) {
            throw new InvalidSolutionException(
                    "This is not one of the 92 valid solutions. Please run the algorithm first or try a different solution.");
        }

        // Check if solution already exists in player submissions
        boolean exists = gameRepository.existsByBoard(boardJson);
        long currentFoundSolutions = gameRepository.countDistinctBoards();

        if (exists) {
            // Solution already found, but still save this player's attempt
            EightQueens game = new EightQueens(name, boardJson, timeTaken, moves, score);
            gameRepository.save(game);

            return new GameSubmissionResponse(
                    true,
                    "Correct solution! However, this solution has already been found by another player. Please try to find a different solution.",
                    true,
                    (int) currentFoundSolutions,
                    TOTAL_SOLUTIONS);
        }

        // Save new solution
        EightQueens game = new EightQueens(name, boardJson, timeTaken, moves, score);
        gameRepository.save(game);
        currentFoundSolutions++;

        // Check if all solutions have been found
        if (currentFoundSolutions >= TOTAL_SOLUTIONS) {
            // All solutions found - clear flag (in future implementations)
            return new GameSubmissionResponse(
                    true,
                    "Congratulations! You found a new solution! All " + TOTAL_SOLUTIONS
                            + " solutions have now been discovered!",
                    false,
                    (int) currentFoundSolutions,
                    TOTAL_SOLUTIONS);
        }

        return new GameSubmissionResponse(
                true,
                "Congratulations! You found a new solution!",
                false,
                (int) currentFoundSolutions,
                TOTAL_SOLUTIONS);
    }

    /**
     * Get all computation results
     */
    public List<ComputationResultDTO> getAllComputationResults() {
        return computationRepository.findAll().stream()
                .map(result -> new ComputationResultDTO(
                        result.getComputationType(),
                        result.getTotalSolutions(),
                        result.getTimeTakenMs(),
                        result.getCreatedAt().toString()))
                .collect(Collectors.toList());
    }

    /**
     * Calculate score based on time and moves
     * Formula: 10000 - (time * 10 + moves * 5)
     * Lower time and fewer moves = higher score
     */
    private int calculateScore(int timeTakenSeconds, int movesCount) {
        int score = 10000 - (timeTakenSeconds * 10 + movesCount * 5);
        return Math.max(score, 0); // Ensure score is not negative
    }

    /**
     * Get scoreboard with top players
     */
    public List<ScoreboardEntry> getScoreboard() {
        List<EightQueens> topPlayers = gameRepository.findAllByOrderByScoreDesc();

        List<ScoreboardEntry> scoreboard = new ArrayList<>();
        int rank = 1;
        for (EightQueens player : topPlayers) {
            ScoreboardEntry entry = new ScoreboardEntry(
                    player.getName(),
                    player.getScore(),
                    player.getTimeTakenSeconds(),
                    player.getMovesCount(),
                    player.getCreatedAt().toString(),
                    rank++);
            scoreboard.add(entry);
        }

        return scoreboard;
    }

    /**
     * Get game statistics
     */
    public GameStatistics getGameStatistics() {
        long totalSubmissions = gameRepository.count();
        long uniqueSolutions = gameRepository.countDistinctBoards();
        long totalAnswersComputed = answerRepository.count();

        return new GameStatistics(
                (int) totalSubmissions,
                (int) uniqueSolutions,
                TOTAL_SOLUTIONS,
                (int) totalAnswersComputed);
    }

    /**
     * Save computed solutions to answers table
     */
    private void saveAnswers(List<int[]> solutions, String computationType) {
        // Clear existing answers to avoid duplicates
        answerRepository.deleteAll();

        // Save new answers with solution numbers
        int solutionNumber = 1;
        for (int[] solution : solutions) {
            String boardJson = convertBoardToJson(solution);
            Answer answer = new Answer(solutionNumber, boardJson, computationType);
            answerRepository.save(answer);
            solutionNumber++;
        }
    }

    /**
     * Get all answers (92 solutions)
     */
    public List<AnswerDTO> getAllAnswers() {
        List<Answer> answers = answerRepository.findAllByOrderBySolutionNumberAsc();
        return answers.stream()
                .map(answer -> {
                    try {
                        int[] board = objectMapper.readValue(answer.getBoard(), int[].class);
                        return new AnswerDTO(
                                answer.getId(),
                                answer.getSolutionNumber(),
                                board,
                                answer.getComputationType(),
                                answer.getCreatedAt().toString());
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }

    /**
     * Convert board array to JSON string
     */
    private String convertBoardToJson(int[] board) {
        try {
            return objectMapper.writeValueAsString(board);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting board to JSON", e);
        }
    }

    /**
     * Reset game - clear all player solutions (keep computation results)
     */
    @Transactional
    public void resetGame() {
        gameRepository.deleteAll();
    }

    // Inner class for statistics
    public static class GameStatistics {
        private int totalSubmissions;
        private int uniqueSolutionsFound;
        private int totalPossibleSolutions;
        private int totalAnswersComputed;

        public GameStatistics(int totalSubmissions, int uniqueSolutionsFound,
                int totalPossibleSolutions, int totalAnswersComputed) {
            this.totalSubmissions = totalSubmissions;
            this.uniqueSolutionsFound = uniqueSolutionsFound;
            this.totalPossibleSolutions = totalPossibleSolutions;
            this.totalAnswersComputed = totalAnswersComputed;
        }

        public int getTotalSubmissions() {
            return totalSubmissions;
        }

        public void setTotalSubmissions(int totalSubmissions) {
            this.totalSubmissions = totalSubmissions;
        }

        public int getUniqueSolutionsFound() {
            return uniqueSolutionsFound;
        }

        public void setUniqueSolutionsFound(int uniqueSolutionsFound) {
            this.uniqueSolutionsFound = uniqueSolutionsFound;
        }

        public int getTotalPossibleSolutions() {
            return totalPossibleSolutions;
        }

        public void setTotalPossibleSolutions(int totalPossibleSolutions) {
            this.totalPossibleSolutions = totalPossibleSolutions;
        }

        public int getTotalAnswersComputed() {
            return totalAnswersComputed;
        }

        public void setTotalAnswersComputed(int totalAnswersComputed) {
            this.totalAnswersComputed = totalAnswersComputed;
        }
    }
}
