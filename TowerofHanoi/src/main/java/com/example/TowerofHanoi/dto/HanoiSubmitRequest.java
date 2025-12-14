package com.example.TowerofHanoi.dto;

import java.util.List;

public class HanoiSubmitRequest {
    private String roundId;
    private String playerName;
    private Integer diskCount;
    private Integer pegCount;
    private List<String> moves;
    
    public HanoiSubmitRequest() {
    }
    
    public HanoiSubmitRequest(String roundId, String playerName, Integer diskCount, Integer pegCount, List<String> moves) {
        this.roundId = roundId;
        this.playerName = playerName;
        this.diskCount = diskCount;
        this.pegCount = pegCount;
        this.moves = moves;
    }
    
    // Getters and Setters
    public String getRoundId() {
        return roundId;
    }
    
    public void setRoundId(String roundId) {
        this.roundId = roundId;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    public Integer getDiskCount() {
        return diskCount;
    }
    
    public void setDiskCount(Integer diskCount) {
        this.diskCount = diskCount;
    }
    
    public Integer getPegCount() {
        return pegCount;
    }
    
    public void setPegCount(Integer pegCount) {
        this.pegCount = pegCount;
    }
    
    public List<String> getMoves() {
        return moves;
    }
    
    public void setMoves(List<String> moves) {
        this.moves = moves;
    }
}
