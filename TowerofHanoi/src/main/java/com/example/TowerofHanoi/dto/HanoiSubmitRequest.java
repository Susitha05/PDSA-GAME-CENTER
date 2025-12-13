package com.example.TowerofHanoi.dto;

import java.util.List;

public class HanoiSubmitRequest {
    private String roundId;
    private List<String> moves;
    
    public HanoiSubmitRequest() {
    }
    
    public HanoiSubmitRequest(String roundId, List<String> moves) {
        this.roundId = roundId;
        this.moves = moves;
    }
    
    // Getters and Setters
    public String getRoundId() {
        return roundId;
    }
    
    public void setRoundId(String roundId) {
        this.roundId = roundId;
    }
    
    public List<String> getMoves() {
        return moves;
    }
    
    public void setMoves(List<String> moves) {
        this.moves = moves;
    }
}
