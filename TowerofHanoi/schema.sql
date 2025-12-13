-- Tower of Hanoi Game Database Schema

CREATE DATABASE IF NOT EXISTS hanoi_game;
USE hanoi_game;

-- Table: hanoi_game_rounds
CREATE TABLE hanoi_game_rounds (
    round_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    num_disks INT NOT NULL,
    num_pegs INT NOT NULL,
    user_move_count INT NOT NULL,
    correct_move_count INT NOT NULL,
    is_correct BOOLEAN NOT NULL,
    algorithm_3peg_recursive_time DECIMAL(10, 4),
    algorithm_3peg_iterative_time DECIMAL(10, 4),
    algorithm_4peg_framestewart_time DECIMAL(10, 4),
    algorithm_4peg_dynamic_time DECIMAL(10, 4),
    user_sequence LONGTEXT,
    correct_sequence LONGTEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_created_at (created_at),
    INDEX idx_num_disks (num_disks)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
