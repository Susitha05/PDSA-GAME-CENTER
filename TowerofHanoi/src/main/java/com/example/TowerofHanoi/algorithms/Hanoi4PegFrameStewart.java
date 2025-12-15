package com.example.TowerofHanoi.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hanoi4PegFrameStewart {
    private List<String> moves;
    private Map<String, Integer> memoization;
    
    public Hanoi4PegFrameStewart() {
        this.moves = new ArrayList<>();
        this.memoization = new HashMap<>();
    }
    
    public List<String> solve(int n) {
        moves.clear();
        memoization.clear();
        solveFrameStewart(n, 'A', 'D', 'B', 'C');
        return new ArrayList<>(moves);
    }
    
    private void solveFrameStewart(int n, char source, char destination, char aux1, char aux2) {
        if (n == 0) {
            return;
        }
        
        if (n == 1) {
            moves.add(source + " → " + destination);
            return;
        }
        
        int k = findOptimalK(n);
        
        solveFrameStewart(k, source, aux1, aux2, destination);
        
        solve3Peg(n - k, source, destination, aux2);
        
        solveFrameStewart(k, aux1, destination, source, aux2);
    }
    
    private int findOptimalK(int n) {
        int minMoves = Integer.MAX_VALUE;
        int optimalK = 1;
        
        for (int k = 1; k < n; k++) {
            int moves = 2 * getMinMoves4Peg(k) + getMinMoves3Peg(n - k);
            if (moves < minMoves) {
                minMoves = moves;
                optimalK = k;
            }
        }
        
        return optimalK;
    }
    
    private int getMinMoves4Peg(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        
        String key = "4p_" + n;
        if (memoization.containsKey(key)) {
            return memoization.get(key);
        }
        
        int minMoves = Integer.MAX_VALUE;
        for (int k = 1; k < n; k++) {
            int moves = 2 * getMinMoves4Peg(k) + getMinMoves3Peg(n - k);
            minMoves = Math.min(minMoves, moves);
        }
        
        memoization.put(key, minMoves);
        return minMoves;
    }
    
    private int getMinMoves3Peg(int n) {
        return (int) Math.pow(2, n) - 1;
    }
    
    private void solve3Peg(int n, char source, char destination, char auxiliary) {
        if (n == 0) {
            return;
        }
        
        if (n == 1) {
            moves.add(source + " → " + destination);
            return;
        }
        
        solve3Peg(n - 1, source, auxiliary, destination);
        moves.add(source + " → " + destination);
        solve3Peg(n - 1, auxiliary, destination, source);
    }
    
    public int getMoveCount() {
        return moves.size();
    }
}
