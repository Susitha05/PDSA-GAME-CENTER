package com.PDSA.Eight_Queens.dto;

public class ScoreboardEntry {

    private String name;
    private int score;
    private int timeTakenSeconds;
    private int movesCount;
    private String createdAt;
    private int rank;

    // Constructors
    public ScoreboardEntry() {
    }

    public ScoreboardEntry(String name, int score, int timeTakenSeconds, int movesCount, String createdAt, int rank) {
        this.name = name;
        this.score = score;
        this.timeTakenSeconds = timeTakenSeconds;
        this.movesCount = movesCount;
        this.createdAt = createdAt;
        this.rank = rank;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTimeTakenSeconds() {
        return timeTakenSeconds;
    }

    public void setTimeTakenSeconds(int timeTakenSeconds) {
        this.timeTakenSeconds = timeTakenSeconds;
    }

    public int getMovesCount() {
        return movesCount;
    }

    public void setMovesCount(int movesCount) {
        this.movesCount = movesCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
