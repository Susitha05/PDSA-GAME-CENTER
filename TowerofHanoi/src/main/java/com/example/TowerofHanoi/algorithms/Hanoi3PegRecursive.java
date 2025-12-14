package com.example.TowerofHanoi.algorithms;

import java.util.ArrayList;
import java.util.List;

public class Hanoi3PegRecursive {
    private List<String> moves;
    
    public Hanoi3PegRecursive() {
        this.moves = new ArrayList<>();
    }
    
    public List<String> solve(int n) {
        moves.clear();
        solveRecursive(n, 'A', 'C', 'B');
        return new ArrayList<>(moves);
    }
    
    private void solveRecursive(int n, char source, char destination, char auxiliary) {
        if (n == 1) {
            moves.add(source + " → " + destination);
            return;
        }
        
        solveRecursive(n - 1, source, auxiliary, destination);
        
        moves.add(source + " → " + destination);
        
        solveRecursive(n - 1, auxiliary, destination, source);
    }
    
    public int getMoveCount() {
        return moves.size();
    }
}
