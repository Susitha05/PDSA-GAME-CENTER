package com.example.TowerofHanoi.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hanoi4PegDynamic {
    private List<String> moves;
    private Map<String, Integer> dpTable;
    private Map<String, Integer> optimalKTable;
    
    public Hanoi4PegDynamic() {
        this.moves = new ArrayList<>();
        this.dpTable = new HashMap<>();
        this.optimalKTable = new HashMap<>();
    }
    
    public List<String> solve(int n) {
        moves.clear();
        dpTable.clear();
        optimalKTable.clear();
        
        computeOptimalMoves(n);
        
        solveDynamic(n, 'A', 'D', 'B', 'C');
        return new ArrayList<>(moves);
    }
    
    private void computeOptimalMoves(int maxN) {
        // Base cases
        dpTable.put("0", 0);
        dpTable.put("1", 1);
        
        for (int n = 2; n <= maxN; n++) {
            int minMoves = Integer.MAX_VALUE;
            int bestK = 1;
            
            for (int k = 1; k < n; k++) {
                int movesForK = 2 * getMoves(k) + ((int) Math.pow(2, n - k) - 1);
                if (movesForK < minMoves) {
                    minMoves = movesForK;
                    bestK = k;
                }
            }
            
            dpTable.put(String.valueOf(n), minMoves);
            optimalKTable.put(String.valueOf(n), bestK);
        }
    }
    
    private int getMoves(int n) {
        String key = String.valueOf(n);
        return dpTable.getOrDefault(key, Integer.MAX_VALUE);
    }
    
    private int getOptimalK(int n) {
        String key = String.valueOf(n);
        return optimalKTable.getOrDefault(key, 1);
    }
    
    private void solveDynamic(int n, char source, char destination, char aux1, char aux2) {
        if (n == 0) {
            return;
        }
        
        if (n == 1) {
            moves.add(source + " → " + destination);
            return;
        }
        
        int k = getOptimalK(n);
        
        solveDynamic(k, source, aux1, aux2, destination);
        
        solve3Peg(n - k, source, destination, aux2);
        solveDynamic(k, aux1, destination, source, aux2);
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
