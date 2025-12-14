package com.PDSA.Traffic_Simulation.controller;

import com.PDSA.Traffic_Simulation.service.TrafficGameService;
import com.PDSA.Traffic_Simulation.data.*;
import com.PDSA.Traffic_Simulation.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for Traffic Simulation Game API
 */
@RestController
@RequestMapping("/traffic-game")
public class TrafficGameController {

    @Autowired
    private TrafficGameService gameService;

    /**
     * Register a new player
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerPlayer(@RequestBody Map<String, String> request) {
        try {
            String playerName = request.get("playerName");
            if (playerName == null || playerName.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("Player name is required"));
            }

            Player player = gameService.registerPlayer(playerName);
            return ResponseEntity.ok(createSuccessResponse("Player registered successfully", player));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(createErrorResponse("Error registering player: " + e.getMessage()));
        }
    }

    /**
     * Start a new game
     */
    @PostMapping("/start")
    public ResponseEntity<?> startGame(@RequestBody Map<String, Long> request) {
        try {
            Long playerId = request.get("playerId");
            if (playerId == null) {
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("Player ID is required"));
            }

            GameStartResponse response = gameService.startGame(playerId);
            return ResponseEntity.ok(createSuccessResponse("Game started", response));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(createErrorResponse("Error starting game: " + e.getMessage()));
        }
    }

    /**
     * Submit game answer
     */
    @PostMapping("/submit")
    public ResponseEntity<?> submitAnswer(@RequestBody GameSubmissionRequest request) {
        try {
            if (request.getRoundId() == null || request.getPlayerAnswer() == null) {
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("Round ID and Player Answer are required"));
            }

            GameResultResponse response = gameService.submitAnswer(request);
            return ResponseEntity.ok(createSuccessResponse("Answer submitted", response));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(createErrorResponse("Error submitting answer: " + e.getMessage()));
        }
    }

    /**
     * Get player's game history
     */
    @GetMapping("/history/{playerId}")
    public ResponseEntity<?> getPlayerHistory(@PathVariable Long playerId) {
        try {
            List<GameRound> history = gameService.getPlayerHistory(playerId);
            return ResponseEntity.ok(createSuccessResponse("Player history retrieved", history));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(createErrorResponse("Error retrieving history: " + e.getMessage()));
        }
    }

    /**
     * Get leaderboard
     */
    @GetMapping("/leaderboard")
    public ResponseEntity<?> getLeaderboard() {
        try {
            List<PlayerDTO> leaderboard = gameService.getLeaderboard();
            return ResponseEntity.ok(createSuccessResponse("Leaderboard retrieved", leaderboard));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(createErrorResponse("Error retrieving leaderboard: " + e.getMessage()));
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Traffic Simulation Game API");
        return ResponseEntity.ok(response);
    }

    // Helper methods
    private Map<String, Object> createSuccessResponse(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        response.put("data", data);
        return response;
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }
}
