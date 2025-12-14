-- Migration to add scoring features to games table
-- Run this script on your eight_queens database

USE eight_queens;

-- Add new columns to games table
ALTER TABLE games 
ADD COLUMN IF NOT EXISTS time_taken_seconds INT DEFAULT 0 COMMENT 'Time taken to solve in seconds',
ADD COLUMN IF NOT EXISTS moves_count INT DEFAULT 0 COMMENT 'Number of moves made',
ADD COLUMN IF NOT EXISTS score INT DEFAULT 0 COMMENT 'Score based on time and moves (higher is better)';

-- Create index on score for faster scoreboard queries
CREATE INDEX IF NOT EXISTS idx_games_score ON games(score DESC);

-- Verify the changes
DESCRIBE games;

SELECT 'Migration completed successfully!' AS Status;
