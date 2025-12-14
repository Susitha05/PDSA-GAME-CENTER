package com.snakeandladder.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class PlayerResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String playerName;
    private int boardSize;
    private int correctMinThrows; // The correct answer they identified
    private LocalDateTime timestamp = LocalDateTime.now();
    private boolean won; // true if they guessed correctly? or if they played and won?
                         // "When a game player correctly identifies an answer..."
}
