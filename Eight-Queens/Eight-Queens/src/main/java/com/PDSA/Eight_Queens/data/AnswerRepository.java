package com.PDSA.Eight_Queens.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    // Find answer by board configuration
    Optional<Answer> findByBoard(String board);

    // Check if an answer exists
    boolean existsByBoard(String board);

    // Get all answers ordered by solution number
    List<Answer> findAllByOrderBySolutionNumberAsc();

    // Count total answers
    long count();

    // Get answers by computation type
    List<Answer> findByComputationType(String computationType);

    // Find by solution number
    Optional<Answer> findBySolutionNumber(int solutionNumber);
}
