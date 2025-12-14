package com.PDSA.Eight_Queens.controller;

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

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/eight-queens")
@CrossOrigin(origins = "http://localhost:3000")
public class EQController {

    @Autowired
    private EQService eqService;

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
}
