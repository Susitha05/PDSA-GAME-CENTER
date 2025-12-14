package com.example.TowerofHanoi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.TowerofHanoi.entity.HanoiGameRound;

@Repository
public interface HanoiRepository extends JpaRepository<HanoiGameRound, Long> {
    Optional<HanoiGameRound> findByRoundId(String roundId);
}
