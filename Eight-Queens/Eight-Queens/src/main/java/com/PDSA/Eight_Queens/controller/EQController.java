package com.PDSA.Eight_Queens.controller;

import com.PDSA.Eight_Queens.data.EightQueens;
import com.PDSA.Eight_Queens.data.EightQueensRepository;
import com.PDSA.Eight_Queens.dto.AnswerDTO;
import com.PDSA.Eight_Queens.dto.ComputationResultDTO;
import com.PDSA.Eight_Queens.dto.GameSubmissionRequest;
import com.PDSA.Eight_Queens.dto.GameSubmissionResponse;
import com.PDSA.Eight_Queens.dto.ScoreboardEntry;
import com.PDSA.Eight_Queens.service.EQService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping({ "/api/eight-queens", "/api/queens" })
@CrossOrigin(origins = "http://localhost:3000")
public class EQController {

    @Autowired
    private EQService eqService;

    @Autowired
    private EightQueensRepository gameRepository;

    /**
     * Compute all solutions using sequential algorithm
     * POST /api/eight-queens/compute/sequential
     */
    @PostMapping("/compute/sequential")
    public ResponseEntity<ComputationResultDTO> computeSequential() {
        ComputationResultDTO result = eqService.findAllSolutionsSequential();
        return ResponseEntity.ok(result);
    }

    /**
     * Compute all solutions using threaded algorithm
     * POST /api/eight-queens/compute/threaded
     */
    @PostMapping("/compute/threaded")
    public ResponseEntity<ComputationResultDTO> computeThreaded() {
        try {
            ComputationResultDTO result = eqService.findAllSolutionsThreaded();
            return ResponseEntity.ok(result);
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Submit a player's solution
     * POST /api/eight-queens/submit
     */
    @PostMapping("/submit")
    public ResponseEntity<GameSubmissionResponse> submitSolution(@Valid @RequestBody GameSubmissionRequest request) {
        GameSubmissionResponse response = eqService.submitSolution(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all computation results
     * GET /api/eight-queens/computations
     */
    @GetMapping("/computations")
    public ResponseEntity<List<ComputationResultDTO>> getAllComputations() {
        List<ComputationResultDTO> results = eqService.getAllComputationResults();
        return ResponseEntity.ok(results);
    }

    /**
     * Get game statistics
     * GET /api/eight-queens/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<EQService.GameStatistics> getStatistics() {
        EQService.GameStatistics stats = eqService.getGameStatistics();
        return ResponseEntity.ok(stats);
    }

    /**
     * Reset game (clear all solutions)
     * DELETE /api/eight-queens/reset
     */
    @DeleteMapping("/reset")
    public ResponseEntity<String> resetGame() {
        eqService.resetGame();
        return ResponseEntity.ok("Game reset successfully. All player solutions cleared.");
    }

    /**
     * Get all answers (92 solutions)
     * GET /api/eight-queens/answers
     */
    @GetMapping("/answers")
    public ResponseEntity<List<AnswerDTO>> getAllAnswers() {
        List<AnswerDTO> answers = eqService.getAllAnswers();
        return ResponseEntity.ok(answers);
    }

    /**
     * Get scoreboard with top players
     * GET /api/eight-queens/scoreboard
     */
    @GetMapping("/scoreboard")
    public ResponseEntity<List<ScoreboardEntry>> getScoreboard() {
        List<ScoreboardEntry> scoreboard = eqService.getScoreboard();
        return ResponseEntity.ok(scoreboard);
    }

    /**
     * Health check endpoint
     * GET /api/eight-queens/health
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Eight Queens API is running");
    }

    /**
     * Get solutions in sequential mode (Python-compatible format)
     * GET /api/queens/solutions/sequential
     */
    @GetMapping("/solutions/sequential")
    public ResponseEntity<Map<String, Object>> getSequentialSolutions() {
        try {
            ComputationResultDTO result = eqService.findAllSolutionsSequential();
            List<AnswerDTO> answers = eqService.getAllAnswers();

            Map<String, Object> response = new HashMap<>();
            response.put("solutions", convertAnswersToPositionFormat(answers));
            response.put("count", answers.size());
            response.put("time_taken", result.getTimeTakenMs());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to find solutions");
            errorResponse.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get solutions in threaded mode (Python-compatible format)
     * GET /api/queens/solutions/threaded
     */
    @GetMapping("/solutions/threaded")
    public ResponseEntity<Map<String, Object>> getThreadedSolutions() {
        try {
            ComputationResultDTO result = eqService.findAllSolutionsThreaded();
            List<AnswerDTO> answers = eqService.getAllAnswers();

            Map<String, Object> response = new HashMap<>();
            response.put("solutions", convertAnswersToPositionFormat(answers));
            response.put("count", answers.size());
            response.put("time_taken", result.getTimeTakenMs());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to find solutions");
            errorResponse.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Create a new game (Python-compatible endpoint)
     * POST /api/queens/games
     */
    @PostMapping("/games")
    public ResponseEntity<Map<String, Object>> createGame(@RequestBody Map<String, String> request) {
        String playerName = request.get("player_name");
        if (playerName == null || playerName.trim().isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Player name is required");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("game_id", System.currentTimeMillis()); // Generate a game ID
        response.put("message", "Game created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Convert AnswerDTO list to Python-compatible position format
     * Each solution: [{"row": 0, "col": 1}, {"row": 1, "col": 3}, ...]
     */
    private List<List<Map<String, Integer>>> convertAnswersToPositionFormat(List<AnswerDTO> answers) {
        List<List<Map<String, Integer>>> result = new ArrayList<>();

        for (AnswerDTO answer : answers) {
            List<Map<String, Integer>> solution = new ArrayList<>();
            int[] board = answer.getBoard();

            for (int row = 0; row < board.length; row++) {
                Map<String, Integer> position = new HashMap<>();
                position.put("row", row);
                position.put("col", board[row]);
                solution.add(position);
            }

            result.add(solution);
        }

        return result;
    }

    /**
     * Get solutions as 2D board matrices (matches Python board representation)
     * GET /api/eight-queens/solutions/2d
     */
    @GetMapping("/solutions/2d")
    public ResponseEntity<Map<String, Object>> getSolutions2D() {
        List<int[][]> boards = eqService.getSolutionsAs2DBoards();
        Map<String, Object> response = new HashMap<>();
        response.put("solutions", boards);
        response.put("count", boards.size());
        response.put("format", "2D_MATRIX");
        return ResponseEntity.ok(response);
    }

    /**
     * Get all solutions in all formats (comprehensive endpoint)
     * GET /api/eight-queens/solutions/all-formats
     */
    @GetMapping("/solutions/all-formats")
    public ResponseEntity<Map<String, Object>> getAllFormats() {
        Map<String, Object> allFormats = eqService.getAllSolutionsAllFormats();
        return ResponseEntity.ok(allFormats);
    }

    /**
     * Get game history/submissions (Python-compatible)
     * GET /api/queens/games
     */
    @GetMapping("/games")
    public ResponseEntity<List<Map<String, Object>>> getGames() {
        List<EightQueens> games = gameRepository.findTop20ByOrderByScoreDesc();
        List<Map<String, Object>> response = new ArrayList<>();

        for (EightQueens game : games) {
            Map<String, Object> gameData = new HashMap<>();
            gameData.put("id", game.getId());
            gameData.put("player_name", game.getName());
            gameData.put("solution_found", game.getBoard());
            gameData.put("score", game.getScore());
            gameData.put("time_taken_seconds", game.getTimeTakenSeconds());
            gameData.put("moves_count", game.getMovesCount());
            gameData.put("date", game.getCreatedAt().toString());
            response.add(gameData);
        }

        return ResponseEntity.ok(response);
    }
}