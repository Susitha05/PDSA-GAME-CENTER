package com.example.TowerofHanoi.controller;

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
@CrossOrigin(origins = "*")
public class HanoiController {
    
    @Autowired
    private HanoiService hanoiService;
    
    @GetMapping("/start")
    public ResponseEntity<HanoiStartResponse> startNewRound() {
        try {
            HanoiStartResponse response = hanoiService.startNewRound();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/submit")
    public ResponseEntity<HanoiSubmitResponse> submitRound(@RequestBody HanoiSubmitRequest request) {
        try {
            if (request == null || request.getRoundId() == null || request.getMoves() == null) {
                HanoiSubmitResponse errorResponse = new HanoiSubmitResponse(
                    false, "Invalid request: roundId and moves are required", 0, null);
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            HanoiSubmitResponse response = hanoiService.submitRound(request);
            
            if (response.getIsValid()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            HanoiSubmitResponse errorResponse = new HanoiSubmitResponse(
                false, "Error processing submission: " + e.getMessage(), 0, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
