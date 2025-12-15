package com.PDSA.Traffic_Simulation.service;

import com.PDSA.Traffic_Simulation.data.*;
import com.PDSA.Traffic_Simulation.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Game Logic Service for Traffic Simulation Game
 * Handles game flow, road capacity generation, maximum flow calculation, and scoring
 */
@Service
public class TrafficGameService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRoundRepository gameRoundRepository;

    private static final int MIN_CAPACITY = 5;
    private static final int MAX_CAPACITY = 15;
    private static final int BASE_SCORE = 100;
    private static final int BONUS_SCORE = 50;

    // Traffic network nodes: A=0 (source), B=1, C=2, D=3, E=4, F=5, G=6, H=7, T=8 (sink)
    private static final String[] NODES = {"A", "B", "C", "D", "E", "F", "G", "H", "T"};
    private static final int SOURCE = 0; // Node A
    private static final int SINK = 8;   // Node T

    // Predefined road structure
    private static final int[][] EDGE_DEFINITIONS = {
        {0, 1}, {0, 2}, {0, 3}, // A -> B, C, D
        {1, 4}, {1, 5},         // B -> E, F
        {2, 4}, {2, 5},         // C -> E, F
        {3, 5},                 // D -> F
        {4, 6}, {4, 7},         // E -> G, H
        {5, 7},                 // F -> H
        {6, 8}, {7, 8}          // G -> T, H -> T
    };

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Register or get a player
     */
    public Player registerPlayer(String playerName) throws Exception {
        Optional<Player> existingPlayer = playerRepository.findByName(playerName);
        if (existingPlayer.isPresent()) {
            return existingPlayer.get();
        }
        Player newPlayer = new Player(playerName);
        return playerRepository.save(newPlayer);
    }

    /**
     * Start a new game round
     */
    public GameStartResponse startGame(Long playerId) throws Exception {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new Exception("Player not found"));

        // Generate random capacities for roads
        int[][] capacityMatrix = generateCapacityMatrix();
        List<RoadCapacity> roadCapacities = matrixToRoadList(capacityMatrix);

        // Create game round record
        GameRound gameRound = new GameRound();
        gameRound.setPlayer(player);
        gameRound.setStatus("ACTIVE");
        gameRound.setRoadCapacitiesJson(objectMapper.writeValueAsString(roadCapacities));

        GameRound savedRound = gameRoundRepository.save(gameRound);

        return new GameStartResponse(
            savedRound.getId(),
            roadCapacities,
            generateNetworkGraphJSON()
        );
    }

    /**
     * Submit game answer and calculate result
     */
    public GameResultResponse submitAnswer(GameSubmissionRequest request) throws Exception {
        System.out.println("\n========== GAME SUBMISSION STARTED ==========");
        
        GameRound gameRound = gameRoundRepository.findById(request.getRoundId())
                .orElseThrow(() -> new Exception("Game round not found"));

        if (!"ACTIVE".equals(gameRound.getStatus())) {
            throw new Exception("Game round is not active");
        }

        // Parse road capacities
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> roadList = objectMapper.readValue(
            gameRound.getRoadCapacitiesJson(), List.class);
        int[][] capacityMatrix = roadListToMatrix(roadList);

        System.out.println("[INFO] Capacity matrix created. Running algorithms...");

        // Run Ford-Fulkerson Algorithm
        System.out.println("[INFO] Starting Ford-Fulkerson Algorithm...");
        FordFulkersonAlgorithm ff = new FordFulkersonAlgorithm(capacityMatrix, SOURCE, SINK);
        int ffResult = ff.findMaxFlow();
        long ffTimeNs = ff.getExecutionTimeNanos();
        long ffTimeUs = ff.getExecutionTimeMicros();
        long ffTimeMs = ff.getExecutionTimeMillis();
        System.out.println("[FF Result] Max Flow: " + ffResult + " | Time: " + ffTimeNs + "ns (" + ffTimeUs + "µs, " + ffTimeMs + "ms)");

        // Run Edmonds-Karp (Dinic's) Algorithm
        System.out.println("[INFO] Starting Edmonds-Karp/Dinic's Algorithm...");
        DinicsAlgorithm dinic = new DinicsAlgorithm(capacityMatrix, SOURCE, SINK);
        int ekResult = dinic.findMaxFlow();
        long ekTimeNs = dinic.getExecutionTimeNanos();
        long ekTimeUs = dinic.getExecutionTimeMicros();
        long ekTimeMs = dinic.getExecutionTimeMillis();
        System.out.println("[EK Result] Max Flow: " + ekResult + " | Time: " + ekTimeNs + "ns (" + ekTimeUs + "µs, " + ekTimeMs + "ms)");

        // Verify both algorithms produce the same result
        int correctAnswer = ffResult;
        if (ffResult != ekResult) {
            System.out.println("[WARNING] Algorithm results differ! FF=" + ffResult + ", EK=" + ekResult);
            throw new Exception("Algorithm results differ: Ford-Fulkerson=" + ffResult + ", Edmonds-Karp=" + ekResult);
        }
        System.out.println("[SUCCESS] Both algorithms produced identical result: " + correctAnswer);

        // Determine if answer is correct
        boolean isCorrect = request.getPlayerAnswer().equals(correctAnswer);
        int score = isCorrect ? BASE_SCORE + BONUS_SCORE : BASE_SCORE / 2;
        System.out.println("[Result] Player Answer: " + request.getPlayerAnswer() + " | Correct: " + isCorrect + " | Score: " + score);

        // Update game round
        gameRound.setPlayerAnswer(request.getPlayerAnswer());
        gameRound.setCorrectAnswer(correctAnswer);
        gameRound.setIsCorrect(isCorrect);
        gameRound.setScore(score);
        gameRound.setAlgorithmUsed("BOTH");
        gameRound.setAlgorithmExecutionTime(ffTimeMs);
        
        // Store timing data for both algorithms in all units (ns/µs/ms)
        gameRound.setFordFulkersonTimeNs(ffTimeNs);
        gameRound.setFordFulkersonTimeUs(ffTimeUs);
        gameRound.setFordFulkersonTimeMs(ffTimeMs);
        gameRound.setEdmondsKarpTimeNs(ekTimeNs);
        gameRound.setEdmondsKarpTimeUs(ekTimeUs);
        gameRound.setEdmondsKarpTimeMs(ekTimeMs);
        System.out.println("[PERSIST] Storing timing data: FF=" + ffTimeNs + "ns, EK=" + ekTimeNs + "ns");
        
        gameRound.setStatus("COMPLETED");
        gameRound.setCompletedAt(LocalDateTime.now());

        gameRoundRepository.save(gameRound);

        // Update player statistics
        Player player = gameRound.getPlayer();
        player.setGamesPlayed(player.getGamesPlayed() + 1);
        if (isCorrect) {
            player.setGamesWon(player.getGamesWon() + 1);
        }
        player.setTotalScore(player.getTotalScore() + score);
        player.setUpdatedAt(LocalDateTime.now());
        playerRepository.save(player);

        String message = isCorrect 
            ? "Correct! Maximum flow is " + correctAnswer + " vehicles/minute"
            : "Incorrect. The correct maximum flow is " + correctAnswer + " vehicles/minute";

        // Build and return response with all timing information
        GameResultResponse response = new GameResultResponse();
        response.setPlayerAnswer(request.getPlayerAnswer());
        response.setIsCorrect(isCorrect);
        response.setCorrectAnswer(correctAnswer);
        response.setScore(score);
        
        // Set Ford-Fulkerson times
        response.setFordFulkersonTimeMs(ffTimeMs);
        response.setFordFulkersonTimeUs(ffTimeUs);
        response.setFordFulkersonTimeNs(ffTimeNs);
        
        // Set Edmonds-Karp times
        response.setEdmondsKarpTimeMs(ekTimeMs);
        response.setEdmondsKarpTimeUs(ekTimeUs);
        response.setEdmondsKarpTimeNs(ekTimeNs);
        
        // Legacy field for backward compatibility
        response.setAlgorithmExecutionTime(ffTimeMs);
        
        response.setMessage(message);

        System.out.println("[RESPONSE] Sending back: FF=" + ffTimeMs + "ms (" + ffTimeUs + "µs), EK=" + ekTimeMs + "ms (" + ekTimeUs + "µs)");
        System.out.println("========== GAME SUBMISSION COMPLETED ==========\n");

        return response;
    }

    /**
     * Get player's game history
     */
    public List<GameRound> getPlayerHistory(Long playerId) throws Exception {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new Exception("Player not found"));
        return gameRoundRepository.findByPlayerOrderByCreatedAtDesc(player);
    }

    /**
     * Get leaderboard
     */
    public List<PlayerDTO> getLeaderboard() {
        List<Player> players = playerRepository.findAllByOrderByTotalScoreDescGamesWonDesc();
        List<PlayerDTO> dtos = new ArrayList<>();
        for (Player player : players) {
            dtos.add(new PlayerDTO(
                player.getId(),
                player.getName(),
                player.getTotalScore(),
                player.getGamesPlayed(),
                player.getGamesWon()
            ));
        }
        return dtos;
    }

    /**
     * Generate random capacity matrix
     */
    private int[][] generateCapacityMatrix() {
        int[][] matrix = new int[NODES.length][NODES.length];
        Random rand = new Random();

        for (int[] edge : EDGE_DEFINITIONS) {
            int from = edge[0];
            int to = edge[1];
            int capacity = MIN_CAPACITY + rand.nextInt(MAX_CAPACITY - MIN_CAPACITY + 1);
            matrix[from][to] = capacity;
        }

        return matrix;
    }

    /**
     * Convert capacity matrix to road list
     */
    private List<RoadCapacity> matrixToRoadList(int[][] matrix) {
        List<RoadCapacity> roads = new ArrayList<>();

        for (int[] edge : EDGE_DEFINITIONS) {
            int from = edge[0];
            int to = edge[1];
            roads.add(new RoadCapacity(
                NODES[from],
                NODES[to],
                matrix[from][to]
            ));
        }

        return roads;
    }

    /**
     * Convert road list back to capacity matrix
     */
    private int[][] roadListToMatrix(List<Map<String, Object>> roadList) {
        int[][] matrix = new int[NODES.length][NODES.length];

        for (Map<String, Object> road : roadList) {
            String from = (String) road.get("from");
            String to = (String) road.get("to");
            Integer capacity = ((Number) road.get("capacity")).intValue();

            int fromIdx = Arrays.asList(NODES).indexOf(from);
            int toIdx = Arrays.asList(NODES).indexOf(to);

            if (fromIdx >= 0 && toIdx >= 0) {
                matrix[fromIdx][toIdx] = capacity;
            }
        }

        return matrix;
    }

    /**
     * Generate network graph JSON for visualization
     */
    private String generateNetworkGraphJSON() {
        String json = "{" +
            "\"nodes\": [" +
            "{\"id\": \"A\", \"label\": \"A (Source)\", \"x\": 50, \"y\": 50}," +
            "{\"id\": \"B\", \"label\": \"B\", \"x\": 150, \"y\": 50}," +
            "{\"id\": \"C\", \"label\": \"C\", \"x\": 250, \"y\": 50}," +
            "{\"id\": \"D\", \"label\": \"D\", \"x\": 350, \"y\": 50}," +
            "{\"id\": \"E\", \"label\": \"E\", \"x\": 150, \"y\": 150}," +
            "{\"id\": \"F\", \"label\": \"F\", \"x\": 250, \"y\": 150}," +
            "{\"id\": \"G\", \"label\": \"G\", \"x\": 150, \"y\": 250}," +
            "{\"id\": \"H\", \"label\": \"H\", \"x\": 250, \"y\": 250}," +
            "{\"id\": \"T\", \"label\": \"T (Sink)\", \"x\": 200, \"y\": 350}" +
            "]," +
            "\"edges\": [" +
            "{\"from\": \"A\", \"to\": \"B\"}," +
            "{\"from\": \"A\", \"to\": \"C\"}," +
            "{\"from\": \"A\", \"to\": \"D\"}," +
            "{\"from\": \"B\", \"to\": \"E\"}," +
            "{\"from\": \"B\", \"to\": \"F\"}," +
            "{\"from\": \"C\", \"to\": \"E\"}," +
            "{\"from\": \"C\", \"to\": \"F\"}," +
            "{\"from\": \"D\", \"to\": \"F\"}," +
            "{\"from\": \"E\", \"to\": \"G\"}," +
            "{\"from\": \"E\", \"to\": \"H\"}," +
            "{\"from\": \"F\", \"to\": \"H\"}," +
            "{\"from\": \"G\", \"to\": \"T\"}," +
            "{\"from\": \"H\", \"to\": \"T\"}" +
            "]" +
            "}";
        return json;
    }
}
