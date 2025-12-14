package com.example.tsp.Repository;


import  com.example.tsp.model.UserScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserScoreRepository extends JpaRepository<UserScore, Long> {

    List<UserScore> findByName(String name);

    // Find top scores by accuracy
    List<UserScore> findTop10ByOrderByAccuracyPercentageDesc();

    // Find top scores by shortest distance
    List<UserScore> findTop10ByOrderByTotalDistanceAsc();

    // Custom query to find best times for each algorithm
    @Query("SELECT u FROM UserScore u ORDER BY u.algorithm1Time ASC")
    List<UserScore> findBestAlgorithm1Times();

    @Query("SELECT u FROM UserScore u ORDER BY u.algorithm2Time ASC")
    List<UserScore> findBestAlgorithm2Times();

    @Query("SELECT u FROM UserScore u ORDER BY u.algorithm3Time ASC")
    List<UserScore> findBestAlgorithm3Times();


}

