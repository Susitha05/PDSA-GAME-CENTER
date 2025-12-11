package com.snakeandladder.model;

import lombok.Data;
import java.util.List;

@Data
public class Board {
    private int size; // N
    private int totalCells; // N*N
    private List<Snake> snakes;
    private List<Ladder> ladders;
    
    // For algorithms, it's useful to have a direct mapping array
    // jump[i] = destination (if snake or ladder) or 0/i
}
