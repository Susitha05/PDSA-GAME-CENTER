package com.PDSA.Traffic_Simulation.dto;

public class GameSubmissionRequest {
    private Long roundId;
    private Integer playerAnswer;
    private String algorithmUsed; // FORD_FULKERSON or DINIC

    public GameSubmissionRequest() {}

    public GameSubmissionRequest(Long roundId, Integer playerAnswer, String algorithmUsed) {
        this.roundId = roundId;
        this.playerAnswer = playerAnswer;
        this.algorithmUsed = algorithmUsed;
    }

    public Long getRoundId() {
        return roundId;
    }

    public void setRoundId(Long roundId) {
        this.roundId = roundId;
    }

    public Integer getPlayerAnswer() {
        return playerAnswer;
    }

    public void setPlayerAnswer(Integer playerAnswer) {
        this.playerAnswer = playerAnswer;
    }

    public String getAlgorithmUsed() {
        return algorithmUsed;
    }

    public void setAlgorithmUsed(String algorithmUsed) {
        this.algorithmUsed = algorithmUsed;
    }
}
