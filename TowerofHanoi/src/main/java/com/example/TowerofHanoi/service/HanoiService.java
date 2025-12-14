package com.example.TowerofHanoi.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.TowerofHanoi.algorithms.Hanoi3PegIterative;
import com.example.TowerofHanoi.algorithms.Hanoi3PegRecursive;
import com.example.TowerofHanoi.algorithms.Hanoi4PegDynamic;
import com.example.TowerofHanoi.algorithms.Hanoi4PegFrameStewart;
import com.example.TowerofHanoi.dto.HanoiStartResponse;
import com.example.TowerofHanoi.dto.HanoiSubmitRequest;
import com.example.TowerofHanoi.dto.HanoiSubmitResponse;
import com.example.TowerofHanoi.entity.HanoiGameRound;
import com.example.TowerofHanoi.repository.HanoiRepository;

@Service
public class HanoiService {
    
    @Autowired
    private HanoiRepository hanoiRepository;
    
    private final Random random = new Random();
    
    public HanoiStartResponse startNewRound() {
        try {
            int diskCount = 5 + random.nextInt(6); // 5-10 inclusive
            int pegCount = 3; // Default
            
            String roundId = UUID.randomUUID().toString();
            
            return new HanoiStartResponse(roundId, diskCount, pegCount);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error in startNewRound: " + e.getMessage(), e);
        }
    }
    
    public HanoiSubmitResponse submitRound(HanoiSubmitRequest request) {
        try {
            if (request.getRoundId() == null || request.getMoves() == null) {
                return new HanoiSubmitResponse(false, "Invalid request: roundId and moves are required", 0, null);
            }
            
            if (request.getPlayerName() == null || request.getPlayerName().trim().isEmpty()) {
                return new HanoiSubmitResponse(false, "Player name is required", 0, null);
            }
            
            int pegCount = request.getPegCount() != null ? request.getPegCount() : 3;
            
            if (pegCount < 3 || pegCount > 4) {
                return new HanoiSubmitResponse(false, "Peg count must be 3 or 4", 0, null);
            }
            
            int diskCount = request.getDiskCount() != null ? request.getDiskCount() : estimateDiskCount(request.getMoves());
            
            if (diskCount < 5 || diskCount > 10) {
                return new HanoiSubmitResponse(false, "Disk count must be between 5 and 10", 0, null);
            }
            
            boolean isValid = validateMoves(request.getMoves(), diskCount, pegCount);
            
            if (!isValid) {
                return new HanoiSubmitResponse(false, "Invalid move sequence", request.getMoves().size(), null);
            }
            
            Map<String, HanoiSubmitResponse.AlgorithmResult> results = runAllAlgorithms(diskCount);
            
            saveGameRound(request, diskCount, pegCount, isValid, results);
            
            String message = isValid ? "Success! Valid solution." : "Invalid move sequence.";
            return new HanoiSubmitResponse(isValid, message, request.getMoves().size(), results);
            
        } catch (Exception e) {
            return new HanoiSubmitResponse(false, "Error processing submission: " + e.getMessage(), 0, null);
        }
    }
    
    private int estimateDiskCount(List<String> moves) {
        // Estimate disk count (for 3-peg: 2^n - 1 = moves)
        if (moves.isEmpty()) return 5;
        
        int moveCount = moves.size();
        int n = 1;
        while (Math.pow(2, n) - 1 < moveCount && n < 15) {
            n++;
        }
        return Math.max(5, Math.min(10, n));
    }
    
    private boolean validateMoves(List<String> moves, int diskCount, int pegCount) {
        try {
            Map<Character, Stack<Integer>> pegs = new HashMap<>();
            pegs.put('A', new Stack<>());
            pegs.put('B', new Stack<>());
            pegs.put('C', new Stack<>());
            if (pegCount == 4) {
                pegs.put('D', new Stack<>());
            }
            
            for (int i = diskCount; i >= 1; i--) {
                pegs.get('A').push(i);
            }
            
            for (String move : moves) {
                if (!simulateMove(move, pegs)) {
                    return false;
                }
            }
            
            char destination = pegCount == 3 ? 'C' : 'D';
            return pegs.get(destination).size() == diskCount;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    private boolean simulateMove(String move, Map<Character, Stack<Integer>> pegs) {
        try {
            // Parse move (format : "A → B or A->B")
            String[] parts = move.split("→|->|->|\\s+→\\s+");
            if (parts.length != 2) {
                return false;
            }
            
            char from = parts[0].trim().charAt(0);
            char to = parts[1].trim().charAt(0);
            
            if (!pegs.containsKey(from) || !pegs.containsKey(to)) {
                return false;
            }
            
            if (pegs.get(from).isEmpty()) {
                return false;
            }
            
            int disk = pegs.get(from).peek();
            if (!pegs.get(to).isEmpty() && disk > pegs.get(to).peek()) {
                return false;
            }
            
            pegs.get(to).push(pegs.get(from).pop());
            return true;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    private Map<String, HanoiSubmitResponse.AlgorithmResult> runAllAlgorithms(int diskCount) {
        Map<String, HanoiSubmitResponse.AlgorithmResult> results = new HashMap<>();
        
        Hanoi3PegRecursive recursive3Peg = new Hanoi3PegRecursive();
        long startTime = System.nanoTime();
        List<String> recursiveMoves = recursive3Peg.solve(diskCount);
        long endTime = System.nanoTime();
        results.put("recursive3Peg", new HanoiSubmitResponse.AlgorithmResult(
            recursiveMoves.size(), endTime - startTime, recursiveMoves));
        
        Hanoi3PegIterative iterative3Peg = new Hanoi3PegIterative();
        startTime = System.nanoTime();
        List<String> iterativeMoves = iterative3Peg.solve(diskCount);
        endTime = System.nanoTime();
        results.put("iterative3Peg", new HanoiSubmitResponse.AlgorithmResult(
            iterativeMoves.size(), endTime - startTime, iterativeMoves));
        
        Hanoi4PegFrameStewart frameStewart = new Hanoi4PegFrameStewart();
        startTime = System.nanoTime();
        List<String> frameStewartMoves = frameStewart.solve(diskCount);
        endTime = System.nanoTime();
        results.put("frameStewart4Peg", new HanoiSubmitResponse.AlgorithmResult(
            frameStewartMoves.size(), endTime - startTime, frameStewartMoves));
        
        Hanoi4PegDynamic dynamic4Peg = new Hanoi4PegDynamic();
        startTime = System.nanoTime();
        List<String> dynamicMoves = dynamic4Peg.solve(diskCount);
        endTime = System.nanoTime();
        results.put("dynamic4Peg", new HanoiSubmitResponse.AlgorithmResult(
            dynamicMoves.size(), endTime - startTime, dynamicMoves));
        
        return results;
    }
    
    private void saveGameRound(HanoiSubmitRequest request, int diskCount, int pegCount, 
                               boolean isValid, Map<String, HanoiSubmitResponse.AlgorithmResult> results) {
        HanoiGameRound gameRound = new HanoiGameRound();
        gameRound.setRoundId(request.getRoundId());
        gameRound.setPlayerName(request.getPlayerName());
        gameRound.setDiskCount(diskCount);
        gameRound.setPegCount(pegCount);
        gameRound.setUserMoveCount(request.getMoves().size());
        gameRound.setUserMoves(String.join(", ", request.getMoves()));
        gameRound.setIsValid(isValid);
        
        HanoiSubmitResponse.AlgorithmResult recursive = results.get("recursive3Peg");
        gameRound.setRecursive3PegMoves(recursive.getMoveCount());
        gameRound.setRecursive3PegTimeNanos(recursive.getExecutionTimeNanos());
        gameRound.setRecursive3PegSequence(String.join(", ", recursive.getSequence()));
        
        HanoiSubmitResponse.AlgorithmResult iterative = results.get("iterative3Peg");
        gameRound.setIterative3PegMoves(iterative.getMoveCount());
        gameRound.setIterative3PegTimeNanos(iterative.getExecutionTimeNanos());
        gameRound.setIterative3PegSequence(String.join(", ", iterative.getSequence()));
        
        HanoiSubmitResponse.AlgorithmResult frameStewart = results.get("frameStewart4Peg");
        gameRound.setFrameStewart4PegMoves(frameStewart.getMoveCount());
        gameRound.setFrameStewart4PegTimeNanos(frameStewart.getExecutionTimeNanos());
        gameRound.setFrameStewart4PegSequence(String.join(", ", frameStewart.getSequence()));
        
        HanoiSubmitResponse.AlgorithmResult dynamic = results.get("dynamic4Peg");
        gameRound.setDynamic4PegMoves(dynamic.getMoveCount());
        gameRound.setDynamic4PegTimeNanos(dynamic.getExecutionTimeNanos());
        gameRound.setDynamic4PegSequence(String.join(", ", dynamic.getSequence()));
        
        hanoiRepository.save(gameRound);
    }
}
