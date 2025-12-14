-- Tower of Hanoi Game Database Schema

CREATE DATABASE IF NOT EXISTS hanoi_game;
USE hanoi_game;

-- Table: hanoi_game_rounds
CREATE TABLE hanoi_game_rounds (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    round_id VARCHAR(255) NOT NULL UNIQUE,
    player_name VARCHAR(255) NOT NULL,
    disk_count INT NOT NULL,
    peg_count INT NOT NULL,
    user_move_count INT NOT NULL,
    is_valid BOOLEAN NOT NULL,
    user_moves TEXT NOT NULL,
    
    -- 3-Peg Recursive Algorithm
    recursive_3peg_moves INT NOT NULL,
    recursive_3peg_time_nanos BIGINT NOT NULL,
    recursive_3peg_sequence TEXT NOT NULL,
    
    -- 3-Peg Iterative Algorithm
    iterative_3peg_moves INT NOT NULL,
    iterative_3peg_time_nanos BIGINT NOT NULL,
    iterative_3peg_sequence TEXT NOT NULL,
    
    -- 4-Peg Frame-Stewart Algorithm
    frame_stewart_4peg_moves INT NOT NULL,
    frame_stewart_4peg_time_nanos BIGINT NOT NULL,
    frame_stewart_4peg_sequence TEXT NOT NULL,
    
    -- 4-Peg Dynamic Programming Algorithm
    dynamic_4peg_moves INT NOT NULL,
    dynamic_4peg_time_nanos BIGINT NOT NULL,
    dynamic_4peg_sequence TEXT NOT NULL,
    
    timestamp DATETIME NOT NULL,
    
    INDEX idx_player_name (player_name),
    INDEX idx_round_id (round_id),
    INDEX idx_timestamp (timestamp),
    INDEX idx_disk_count (disk_count)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
