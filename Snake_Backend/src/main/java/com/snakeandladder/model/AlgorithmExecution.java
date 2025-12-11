package com.snakeandladder.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AlgorithmExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String algorithmName; // "BFS", "Dijkstra", "A*"
    private long executionTimeNano;
    private int resultMinThrows;
    private int boardSize;
    // Could link to a Game ID if we had one, but strict requirement implies just recording it.
}
