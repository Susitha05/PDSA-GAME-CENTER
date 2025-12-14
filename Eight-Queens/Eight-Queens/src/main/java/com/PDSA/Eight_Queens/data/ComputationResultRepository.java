package com.PDSA.Eight_Queens.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComputationResultRepository extends JpaRepository<ComputationResult, Long> {

    List<ComputationResult> findByComputationType(String computationType);

    ComputationResult findTopByComputationTypeOrderByCreatedAtDesc(String computationType);
}
