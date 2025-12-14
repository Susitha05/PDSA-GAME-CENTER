-- Eight Queens Database Schema
-- MySQL Port: 4306
-- Database: eight_queens
CREATE DATABASE IF NOT EXISTS eight_queens;
USE eight_queens;

-- Table for storing player game submissions
CREATE TABLE IF NOT EXISTS games (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    board TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_board (board(255)),
    INDEX idx_name (name),
    INDEX idx_created_at (created_at)
);

-- Table for storing computation results (Sequential vs Threaded)
CREATE TABLE IF NOT EXISTS computation_results (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    computation_type VARCHAR(50) NOT NULL,
    total_solutions INT NOT NULL,
    time_taken_ms BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_computation_type (computation_type),
    INDEX idx_created_at (created_at)
);

-- Table for storing the 92 computed answers/solutions
CREATE TABLE IF NOT EXISTS answers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    solution_number INT NOT NULL,
    board TEXT NOT NULL,
    computation_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_board (board(255)),
    INDEX idx_solution_number (solution_number),
    INDEX idx_computation_type (computation_type)
);

-- Create database user
CREATE USER IF NOT EXISTS 'eq_user'@'%' IDENTIFIED BY 'strong_password';
GRANT ALL PRIVILEGES ON eight_queens.* TO 'eq_user'@'%';
FLUSH PRIVILEGES;
