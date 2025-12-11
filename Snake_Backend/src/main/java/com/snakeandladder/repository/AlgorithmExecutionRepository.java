package com.snakeandladder.repository;

import com.snakeandladder.model.AlgorithmExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlgorithmExecutionRepository extends JpaRepository<AlgorithmExecution, Long> {
}
