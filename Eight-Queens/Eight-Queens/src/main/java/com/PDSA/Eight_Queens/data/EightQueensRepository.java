package com.PDSA.Eight_Queens.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EightQueensRepository extends JpaRepository<EightQueens, Long> {

    // Find a solution by board configuration
    Optional<EightQueens> findByBoard(String board);

    // Check if a solution exists
    boolean existsByBoard(String board);

    // Get all unique solutions
    @Query("SELECT DISTINCT e.board FROM EightQueens e")
    List<String> findAllDistinctBoards();

    // Count distinct solutions
    @Query("SELECT COUNT(DISTINCT e.board) FROM EightQueens e")
    long countDistinctBoards();

    // Get all solutions for a specific player
    List<EightQueens> findByName(String name);

    // Get top players by score
    List<EightQueens> findTop20ByOrderByScoreDesc();

    // Get all players ordered by score
    List<EightQueens> findAllByOrderByScoreDesc();
}
