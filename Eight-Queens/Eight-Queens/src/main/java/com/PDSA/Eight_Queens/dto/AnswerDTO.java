package com.PDSA.Eight_Queens.dto;

public class AnswerDTO {

    private Long id;
    private int solutionNumber;
    private int[] board;
    private String computationType;
    private String createdAt;

    // Constructors
    public AnswerDTO() {
    }

    public AnswerDTO(Long id, int solutionNumber, int[] board, String computationType, String createdAt) {
        this.id = id;
        this.solutionNumber = solutionNumber;
        this.board = board;
        this.computationType = computationType;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSolutionNumber() {
        return solutionNumber;
    }

    public void setSolutionNumber(int solutionNumber) {
        this.solutionNumber = solutionNumber;
    }

    public int[] getBoard() {
        return board;
    }

    public void setBoard(int[] board) {
        this.board = board;
    }

    public String getComputationType() {
        return computationType;
    }

    public void setComputationType(String computationType) {
        this.computationType = computationType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
