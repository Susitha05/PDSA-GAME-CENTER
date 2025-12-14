package com.PDSA.Eight_Queens.service;

import com.PDSA.Eight_Queens.algorithm.SequentialQueensSolver;
import com.PDSA.Eight_Queens.algorithm.ThreadedQueensSolver;
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

import java.util.*;
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
     * Matches Python sequential.py implementation using sets for tracking
     */
    public ComputationResultDTO findAllSolutionsSequential() {
        long startTime = System.currentTimeMillis();

        // Use separate SequentialQueensSolver class
        SequentialQueensSolver solver = new SequentialQueensSolver();
        List<int[]> solutions = solver.findAllSolutions();

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
     * Matches Python threaded.py implementation with threads per first column
     */
    public ComputationResultDTO findAllSolutionsThreaded() throws InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();

        // Use separate ThreadedQueensSolver class
        ThreadedQueensSolver solver = new ThreadedQueensSolver();
        List<int[]> allSolutions = solver.findAllSolutions();

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

        // Log the submitted board for debugging
        System.out.println("DEBUG: Submitted board: " + java.util.Arrays.toString(board));

        // Validate solution
        if (!isValidSolution(board)) {
            System.out.println("DEBUG: Board failed isValidSolution check");
            throw new InvalidSolutionException(
                    "Invalid solution: Queens are attacking each other or invalid board configuration");
        }

        // Convert board to JSON string
        String boardJson = convertBoardToJson(board);
        System.out.println("DEBUG: Board JSON: " + boardJson);

        // Get time and moves with defaults if null
        int timeTaken = (request.getTimeTakenSeconds() != null) ? request.getTimeTakenSeconds() : 0;
        int moves = (request.getMovesCount() != null) ? request.getMovesCount() : 0;

        // Calculate score (lower is better: faster time + fewer moves = higher score)
        int score = calculateScore(timeTaken, moves);

        // Check if answers have been computed
        long totalAnswers = answerRepository.count();
        System.out.println("DEBUG: Total answers in database: " + totalAnswers);
        if (totalAnswers == 0) {
            throw new InvalidSolutionException(
                    "No solutions have been computed yet! Please run the Sequential or Threaded computation first to generate all 92 solutions.");
        }

        // Check if this is a valid answer (one of the 92 solutions)
        boolean exists = answerRepository.existsByBoard(boardJson);
        System.out.println("DEBUG: Answer exists in database: " + exists);
        if (!exists) {
            // Debug: Show first few answers from database
            List<Answer> sampleAnswers = answerRepository.findAllByOrderBySolutionNumberAsc()
                    .stream().limit(3).toList();
            System.out.println("DEBUG: First 3 answers in DB:");
            for (Answer answer : sampleAnswers) {
                System.out.println("  - " + answer.getBoard());
            }
            throw new InvalidSolutionException(
                    "This is not one of the 92 valid solutions. Your configuration is valid but doesn't match any computed solution. Try a different arrangement!");
        }

        // Check if solution already exists in player submissions
        boolean alreadyFoundByPlayer = gameRepository.existsByBoard(boardJson);
        long currentFoundSolutions = gameRepository.countDistinctBoards();

        if (alreadyFoundByPlayer) {
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

        System.out.println("DEBUG: Saving " + solutions.size() + " solutions as " + computationType);

        // Save new answers with solution numbers
        int solutionNumber = 1;
        for (int[] solution : solutions) {
            String boardJson = convertBoardToJson(solution);
            if (solutionNumber <= 3) {
                System.out.println("DEBUG: Saving solution #" + solutionNumber + ": " + boardJson);
            }
            Answer answer = new Answer(solutionNumber, boardJson, computationType);
            answerRepository.save(answer);
            solutionNumber++;
        }

        System.out.println("DEBUG: Saved all " + (solutionNumber - 1) + " solutions to database");
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

    /**
     * Get solutions as 2D board matrices (8x8) - matches Python's board
     * representation
     * This returns each solution as a full 8x8 matrix with 0s and 1s
     */
    public List<int[][]> getSolutionsAs2DBoards() {
        List<Answer> answers = answerRepository.findAllByOrderBySolutionNumberAsc();
        List<int[][]> boards = new ArrayList<>();

        for (Answer answer : answers) {
            try {
                int[] solution = objectMapper.readValue(answer.getBoard(), int[].class);
                int[][] board = new int[8][8];

                // Convert 1D solution to 2D board matrix
                for (int row = 0; row < 8; row++) {
                    for (int col = 0; col < 8; col++) {
                        board[row][col] = (solution[row] == col) ? 1 : 0;
                    }
                }
                boards.add(board);
            } catch (Exception e) {
                // Skip invalid solutions
            }
        }

        return boards;
    }

    /**
     * Get solutions in Python-compatible format: List<List<Map<"row", "col">>>
     * Matches Python's solution format: [{'row': 0, 'col': 1}, ...]
     */
    public List<List<Map<String, Integer>>> getSolutionsAsPythonFormat() {
        List<Answer> answers = answerRepository.findAllByOrderBySolutionNumberAsc();
        List<List<Map<String, Integer>>> pythonFormatSolutions = new ArrayList<>();

        for (Answer answer : answers) {
            try {
                int[] solution = objectMapper.readValue(answer.getBoard(), int[].class);
                List<Map<String, Integer>> positions = new ArrayList<>();

                for (int row = 0; row < solution.length; row++) {
                    Map<String, Integer> position = new HashMap<>();
                    position.put("row", row);
                    position.put("col", solution[row]);
                    positions.add(position);
                }

                pythonFormatSolutions.add(positions);
            } catch (Exception e) {
                // Skip invalid solutions
            }
        }

        return pythonFormatSolutions;
    }

    /**
     * Convert a 1D board solution to 2D matrix representation
     * Used to match Python's board visualization
     */
    public int[][] convertTo2DBoard(int[] solution) {
        int[][] board = new int[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = (solution[row] == col) ? 1 : 0;
            }
        }
        return board;
    }

    /**
     * Get all solutions with both formats (1D array, 2D matrix, and position list)
     * Comprehensive response matching Python behavior
     */
    public Map<String, Object> getAllSolutionsAllFormats() {
        List<Answer> answers = answerRepository.findAllByOrderBySolutionNumberAsc();
        List<int[]> solutions1D = new ArrayList<>();
        List<int[][]> solutions2D = new ArrayList<>();
        List<List<Map<String, Integer>>> solutionsPython = new ArrayList<>();

        for (Answer answer : answers) {
            try {
                int[] solution = objectMapper.readValue(answer.getBoard(), int[].class);
                solutions1D.add(solution);
                solutions2D.add(convertTo2DBoard(solution));

                // Python format
                List<Map<String, Integer>> positions = new ArrayList<>();
                for (int row = 0; row < solution.length; row++) {
                    Map<String, Integer> position = new HashMap<>();
                    position.put("row", row);
                    position.put("col", solution[row]);
                    positions.add(position);
                }
                solutionsPython.add(positions);
            } catch (Exception e) {
                // Skip invalid solutions
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("solutions_1d", solutions1D);
        result.put("solutions_2d", solutions2D);
        result.put("solutions_python", solutionsPython);
        result.put("count", solutions1D.size());

        return result;
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
