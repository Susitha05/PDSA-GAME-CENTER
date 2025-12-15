-- Create traffic_simulation Database
CREATE DATABASE IF NOT EXISTS traffic_simulation;
USE traffic_simulation;

-- Create players table
CREATE TABLE IF NOT EXISTS players (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    total_score INT NOT NULL DEFAULT 0,
    games_played INT NOT NULL DEFAULT 0,
    games_won INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_name (name),
    INDEX idx_total_score (total_score)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create game_rounds table
CREATE TABLE IF NOT EXISTS game_rounds (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    player_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    player_answer INT NOT NULL,
    correct_answer INT NOT NULL,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE,
    score INT NOT NULL DEFAULT 0,
    algorithm_execution_time BIGINT NOT NULL DEFAULT 0,
    algorithm_used VARCHAR(50) NOT NULL,
    road_capacities_json LONGTEXT NOT NULL,
    ford_fulkerson_time_ms BIGINT NOT NULL DEFAULT 0,
    ford_fulkerson_time_us BIGINT NOT NULL DEFAULT 0,
    ford_fulkerson_time_ns BIGINT NOT NULL DEFAULT 0,
    edmonds_karp_time_ms BIGINT NOT NULL DEFAULT 0,
    edmonds_karp_time_us BIGINT NOT NULL DEFAULT 0,
    edmonds_karp_time_ns BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP NULL,
    FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE,
    INDEX idx_player_id (player_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create indexes for better query performance
CREATE INDEX idx_game_rounds_player_created ON game_rounds(player_id, created_at);
CREATE INDEX idx_game_rounds_is_correct ON game_rounds(is_correct);
