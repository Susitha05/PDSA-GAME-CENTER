package com.example.TowerofHanoi.dto;

public class HanoiStartResponse {
    private String roundId;
    private Integer diskCount;
    private Integer pegCount;
    
    public HanoiStartResponse() {
    }
    
    public HanoiStartResponse(String roundId, Integer diskCount, Integer pegCount) {
        this.roundId = roundId;
        this.diskCount = diskCount;
        this.pegCount = pegCount;
    }
    
    // Getters and Setters
    public String getRoundId() {
        return roundId;
    }
    
    public void setRoundId(String roundId) {
        this.roundId = roundId;
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
}
