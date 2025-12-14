package com.PDSA.Eight_Queens.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class GameSubmissionRequest {

    @NotBlank(message = "Player name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Board configuration is required")
    @Size(min = 8, max = 8, message = "Board must have exactly 8 queens")
    private int[] board;

    @NotNull(message = "Time taken is required")
    private Integer timeTakenSeconds;

    @NotNull(message = "Moves count is required")
    private Integer movesCount;

    // Constructors
    public GameSubmissionRequest() {
    }

    public GameSubmissionRequest(String name, int[] board, Integer timeTakenSeconds, Integer movesCount) {
        this.name = name;
        this.board = board;
        this.timeTakenSeconds = timeTakenSeconds;
        this.movesCount = movesCount;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getBoard() {
        return board;
    }

    public void setBoard(int[] board) {
        this.board = board;
    }

    public Integer getTimeTakenSeconds() {
        return timeTakenSeconds;
    }

    public void setTimeTakenSeconds(Integer timeTakenSeconds) {
        this.timeTakenSeconds = timeTakenSeconds;
    }

    public Integer getMovesCount() {
        return movesCount;
    }

    public void setMovesCount(Integer movesCount) {
        this.movesCount = movesCount;
    }
}
