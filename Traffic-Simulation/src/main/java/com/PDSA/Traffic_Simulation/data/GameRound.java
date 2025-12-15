package com.PDSA.Traffic_Simulation.data;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "game_rounds")
public class GameRound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(nullable = false)
    private String status; // ACTIVE, COMPLETED, LOST

    @Column(name = "player_answer", nullable = true)
    private Integer playerAnswer;

    @Column(name = "correct_answer", nullable = true)
    private Integer correctAnswer;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect = false;

    @Column(name = "score", nullable = false)
    private Integer score = 0;

    @Column(name = "algorithm_execution_time", nullable = false)
    private Long algorithmExecutionTime = 0L; // in milliseconds (legacy field)

    @Column(name = "ford_fulkerson_time_ms", nullable = true)
    private Long fordFulkersonTimeMs = 0L; // Ford-Fulkerson execution time in milliseconds

    @Column(name = "ford_fulkerson_time_us", nullable = true)
    private Long fordFulkersonTimeUs = 0L; // Ford-Fulkerson execution time in microseconds

    @Column(name = "ford_fulkerson_time_ns", nullable = true)
    private Long fordFulkersonTimeNs = 0L; // Ford-Fulkerson execution time in nanoseconds

    @Column(name = "edmonds_karp_time_ms", nullable = true)
    private Long edmondsKarpTimeMs = 0L; // Edmonds-Karp execution time in milliseconds

    @Column(name = "edmonds_karp_time_us", nullable = true)
    private Long edmondsKarpTimeUs = 0L; // Edmonds-Karp execution time in microseconds

    @Column(name = "edmonds_karp_time_ns", nullable = true)
    private Long edmondsKarpTimeNs = 0L; // Edmonds-Karp execution time in nanoseconds

    @Column(name = "algorithm_used", nullable = true)
    private String algorithmUsed; // FORD_FULKERSON or DINIC

    @Column(name = "road_capacities_json", nullable = false, columnDefinition = "LONGTEXT")
    private String roadCapacitiesJson; // JSON representation of road capacities

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime completedAt;

    // Constructors
    public GameRound() {
    }

    public GameRound(Player player, Integer playerAnswer, Integer correctAnswer) {
        this.player = player;
        this.playerAnswer = playerAnswer;
        this.correctAnswer = correctAnswer;
        this.isCorrect = playerAnswer.equals(correctAnswer);
        this.status = "COMPLETED";
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPlayerAnswer() {
        return playerAnswer;
    }

    public void setPlayerAnswer(Integer playerAnswer) {
        this.playerAnswer = playerAnswer;
    }

    public Integer getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Integer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Long getAlgorithmExecutionTime() {
        return algorithmExecutionTime;
    }

    public void setAlgorithmExecutionTime(Long algorithmExecutionTime) {
        this.algorithmExecutionTime = algorithmExecutionTime;
    }

    public String getAlgorithmUsed() {
        return algorithmUsed;
    }

    public void setAlgorithmUsed(String algorithmUsed) {
        this.algorithmUsed = algorithmUsed;
    }

    public String getRoadCapacitiesJson() {
        return roadCapacitiesJson;
    }

    public void setRoadCapacitiesJson(String roadCapacitiesJson) {
        this.roadCapacitiesJson = roadCapacitiesJson;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public Long getFordFulkersonTimeMs() {
        return fordFulkersonTimeMs;
    }

    public void setFordFulkersonTimeMs(Long fordFulkersonTimeMs) {
        this.fordFulkersonTimeMs = fordFulkersonTimeMs;
    }

    public Long getFordFulkersonTimeUs() {
        return fordFulkersonTimeUs;
    }

    public void setFordFulkersonTimeUs(Long fordFulkersonTimeUs) {
        this.fordFulkersonTimeUs = fordFulkersonTimeUs;
    }

    public Long getFordFulkersonTimeNs() {
        return fordFulkersonTimeNs;
    }

    public void setFordFulkersonTimeNs(Long fordFulkersonTimeNs) {
        this.fordFulkersonTimeNs = fordFulkersonTimeNs;
    }

    public Long getEdmondsKarpTimeMs() {
        return edmondsKarpTimeMs;
    }

    public void setEdmondsKarpTimeMs(Long edmondsKarpTimeMs) {
        this.edmondsKarpTimeMs = edmondsKarpTimeMs;
    }

    public Long getEdmondsKarpTimeUs() {
        return edmondsKarpTimeUs;
    }

    public void setEdmondsKarpTimeUs(Long edmondsKarpTimeUs) {
        this.edmondsKarpTimeUs = edmondsKarpTimeUs;
    }

    public Long getEdmondsKarpTimeNs() {
        return edmondsKarpTimeNs;
    }

    public void setEdmondsKarpTimeNs(Long edmondsKarpTimeNs) {
        this.edmondsKarpTimeNs = edmondsKarpTimeNs;
    }
}
