package com.PDSA.Eight_Queens.data;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "computation_results")
public class ComputationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "computation_type")
    private String computationType; // "SEQUENTIAL" or "THREADED"

    @Column(name = "total_solutions")
    private int totalSolutions;

    @Column(name = "time_taken_ms")
    private long timeTakenMs;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Constructors
    public ComputationResult() {
    }

    public ComputationResult(String computationType, int totalSolutions, long timeTakenMs) {
        this.computationType = computationType;
        this.totalSolutions = totalSolutions;
        this.timeTakenMs = timeTakenMs;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComputationType() {
        return computationType;
    }

    public void setComputationType(String computationType) {
        this.computationType = computationType;
    }

    public int getTotalSolutions() {
        return totalSolutions;
    }

    public void setTotalSolutions(int totalSolutions) {
        this.totalSolutions = totalSolutions;
    }

    public long getTimeTakenMs() {
        return timeTakenMs;
    }

    public void setTimeTakenMs(long timeTakenMs) {
        this.timeTakenMs = timeTakenMs;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
