package com.example.TowerofHanoi.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "hanoi_game_rounds")
public class HanoiGameRound {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "round_id", nullable = false)
    private String roundId;
    
    @Column(name = "player_name", nullable = false)
    private String playerName;
    
    @Column(name = "disk_count", nullable = false)
    private Integer diskCount;
    
    @Column(name = "peg_count", nullable = false)
    private Integer pegCount;
    
    @Column(name = "user_move_count", nullable = false)
    private Integer userMoveCount;
    
    @Column(name = "user_moves", nullable = false, columnDefinition = "TEXT")
    private String userMoves;
    
    @Column(name = "is_valid", nullable = false)
    private Boolean isValid;
    
    @Column(name = "recursive3peg_moves", nullable = false)
    private Integer recursive3PegMoves;
    
    @Column(name = "recursive3peg_time_nanos", nullable = false)
    private Long recursive3PegTimeNanos;
    
    @Column(name = "iterative3peg_moves", nullable = false)
    private Integer iterative3PegMoves;
    
    @Column(name = "iterative3peg_time_nanos", nullable = false)
    private Long iterative3PegTimeNanos;
    
    @Column(name = "frame_stewart4peg_moves", nullable = false)
    private Integer frameStewart4PegMoves;
    
    @Column(name = "frame_stewart4peg_time_nanos", nullable = false)
    private Long frameStewart4PegTimeNanos;
    
    @Column(name = "dynamic4peg_moves", nullable = false)
    private Integer dynamic4PegMoves;
    
    @Column(name = "dynamic4peg_time_nanos", nullable = false)
    private Long dynamic4PegTimeNanos;
    
    @Column(name = "recursive3peg_sequence", nullable = false, columnDefinition = "TEXT")
    private String recursive3PegSequence;
    
    @Column(name = "iterative3peg_sequence", nullable = false, columnDefinition = "TEXT")
    private String iterative3PegSequence;
    
    @Column(name = "frame_stewart4peg_sequence", nullable = false, columnDefinition = "TEXT")
    private String frameStewart4PegSequence;
    
    @Column(name = "dynamic4peg_sequence", nullable = false, columnDefinition = "TEXT")
    private String dynamic4PegSequence;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    // Constructors
    public HanoiGameRound() {
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getRoundId() {
        return roundId;
    }
    
    public void setRoundId(String roundId) {
        this.roundId = roundId;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    public Integer getDiskCount() {
        return diskCount;
    }
    
    public void setDiskCount(Integer diskCount) {
        this.diskCount = diskCount;
    }
    
    public Integer getPegCount() {
        return pegCount;
    }
    
    public void setPegCount(Integer pegCount) {
        this.pegCount = pegCount;
    }
    
    public Integer getUserMoveCount() {
        return userMoveCount;
    }
    
    public void setUserMoveCount(Integer userMoveCount) {
        this.userMoveCount = userMoveCount;
    }
    
    public String getUserMoves() {
        return userMoves;
    }
    
    public void setUserMoves(String userMoves) {
        this.userMoves = userMoves;
    }
    
    public Boolean getIsValid() {
        return isValid;
    }
    
    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }
    
    public Integer getRecursive3PegMoves() {
        return recursive3PegMoves;
    }
    
    public void setRecursive3PegMoves(Integer recursive3PegMoves) {
        this.recursive3PegMoves = recursive3PegMoves;
    }
    
    public Long getRecursive3PegTimeNanos() {
        return recursive3PegTimeNanos;
    }
    
    public void setRecursive3PegTimeNanos(Long recursive3PegTimeNanos) {
        this.recursive3PegTimeNanos = recursive3PegTimeNanos;
    }
    
    public Integer getIterative3PegMoves() {
        return iterative3PegMoves;
    }
    
    public void setIterative3PegMoves(Integer iterative3PegMoves) {
        this.iterative3PegMoves = iterative3PegMoves;
    }
    
    public Long getIterative3PegTimeNanos() {
        return iterative3PegTimeNanos;
    }
    
    public void setIterative3PegTimeNanos(Long iterative3PegTimeNanos) {
        this.iterative3PegTimeNanos = iterative3PegTimeNanos;
    }
    
    public Integer getFrameStewart4PegMoves() {
        return frameStewart4PegMoves;
    }
    
    public void setFrameStewart4PegMoves(Integer frameStewart4PegMoves) {
        this.frameStewart4PegMoves = frameStewart4PegMoves;
    }
    
    public Long getFrameStewart4PegTimeNanos() {
        return frameStewart4PegTimeNanos;
    }
    
    public void setFrameStewart4PegTimeNanos(Long frameStewart4PegTimeNanos) {
        this.frameStewart4PegTimeNanos = frameStewart4PegTimeNanos;
    }
    
    public Integer getDynamic4PegMoves() {
        return dynamic4PegMoves;
    }
    
    public void setDynamic4PegMoves(Integer dynamic4PegMoves) {
        this.dynamic4PegMoves = dynamic4PegMoves;
    }
    
    public Long getDynamic4PegTimeNanos() {
        return dynamic4PegTimeNanos;
    }
    
    public void setDynamic4PegTimeNanos(Long dynamic4PegTimeNanos) {
        this.dynamic4PegTimeNanos = dynamic4PegTimeNanos;
    }
    
    public String getRecursive3PegSequence() {
        return recursive3PegSequence;
    }
    
    public void setRecursive3PegSequence(String recursive3PegSequence) {
        this.recursive3PegSequence = recursive3PegSequence;
    }
    
    public String getIterative3PegSequence() {
        return iterative3PegSequence;
    }
    
    public void setIterative3PegSequence(String iterative3PegSequence) {
        this.iterative3PegSequence = iterative3PegSequence;
    }
    
    public String getFrameStewart4PegSequence() {
        return frameStewart4PegSequence;
    }
    
    public void setFrameStewart4PegSequence(String frameStewart4PegSequence) {
        this.frameStewart4PegSequence = frameStewart4PegSequence;
    }
    
    public String getDynamic4PegSequence() {
        return dynamic4PegSequence;
    }
    
    public void setDynamic4PegSequence(String dynamic4PegSequence) {
        this.dynamic4PegSequence = dynamic4PegSequence;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
