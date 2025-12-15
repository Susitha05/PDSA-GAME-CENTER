package com.PDSA.Eight_Queens.dto;

public class ComputationResultDTO {

    private String computationType;
    private int totalSolutions;
    private long timeTakenMs;
    private String createdAt;

    // Constructors
    public ComputationResultDTO() {
    }

    public ComputationResultDTO(String computationType, int totalSolutions, long timeTakenMs, String createdAt) {
        this.computationType = computationType;
        this.totalSolutions = totalSolutions;
        this.timeTakenMs = timeTakenMs;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getComputationType() {
        return computationType;
    }

    public void setComputationType(String computationType) {
        this.computationType = computationType;
    }

    public int getTotalSolutions() {
        return totalSolutions;
    }

    public void setTotalSolutions(int totalSolutions) {
        this.totalSolutions = totalSolutions;
    }

    public long getTimeTakenMs() {
        return timeTakenMs;
    }

    public void setTimeTakenMs(long timeTakenMs) {
        this.timeTakenMs = timeTakenMs;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
