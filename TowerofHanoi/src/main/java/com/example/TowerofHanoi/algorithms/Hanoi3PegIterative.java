package com.example.TowerofHanoi.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Hanoi3PegIterative {
    private List<String> moves;
    
    public Hanoi3PegIterative() {
        this.moves = new ArrayList<>();
    }
    
    public List<String> solve(int n) {
        moves.clear();
        
        Stack<Integer>[] pegs = new Stack[3];
        pegs[0] = new Stack<>();
        pegs[1] = new Stack<>();
        pegs[2] = new Stack<>();
        
        for (int i = n; i >= 1; i--) {
            pegs[0].push(i);
        }
        
        int totalMoves = (int) Math.pow(2, n) - 1;
        char[] pegNames = {'A', 'B', 'C'};
        
        int source = 0;
        int destination = 2;
        int auxiliary = 1;
        
        if (n % 2 == 0) {
            int temp = destination;
            destination = auxiliary;
            auxiliary = temp;
        }
        
        for (int i = 1; i <= totalMoves; i++) {
            if (i % 3 == 1) {
                moveDisk(pegs, pegNames, source, destination);
            } else if (i % 3 == 2) {
                moveDisk(pegs, pegNames, source, auxiliary);
            } else {
                moveDisk(pegs, pegNames, auxiliary, destination);
            }
        }
        
        return new ArrayList<>(moves);
    }
    
    private void moveDisk(Stack<Integer>[] pegs, char[] pegNames, int from, int to) {
        if (pegs[from].isEmpty() && pegs[to].isEmpty()) {
            return;
        }
        
        if (pegs[from].isEmpty()) {
            pegs[from].push(pegs[to].pop());
            moves.add(pegNames[to] + " → " + pegNames[from]);
        } else if (pegs[to].isEmpty()) {
            pegs[to].push(pegs[from].pop());
            moves.add(pegNames[from] + " → " + pegNames[to]);
        } else if (pegs[from].peek() < pegs[to].peek()) {
            pegs[to].push(pegs[from].pop());
            moves.add(pegNames[from] + " → " + pegNames[to]);
        } else {
            pegs[from].push(pegs[to].pop());
            moves.add(pegNames[to] + " → " + pegNames[from]);
        }
    }
    
    public int getMoveCount() {
        return moves.size();
    }
}
