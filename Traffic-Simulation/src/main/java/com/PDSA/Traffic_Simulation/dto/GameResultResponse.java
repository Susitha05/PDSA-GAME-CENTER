package com.PDSA.Traffic_Simulation.dto;

public class GameResultResponse {
    private Integer playerAnswer;
    private Boolean isCorrect;
    private Integer correctAnswer;
    private Integer score;
    
    // Ford-Fulkerson timing (in various units for precision)
    private Long fordFulkersonTimeMs;
    private Long fordFulkersonTimeUs;
    private Long fordFulkersonTimeNs;
    
    // Edmonds-Karp timing (in various units for precision)
    private Long edmondsKarpTimeMs;
    private Long edmondsKarpTimeUs;
    private Long edmondsKarpTimeNs;
    
    // Legacy fields for backward compatibility
    private Long algorithmExecutionTime;
    
    private String message;

    public GameResultResponse() {}

    public GameResultResponse(Integer playerAnswer, Boolean isCorrect, Integer correctAnswer, 
                              Integer score, Long ffTimeMs, Long ekTimeMs, String message) {
        this.playerAnswer = playerAnswer;
        this.isCorrect = isCorrect;
        this.correctAnswer = correctAnswer;
        this.score = score;
        this.fordFulkersonTimeMs = ffTimeMs;
        this.edmondsKarpTimeMs = ekTimeMs;
        this.algorithmExecutionTime = ffTimeMs;
        this.message = message;
    }

    // Getters and Setters
    public Integer getPlayerAnswer() {
        return playerAnswer;
    }

    public void setPlayerAnswer(Integer playerAnswer) {
        this.playerAnswer = playerAnswer;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Integer getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Integer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Long getFordFulkersonTimeMs() {
        return fordFulkersonTimeMs;
    }

    public void setFordFulkersonTimeMs(Long fordFulkersonTimeMs) {
        this.fordFulkersonTimeMs = fordFulkersonTimeMs;
    }

    public Long getFordFulkersonTimeUs() {
        return fordFulkersonTimeUs;
    }

    public void setFordFulkersonTimeUs(Long fordFulkersonTimeUs) {
        this.fordFulkersonTimeUs = fordFulkersonTimeUs;
    }

    public Long getFordFulkersonTimeNs() {
        return fordFulkersonTimeNs;
    }

    public void setFordFulkersonTimeNs(Long fordFulkersonTimeNs) {
        this.fordFulkersonTimeNs = fordFulkersonTimeNs;
    }

    public Long getEdmondsKarpTimeMs() {
        return edmondsKarpTimeMs;
    }

    public void setEdmondsKarpTimeMs(Long edmondsKarpTimeMs) {
        this.edmondsKarpTimeMs = edmondsKarpTimeMs;
    }

    public Long getEdmondsKarpTimeUs() {
        return edmondsKarpTimeUs;
    }

    public void setEdmondsKarpTimeUs(Long edmondsKarpTimeUs) {
        this.edmondsKarpTimeUs = edmondsKarpTimeUs;
    }

    public Long getEdmondsKarpTimeNs() {
        return edmondsKarpTimeNs;
    }

    public void setEdmondsKarpTimeNs(Long edmondsKarpTimeNs) {
        this.edmondsKarpTimeNs = edmondsKarpTimeNs;
    }

    public Long getAlgorithmExecutionTime() {
        return algorithmExecutionTime;
    }

    public void setAlgorithmExecutionTime(Long algorithmExecutionTime) {
        this.algorithmExecutionTime = algorithmExecutionTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
