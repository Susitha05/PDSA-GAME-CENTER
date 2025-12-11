package com.snakeandladder.controller;

import com.snakeandladder.model.AlgorithmExecution;
import com.snakeandladder.model.Board;
import com.snakeandladder.model.PlayerResult;
import com.snakeandladder.repository.AlgorithmExecutionRepository;
import com.snakeandladder.repository.PlayerResultRepository;
import com.snakeandladder.service.AlgorithmService;
import com.snakeandladder.service.GameService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "*") // Allow React frontend
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private AlgorithmService algorithmService;
    
    @Autowired
    private PlayerResultRepository playerResultRepository;
    
    @Autowired
    private AlgorithmExecutionRepository algorithmExecutionRepository;

    // Simple in-memory storage for active games (Board + Correct Answer)
    private final Map<String, GameContext> activeGames = new ConcurrentHashMap<>();

    @Data
    static class GameContext {
        private Board board;
        private int minThrows;
        private Map<String, Long> algorithmTimes = new HashMap<>();
    }

    @PostMapping("/start")
    public ResponseEntity<?> startGame(@RequestParam int n) {
        if (n < 6 || n > 12) {
            return ResponseEntity.badRequest().body("Board size N must be between 6 and 12.");
        }

        Board board = gameService.generateBoard(n);
        
        // Run Algorithms & Measure Time
        long start, end;
        int minThrows = -1;
        Map<String, Long> times = new HashMap<>();

        // BFS
        start = System.nanoTime();
        minThrows = algorithmService.bfs(board);
        end = System.nanoTime();
        times.put("BFS", end - start);
        saveAlgoStats("BFS", end - start, minThrows, n);

        // Dijkstra
        start = System.nanoTime();
        algorithmService.dijkstra(board); // Result should be same
        end = System.nanoTime();
        times.put("Dijkstra", end - start);
        saveAlgoStats("Dijkstra", end - start, minThrows, n);

        // A*
        start = System.nanoTime();
        algorithmService.aStar(board);
        end = System.nanoTime();
        times.put("A", end - start);
        saveAlgoStats("A*", end - start, minThrows, n);

        String gameId = UUID.randomUUID().toString();
        GameContext ctx = new GameContext();
        ctx.setBoard(board);
        ctx.setMinThrows(minThrows);
        ctx.setAlgorithmTimes(times);
        activeGames.put(gameId, ctx);

        // Generate choices (1 correct, 2 random close values)
        List<Integer> choices = generateChoices(minThrows);

        Map<String, Object> response = new HashMap<>();
        response.put("gameId", gameId);
        response.put("board", board);
        response.put("choices", choices);
        // Do not send minThrows to client
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/submit-guess")
    public ResponseEntity<?> submitGuess(@RequestParam String gameId, 
                                         @RequestParam String playerName, 
                                         @RequestParam int guess) {
        GameContext ctx = activeGames.get(gameId);
        if (ctx == null) {
            return ResponseEntity.notFound().build();
        }

        boolean isCorrect = (guess == ctx.getMinThrows());
        
        // Save Result
        if (isCorrect) {
            PlayerResult result = new PlayerResult();
            result.setPlayerName(playerName);
            result.setBoardSize(ctx.getBoard().getSize());
            result.setCorrectMinThrows(ctx.getMinThrows());
            result.setWon(true); // "Correctly identified"
            playerResultRepository.save(result);
        }

        return ResponseEntity.ok(Map.of("correct", isCorrect, "actual", ctx.getMinThrows()));
    }
    
    // Endpoint to validate move (Optional, for "Code used for Logic")
    // Frontend can calculate, but strictly speaking "Application Logic" usually resides on server.
    // We can just trust frontend for gameplay flow or validate each move.
    // For simplicity with React, we'll let Frontend handle gameplay state, 
    // but we can expose a helper.
    
    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        // Aggregate stats essentially
        List<AlgorithmExecution> all = algorithmExecutionRepository.findAll();
        // Group by Algorithm Name
        Map<String, Map<String, Double>> stats = new HashMap<>();
        
        // Calculate Average Time per Algo
        Map<String, List<Long>> times = new HashMap<>();
        for (AlgorithmExecution ae : all) {
            times.computeIfAbsent(ae.getAlgorithmName(), k -> new ArrayList<>()).add(ae.getExecutionTimeNano());
        }
        
        for (Map.Entry<String, List<Long>> entry : times.entrySet()) {
            double avg = entry.getValue().stream().mapToLong(Long::longValue).average().orElse(0.0);
            Map<String, Double> data = new HashMap<>();
            data.put("averageTimeNano", avg);
            data.put("samples", (double) entry.getValue().size());
            stats.put(entry.getKey(), data);
        }
        
        return ResponseEntity.ok(stats);
    }

    // Helper to run 15 rounds simulation for the report
    @PostMapping("/simulate")
    public ResponseEntity<?> runSimulation() {
        algorithmExecutionRepository.deleteAll(); // Clear previous stats for clean report
        List<AlgorithmExecution> results = new ArrayList<>();
        
        for (int i = 0; i < 15; i++) {
            // We need to capture the executions from this call. 
            // Since startGame saves to DB, we can just fetch all after.
            startGame(10); 
        }
        
        return ResponseEntity.ok(algorithmExecutionRepository.findAll());
    }

    private void saveAlgoStats(String name, long time, int result, int n) {
        AlgorithmExecution ae = new AlgorithmExecution();
        ae.setAlgorithmName(name);
        ae.setExecutionTimeNano(time);
        ae.setResultMinThrows(result);
        ae.setBoardSize(n);
        algorithmExecutionRepository.save(ae);
    }

    private List<Integer> generateChoices(int correct) {
        Set<Integer> choices = new HashSet<>();
        choices.add(correct);
        Random r = new Random();
        while (choices.size() < 3) {
            int offset = r.nextInt(5) - 2; // -2 to +2
            int val = correct + offset;
            if (val > 0 && val != correct) {
                choices.add(val);
            } else if (val <= 0) {
                 choices.add(correct + r.nextInt(3) + 1);
            }
        }
        List<Integer> list = new ArrayList<>(choices);
        Collections.shuffle(list);
        return list;
    }
}
