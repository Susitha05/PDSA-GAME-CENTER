package com.example.TowerofHanoi.dto;

import java.util.List;
import java.util.Map;

public class HanoiSubmitResponse {
    private Boolean isValid;
    private String message;
    private Integer userMoveCount;
    private Map<String, AlgorithmResult> algorithmResults;
    
    public HanoiSubmitResponse() {
    }
    
    public HanoiSubmitResponse(Boolean isValid, String message, Integer userMoveCount, Map<String, AlgorithmResult> algorithmResults) {
        this.isValid = isValid;
        this.message = message;
        this.userMoveCount = userMoveCount;
        this.algorithmResults = algorithmResults;
    }
    
    public Boolean getIsValid() {
        return isValid;
    }
    
    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Integer getUserMoveCount() {
        return userMoveCount;
    }
    
    public void setUserMoveCount(Integer userMoveCount) {
        this.userMoveCount = userMoveCount;
    }
    
    public Map<String, AlgorithmResult> getAlgorithmResults() {
        return algorithmResults;
    }
    
    public void setAlgorithmResults(Map<String, AlgorithmResult> algorithmResults) {
        this.algorithmResults = algorithmResults;
    }
    
    public static class AlgorithmResult {
        private Integer moveCount;
        private Long executionTimeNanos;
        private Double executionTimeMs;
        private List<String> sequence;
        
        public AlgorithmResult() {
        }
        
        public AlgorithmResult(Integer moveCount, Long executionTimeNanos, List<String> sequence) {
            this.moveCount = moveCount;
            this.executionTimeNanos = executionTimeNanos;
            this.executionTimeMs = executionTimeNanos / 1_000_000.0;
            this.sequence = sequence;
        }
        
        // Getters and Setters
        public Integer getMoveCount() {
            return moveCount;
        }
        
        public void setMoveCount(Integer moveCount) {
            this.moveCount = moveCount;
        }
        
        public Long getExecutionTimeNanos() {
            return executionTimeNanos;
        }
        
        public void setExecutionTimeNanos(Long executionTimeNanos) {
            this.executionTimeNanos = executionTimeNanos;
        }
        
        public Double getExecutionTimeMs() {
            return executionTimeMs;
        }
        
        public void setExecutionTimeMs(Double executionTimeMs) {
            this.executionTimeMs = executionTimeMs;
        }
        
        public List<String> getSequence() {
            return sequence;
        }
        
        public void setSequence(List<String> sequence) {
            this.sequence = sequence;
        }
    }
}
