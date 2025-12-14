package com.example.tsp.controller;

import com.example.tsp.Repository.UserScoreRepository;
import com.example.tsp.model.UserScore;
import com.example.tsp.model.city;
import com.example.tsp.model.Tsprsponse;
import com.example.tsp.service.TspService;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@RestController
@RequestMapping("/tsp")
@CrossOrigin(origins = "*")
public class TspController {

    private final TspService tspService;
    private final UserScoreRepository userScoreRepository;
    private final ObjectMapper objectMapper;

    public TspController(TspService tspService, UserScoreRepository userScoreRepository) {
        this.tspService = tspService;
        this.userScoreRepository = userScoreRepository;
        this.objectMapper = new ObjectMapper();
    }

    @GetMapping("/new")
    public Tsprsponse newGame() {
        List<city> points = new ArrayList<>();
        Random random = new Random();

        // Generate 8-10 random cities with coordinates
        int numCities = 8 + random.nextInt(3); // 8-10 cities
        for (int i = 0; i < numCities; i++) {
            points.add(new city(random.nextDouble() * 80 + 10, random.nextDouble() * 80 + 10));
        }

        // Randomly select home city index
        int homeCityIndex = random.nextInt(numCities);

        Tsprsponse res = new Tsprsponse();
        res.points = points;
        res.homeCityIndex = homeCityIndex;
        return res;
    }

    @PostMapping("/solve")
    public Tsprsponse solveWithAllAlgorithms(@RequestBody Map<String, Object> request) {
        List<Map<String, Double>> cityData = (List<Map<String, Double>>) request.get("cities");
        List<Integer> selectedIndices = (List<Integer>) request.get("selectedIndices");
        Integer homeIndex = (Integer) request.get("homeIndex");

        List<city> allCities = new ArrayList<>();
        for (Map<String, Double> c : cityData) {
            allCities.add(new city(c.get("x"), c.get("y")));
        }

        // Get only selected cities
        List<city> selectedCities = new ArrayList<>();
        for (int idx : selectedIndices) {
            selectedCities.add(allCities.get(idx));
        }

        Tsprsponse res = new Tsprsponse();
        res.points = selectedCities;

        // Algorithm 1: Nearest Neighbor (Greedy)
        long start1 = System.nanoTime();
        List<Integer> path1 = tspService.nearestNeighbor(selectedCities, 0);
        long end1 = System.nanoTime();
        double distance1 = tspService.pathDistance(selectedCities, path1);
        res.algorithm1Time = (end1 - start1) / 1_000_000.0; // Convert to ms
        res.algorithm1Path = path1;
        res.algorithm1Distance = distance1;

        // Algorithm 2: Dynamic Programming
        long start2 = System.nanoTime();
        List<Integer> path2 = tspService.dynamicProgramming(selectedCities, 0);
        long end2 = System.nanoTime();
        double distance2 = tspService.pathDistance(selectedCities, path2);
        res.algorithm2Time = (end2 - start2) / 1_000_000.0;
        res.algorithm2Path = path2;
        res.algorithm2Distance = distance2;

        // Algorithm 3: Genetic Algorithm
        long start3 = System.nanoTime();
        List<Integer> path3 = tspService.geneticAlgorithm(selectedCities, 0);
        long end3 = System.nanoTime();
        double distance3 = tspService.pathDistance(selectedCities, path3);
        res.algorithm3Time = (end3 - start3) / 1_000_000.0;
        res.algorithm3Path = path3;
        res.algorithm3Distance = distance3;

        // Find best solution
        if (distance1 <= distance2 && distance1 <= distance3) {
            res.shortestPath = path1;
            res.distance = distance1;
            res.bestAlgorithm = "Nearest Neighbor";
        } else if (distance2 <= distance1 && distance2 <= distance3) {
            res.shortestPath = path2;
            res.distance = distance2;
            res.bestAlgorithm = "Dynamic Programming";
        } else {
            res.shortestPath = path3;
            res.distance = distance3;
            res.bestAlgorithm = "Genetic Algorithm";
        }

        return res;
    }

    @PostMapping("/check")
    public Map<String, Object> checkUserPath(@RequestBody Map<String, Object> req) {
        try {
            String userName = (String) req.get("user_Name");
            List<Map<String, Double>> cityData = (List<Map<String, Double>>) req.get("dots");
            List<Integer> userPath = (List<Integer>) req.get("userPath");
            List<Integer> selectedIndices = (List<Integer>) req.get("selectedIndices");
            Integer homeCityIndex = (Integer) req.get("homeCityIndex");
            String homeCityName = (String) req.get("homeCityName");

            List<city> cities = new ArrayList<>();
            for (Map<String, Double> c : cityData) {
                cities.add(new city(c.get("x"), c.get("y")));
            }

            // Run all three algorithms
            long start1 = System.nanoTime();
            List<Integer> path1 = tspService.nearestNeighbor(cities, 0);
            long time1 = (System.nanoTime() - start1) / 1_000_000;

            long start2 = System.nanoTime();
            List<Integer> path2 = tspService.dynamicProgramming(cities, 0);
            long time2 = (System.nanoTime() - start2) / 1_000_000;

            long start3 = System.nanoTime();
            List<Integer> path3 = tspService.geneticAlgorithm(cities, 0);
            long time3 = (System.nanoTime() - start3) / 1_000_000;

            // Find best path
            double dist1 = tspService.pathDistance(cities, path1);
            double dist2 = tspService.pathDistance(cities, path2);
            double dist3 = tspService.pathDistance(cities, path3);

            List<Integer> correctPath = path1;
            double bestDistance = dist1;
            if (dist2 < bestDistance) {
                correctPath = path2;
                bestDistance = dist2;
            }
            if (dist3 < bestDistance) {
                correctPath = path3;
                bestDistance = dist3;
            }

            // Calculate accuracy
            int correctMatches = 0;
            for (int i = 0; i < Math.min(userPath.size(), correctPath.size()); i++) {
                if (userPath.get(i).equals(correctPath.get(i))) {
                    correctMatches++;
                }
            }

            double percentage = ((double) correctMatches / cities.size()) * 100;
            double userDistance = tspService.pathDistance(cities, userPath);


            // Save to database
            UserScore userScore = new UserScore();
            userScore.setName(userName);
            userScore.setHomeCity(homeCityName);
            userScore.setSelectedCities(objectMapper.writeValueAsString(selectedIndices));
            userScore.setShortestRoute(objectMapper.writeValueAsString(correctPath));
            userScore.setTotalDistance(userDistance);
            userScore.setCorrectDots(correctMatches);
            userScore.setAccuracyPercentage(percentage);
            userScore.setAlgorithm1Time(time1);
            userScore.setAlgorithm2Time(time2);
            userScore.setAlgorithm3Time(time3);

            userScoreRepository.save(userScore);


            Map<String, Object> res = new HashMap<>();
            res.put("correct", correctMatches == cities.size());
            res.put("correctPath", correctPath);
            res.put("percentage", percentage);
            res.put("userDistance", userDistance);
            res.put("bestDistance", bestDistance);

            return res;
        } catch (Exception e) {
            Map<String, Object> errorRes = new HashMap<>();
            errorRes.put("error", e.getMessage());
            return errorRes;
        }
    }
}