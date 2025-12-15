package com.PDSA.Eight_Queens.data;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "solution_number")
    private int solutionNumber; // 1 to 92

    @Column(name = "board", columnDefinition = "TEXT", unique = true)
    private String board; // JSON format: "[0,4,7,5,2,6,1,3]"

    @Column(name = "computation_type")
    private String computationType; // "SEQUENTIAL" or "THREADED"

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Constructors
    public Answer() {
    }

    public Answer(int solutionNumber, String board, String computationType) {
        this.solutionNumber = solutionNumber;
        this.board = board;
        this.computationType = computationType;
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

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getComputationType() {
        return computationType;
    }

    public void setComputationType(String computationType) {
        this.computationType = computationType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
