package com.PDSA.Traffic_Simulation.dto;

import java.time.LocalDateTime;

public class PlayerDTO {
    private Long id;
    private String name;
    private Integer totalScore;
    private Integer gamesPlayed;
    private Integer gamesWon;
    private LocalDateTime createdAt;

    public PlayerDTO() {}

    public PlayerDTO(Long id, String name, Integer totalScore, Integer gamesPlayed, Integer gamesWon) {
        this.id = id;
        this.name = name;
        this.totalScore = totalScore;
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(Integer gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public Integer getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(Integer gamesWon) {
        this.gamesWon = gamesWon;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
