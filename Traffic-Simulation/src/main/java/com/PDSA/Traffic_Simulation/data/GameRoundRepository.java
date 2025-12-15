package com.PDSA.Traffic_Simulation.data;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GameRoundRepository extends JpaRepository<GameRound, Long> {
    List<GameRound> findByPlayerOrderByCreatedAtDesc(Player player);
    List<GameRound> findAllByOrderByCreatedAtDesc();
}
