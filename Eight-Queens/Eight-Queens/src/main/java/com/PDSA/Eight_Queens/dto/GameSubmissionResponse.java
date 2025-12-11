package com.PDSA.Eight_Queens.dto;

public class GameSubmissionResponse {

    private boolean correct;
    private String message;
    private boolean alreadyFound;
    private int totalFoundSolutions;
    private int totalPossibleSolutions;

    // Constructors
    public GameSubmissionResponse() {
    }

    public GameSubmissionResponse(boolean correct, String message, boolean alreadyFound,
            int totalFoundSolutions, int totalPossibleSolutions) {
        this.correct = correct;
        this.message = message;
        this.alreadyFound = alreadyFound;
        this.totalFoundSolutions = totalFoundSolutions;
        this.totalPossibleSolutions = totalPossibleSolutions;
    }

    // Getters and Setters
    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAlreadyFound() {
        return alreadyFound;
    }

    public void setAlreadyFound(boolean alreadyFound) {
        this.alreadyFound = alreadyFound;
    }

    public int getTotalFoundSolutions() {
        return totalFoundSolutions;
    }

    public void setTotalFoundSolutions(int totalFoundSolutions) {
        this.totalFoundSolutions = totalFoundSolutions;
    }

    public int getTotalPossibleSolutions() {
        return totalPossibleSolutions;
    }

    public void setTotalPossibleSolutions(int totalPossibleSolutions) {
        this.totalPossibleSolutions = totalPossibleSolutions;
    }
}
