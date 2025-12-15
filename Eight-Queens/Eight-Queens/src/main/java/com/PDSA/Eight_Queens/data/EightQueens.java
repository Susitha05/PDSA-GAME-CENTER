package com.PDSA.Eight_Queens.data;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "games")
public class EightQueens {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "board", columnDefinition = "TEXT")
    private String board; // JSON format: "[0,4,7,5,2,6,1,3]"

    @Column(name = "time_taken_seconds")
    private int timeTakenSeconds; // Time taken to solve in seconds

    @Column(name = "moves_count")
    private int movesCount; // Number of moves made

    @Column(name = "score")
    private int score; // Score based on time and moves

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Constructors
    public EightQueens() {
    }

    public EightQueens(String name, String board) {
        this.name = name;
        this.board = board;
    }

    public EightQueens(String name, String board, int timeTakenSeconds, int movesCount, int score) {
        this.name = name;
        this.board = board;
        this.timeTakenSeconds = timeTakenSeconds;
        this.movesCount = movesCount;
        this.score = score;
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

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
