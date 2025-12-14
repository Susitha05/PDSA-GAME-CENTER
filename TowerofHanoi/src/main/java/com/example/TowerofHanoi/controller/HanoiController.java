package com.example.TowerofHanoi.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.TowerofHanoi.dto.HanoiStartResponse;
import com.example.TowerofHanoi.dto.HanoiSubmitRequest;
import com.example.TowerofHanoi.dto.HanoiSubmitResponse;
import com.example.TowerofHanoi.service.HanoiService;

@RestController
@RequestMapping("/api/hanoi")
public class HanoiController {
    
    private static final Logger logger = LoggerFactory.getLogger(HanoiController.class);
    
    @Autowired(required = false)
    private HanoiService hanoiService;
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", hanoiService != null ? "LOADED" : "NULL");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/start")
    public ResponseEntity<HanoiStartResponse> startNewRound() {
        try {
            logger.info("Received request to start new round");
            if (hanoiService == null) {
                logger.error("HanoiService is null!");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            HanoiStartResponse response = hanoiService.startNewRound();
            logger.info("Successfully created new round with ID: {}", response.getRoundId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error starting new round", e);
            e.printStackTrace(); // Print stack trace for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("{\"status\":\"Backend is running\"}");
    }
    
    @PostMapping("/submit")
    public ResponseEntity<HanoiSubmitResponse> submitRound(@RequestBody HanoiSubmitRequest request) {
        try {
            logger.info("Received submit request for round: {}", request != null ? request.getRoundId() : "null");
            
            if (request == null || request.getRoundId() == null || request.getMoves() == null) {
                HanoiSubmitResponse errorResponse = new HanoiSubmitResponse(
                    false, "Invalid request: roundId and moves are required", 0, null);
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            HanoiSubmitResponse response = hanoiService.submitRound(request);
            
            if (response.getIsValid()) {
                logger.info("Round {} submitted successfully", request.getRoundId());
                return ResponseEntity.ok(response);
            } else {
                logger.warn("Round {} submission was invalid: {}", request.getRoundId(), response.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            logger.error("Error processing submission", e);
            HanoiSubmitResponse errorResponse = new HanoiSubmitResponse(
                false, "Error processing submission: " + e.getMessage(), 0, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
