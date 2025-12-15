package com.example.tsp.service;
import com.example.tsp.model.city;
import org.junit.jupiter.api.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Algorithm Execution Time Analysis")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class TspServiceTest {

    private TspService tspService;

    @BeforeEach
    void setUp() {
        tspService = new TspService();
    }

    @Nested
    @DisplayName("Individual Algorithm Execution Times")
    class IndividualAlgorithmTimes {

        @Test
        @Order(1)
        @DisplayName("Nearest Neighbor - Execution Time Analysis")
        void nearestNeighborExecutionTime() {
            System.out.println("\n" + "=".repeat(70));
            System.out.println("NEAREST NEIGHBOR ALGORITHM - EXECUTION TIME ANALYSIS");
            System.out.println("=".repeat(70));
            System.out.printf("%-10s | %-20s | %-20s | %-10s%n",
                    "Cities", "Time (ms)", "Time (μs)", "Operations");
            System.out.println("-".repeat(70));

            int[] cityCounts = {5, 10, 15, 20, 30, 50};

            for (int n : cityCounts) {
                List<city> cities = generateRandomCities(n);

                // Warm up JVM
                tspService.nearestNeighbor(cities, 0);

                // Actual measurement
                long startTime = System.nanoTime();
                List<Integer> path = tspService.nearestNeighbor(cities, 0);
                long endTime = System.nanoTime();

                long nanoTime = endTime - startTime;
                double milliseconds = nanoTime / 1_000_000.0;
                double microseconds = nanoTime / 1_000.0;
                int operations = n * n; // O(n²)

                System.out.printf("%-10d | %-20.3f | %-20.1f | %-10d%n",
                        n, milliseconds, microseconds, operations);

                assertNotNull(path);
            }

            System.out.println("=".repeat(70));
            System.out.println("Time Complexity: O(n²)");
            System.out.println("=".repeat(70) + "\n");
        }

        @Test
        @Order(2)
        @DisplayName("Dynamic Programming - Execution Time Analysis")
        void dynamicProgrammingExecutionTime() {
            System.out.println("\n" + "=".repeat(70));
            System.out.println("DYNAMIC PROGRAMMING ALGORITHM - EXECUTION TIME ANALYSIS");
            System.out.println("=".repeat(70));
            System.out.printf("%-10s | %-20s | %-20s | %-15s%n",
                    "Cities", "Time (ms)", "Time (seconds)", "States (2^n*n)");
            System.out.println("-".repeat(70));

            int[] cityCounts = {4, 6, 8, 10, 12};

            for (int n : cityCounts) {
                List<city> cities = generateRandomCities(n);

                // Warm up
                if (n <= 8) {
                    tspService.dynamicProgramming(cities, 0);
                }

                long startTime = System.nanoTime();
                List<Integer> path = tspService.dynamicProgramming(cities, 0);
                long endTime = System.nanoTime();

                long nanoTime = endTime - startTime;
                double milliseconds = nanoTime / 1_000_000.0;
                double seconds = nanoTime / 1_000_000_000.0;
                long states = (1L << n) * n; // 2^n * n

                System.out.printf("%-10d | %-20.3f | %-20.3f | %-15d%n",
                        n, milliseconds, seconds, states);

                assertNotNull(path);
            }

            System.out.println("-".repeat(70));
            System.out.println("Note: n > 12 uses fallback to Nearest Neighbor");
            System.out.println("Time Complexity: O(n² × 2^n)");
            System.out.println("=".repeat(70) + "\n");
        }

        @Test
        @Order(3)
        @DisplayName("Genetic Algorithm - Execution Time Analysis")
        void geneticAlgorithmExecutionTime() {
            System.out.println("\n" + "=".repeat(80));
            System.out.println("GENETIC ALGORITHM - EXECUTION TIME ANALYSIS");
            System.out.println("=".repeat(80));
            System.out.printf("%-10s | %-15s | %-15s | %-15s | %-15s%n",
                    "Cities", "Time (ms)", "Time (sec)", "Generations", "Population");
            System.out.println("-".repeat(80));

            int[] cityCounts = {5, 10, 15, 20, 30};

            for (int n : cityCounts) {
                List<city> cities = generateRandomCities(n);

                // Warm up
                if (n <= 10) {
                    tspService.geneticAlgorithm(cities, 0);
                }

                long startTime = System.nanoTime();
                List<Integer> path = tspService.geneticAlgorithm(cities, 0);
                long endTime = System.nanoTime();

                long nanoTime = endTime - startTime;
                double milliseconds = nanoTime / 1_000_000.0;
                double seconds = nanoTime / 1_000_000_000.0;

                int generations = Math.min(500, n * 50);
                int population = Math.min(100, n * 10);

                System.out.printf("%-10d | %-15.3f | %-15.3f | %-15d | %-15d%n",
                        n, milliseconds, seconds, generations, population);

                assertNotNull(path);
            }

            System.out.println("=".repeat(80));
            System.out.println("Time Complexity: O(G × P × n) where G=generations, P=population");
            System.out.println("=".repeat(80) + "\n");
        }
    }

    @Nested
    @DisplayName("Comparative Execution Time Analysis")
    class ComparativeAnalysis {

        @Test
        @Order(4)
        @DisplayName("All Algorithms - Side by Side Comparison")
        void comparativeExecutionTime() {
            System.out.println("\n" + "=".repeat(90));
            System.out.println("ALL ALGORITHMS - COMPARATIVE EXECUTION TIME ANALYSIS");
            System.out.println("=".repeat(90));
            System.out.printf("%-10s | %-20s | %-20s | %-20s | %-15s%n",
                    "Cities", "Nearest Neighbor", "Dynamic Prog", "Genetic Algo", "Winner");
            System.out.println("-".repeat(90));

            int[] cityCounts = {5, 8, 10, 12, 15, 20};

            for (int n : cityCounts) {
                List<city> cities = generateRandomCities(n);

                // Nearest Neighbor
                long startNN = System.nanoTime();
                tspService.nearestNeighbor(cities, 0);
                long endNN = System.nanoTime();
                double timeNN = (endNN - startNN) / 1_000_000.0;

                // Dynamic Programming
                long startDP = System.nanoTime();
                tspService.dynamicProgramming(cities, 0);
                long endDP = System.nanoTime();
                double timeDP = (endDP - startDP) / 1_000_000.0;

                // Genetic Algorithm
                long startGA = System.nanoTime();
                tspService.geneticAlgorithm(cities, 0);
                long endGA = System.nanoTime();
                double timeGA = (endGA - startGA) / 1_000_000.0;

                // Find fastest
                double minTime = Math.min(timeNN, Math.min(timeDP, timeGA));
                String winner = "";
                if (minTime == timeNN) winner = "Nearest Neighbor";
                else if (minTime == timeDP) winner = "Dynamic Prog";
                else winner = "Genetic Algo";

                System.out.printf("%-10d | %-17.3f ms | %-17.3f ms | %-17.3f ms | %-15s%n",
                        n, timeNN, timeDP, timeGA, winner);
            }

            System.out.println("=".repeat(90) + "\n");
        }

        @Test
        @Order(5)
        @DisplayName("Performance vs Solution Quality Trade-off")
        void performanceQualityTradeoff() {
            System.out.println("\n" + "=".repeat(100));
            System.out.println("PERFORMANCE vs SOLUTION QUALITY TRADE-OFF ANALYSIS");
            System.out.println("=".repeat(100));
            System.out.printf("%-10s | %-20s | %-20s | %-20s | %-20s%n",
                    "Cities", "Algorithm", "Time (ms)", "Distance", "Optimality %");
            System.out.println("-".repeat(100));

            int[] cityCounts = {8, 10, 12};

            for (int n : cityCounts) {
                List<city> cities = generateRandomCities(n);

                // Nearest Neighbor
                long startNN = System.nanoTime();
                List<Integer> pathNN = tspService.nearestNeighbor(cities, 0);
                long endNN = System.nanoTime();
                double timeNN = (endNN - startNN) / 1_000_000.0;
                double distNN = tspService.pathDistance(cities, pathNN);

                // Dynamic Programming (optimal)
                long startDP = System.nanoTime();
                List<Integer> pathDP = tspService.dynamicProgramming(cities, 0);
                long endDP = System.nanoTime();
                double timeDP = (endDP - startDP) / 1_000_000.0;
                double distDP = tspService.pathDistance(cities, pathDP);

                // Genetic Algorithm
                long startGA = System.nanoTime();
                List<Integer> pathGA = tspService.geneticAlgorithm(cities, 0);
                long endGA = System.nanoTime();
                double timeGA = (endGA - startGA) / 1_000_000.0;
                double distGA = tspService.pathDistance(cities, pathGA);

                // Calculate optimality (DP is 100% optimal)
                double optimalityNN = (distDP / distNN) * 100;
                double optimalityGA = (distDP / distGA) * 100;

                System.out.printf("%-10d | %-20s | %-17.3f ms | %-17.2f km | %-17.1f %%%n",
                        n, "Nearest Neighbor", timeNN, distNN, optimalityNN);
                System.out.printf("%-10s | %-20s | %-17.3f ms | %-17.2f km | %-17.1f %%%n",
                        "", "Dynamic Programming", timeDP, distDP, 100.0);
                System.out.printf("%-10s | %-20s | %-17.3f ms | %-17.2f km | %-17.1f %%%n",
                        "", "Genetic Algorithm", timeGA, distGA, optimalityGA);
                System.out.println("-".repeat(100));
            }

            System.out.println("=".repeat(100) + "\n");
        }
    }

    @Nested
    @DisplayName("Scaling Analysis")
    class ScalingAnalysis {

        @Test
        @Order(6)
        @DisplayName("Nearest Neighbor - Scaling Analysis (O(n²))")
        void nearestNeighborScaling() {
            System.out.println("\n" + "=".repeat(80));
            System.out.println("NEAREST NEIGHBOR - SCALING ANALYSIS");
            System.out.println("=".repeat(80));
            System.out.printf("%-10s | %-15s | %-20s | %-25s%n",
                    "Cities", "Time (ms)", "Expected Ratio", "Actual Ratio");
            System.out.println("-".repeat(80));

            int[] cityCounts = {10, 20, 40, 80};
            double[] times = new double[cityCounts.length];

            for (int i = 0; i < cityCounts.length; i++) {
                List<city> cities = generateRandomCities(cityCounts[i]);

                long startTime = System.nanoTime();
                tspService.nearestNeighbor(cities, 0);
                long endTime = System.nanoTime();

                times[i] = (endTime - startTime) / 1_000_000.0;

                if (i > 0) {
                    double expectedRatio = Math.pow((double)cityCounts[i] / cityCounts[i-1], 2);
                    double actualRatio = times[i] / times[i-1];

                    System.out.printf("%-10d | %-15.3f | %-20.2f | %-25.2f%n",
                            cityCounts[i], times[i], expectedRatio, actualRatio);
                } else {
                    System.out.printf("%-10d | %-15.3f | %-20s | %-25s%n",
                            cityCounts[i], times[i], "-", "-");
                }
            }

            System.out.println("-".repeat(80));
            System.out.println("Expected: O(n²) - doubling n should quadruple time");
            System.out.println("=".repeat(80) + "\n");
        }

        @Test
        @Order(7)
        @DisplayName("Dynamic Programming - Exponential Growth Analysis")
        void dynamicProgrammingScaling() {
            System.out.println("\n" + "=".repeat(80));
            System.out.println("DYNAMIC PROGRAMMING - EXPONENTIAL GROWTH ANALYSIS");
            System.out.println("=".repeat(80));
            System.out.printf("%-10s | %-20s | %-25s%n",
                    "Cities", "Time (ms)", "Growth Factor (vs n-1)");
            System.out.println("-".repeat(80));

            int[] cityCounts = {4, 6, 8, 10, 12};
            double[] times = new double[cityCounts.length];

            for (int i = 0; i < cityCounts.length; i++) {
                List<city> cities = generateRandomCities(cityCounts[i]);

                long startTime = System.nanoTime();
                tspService.dynamicProgramming(cities, 0);
                long endTime = System.nanoTime();

                times[i] = (endTime - startTime) / 1_000_000.0;

                if (i > 0) {
                    double growthFactor = times[i] / times[i-1];
                    System.out.printf("%-10d | %-20.3f | %-25.2fx%n",
                            cityCounts[i], times[i], growthFactor);
                } else {
                    System.out.printf("%-10d | %-20.3f | %-25s%n",
                            cityCounts[i], times[i], "-");
                }
            }

            System.out.println("-".repeat(80));
            System.out.println("Expected: Exponential growth (2^n factor)");
            System.out.println("=".repeat(80) + "\n");
        }
    }

    @Nested
    @DisplayName("Statistical Analysis")
    class StatisticalAnalysis {

        @Test
        @Order(8)
        @DisplayName("Genetic Algorithm - Multiple Run Statistics")
        void geneticAlgorithmStatistics() {
            System.out.println("\n" + "=".repeat(90));
            System.out.println("GENETIC ALGORITHM - STATISTICAL ANALYSIS (10 runs each)");
            System.out.println("=".repeat(90));
            System.out.printf("%-10s | %-15s | %-15s | %-15s | %-15s | %-15s%n",
                    "Cities", "Avg Time (ms)", "Min Time", "Max Time", "Std Dev", "Variance");
            System.out.println("-".repeat(90));

            int[] cityCounts = {8, 12, 16};

            for (int n : cityCounts) {
                List<city> cities = generateRandomCities(n);
                List<Double> times = new ArrayList<>();

                // Run 10 times
                for (int run = 0; run < 10; run++) {
                    long startTime = System.nanoTime();
                    tspService.geneticAlgorithm(cities, 0);
                    long endTime = System.nanoTime();

                    times.add((endTime - startTime) / 1_000_000.0);
                }

                double avg = times.stream().mapToDouble(Double::doubleValue).average().orElse(0);
                double min = times.stream().mapToDouble(Double::doubleValue).min().orElse(0);
                double max = times.stream().mapToDouble(Double::doubleValue).max().orElse(0);
                double variance = times.stream()
                        .mapToDouble(t -> Math.pow(t - avg, 2))
                        .average().orElse(0);
                double stdDev = Math.sqrt(variance);

                System.out.printf("%-10d | %-15.3f | %-15.3f | %-15.3f | %-15.3f | %-15.3f%n",
                        n, avg, min, max, stdDev, variance);
            }

            System.out.println("=".repeat(90));
            System.out.println("Note: Genetic Algorithm shows variance due to stochastic nature");
            System.out.println("=".repeat(90) + "\n");
        }

        @Test
        @Order(9)
        @DisplayName("15 Rounds Execution Time Collection")
        void executionTimeFor15Rounds() {

            int rounds = 15;
            int cityCount = 12; // fixed for fair comparison

            List<Double> nnTimes = new ArrayList<>();
            List<Double> dpTimes = new ArrayList<>();
            List<Double> gaTimes = new ArrayList<>();

            System.out.println("\nRound | NN(ms) | DP(ms) | GA(ms)");
            System.out.println("--------------------------------");

            for (int round = 1; round <= rounds; round++) {

                List<city> cities = generateRandomCities(cityCount);

                // Nearest Neighbor
                long startNN = System.nanoTime();
                tspService.nearestNeighbor(cities, 0);
                long endNN = System.nanoTime();
                nnTimes.add((endNN - startNN) / 1_000_000.0);

                // Dynamic Programming
                long startDP = System.nanoTime();
                tspService.dynamicProgramming(cities, 0);
                long endDP = System.nanoTime();
                dpTimes.add((endDP - startDP) / 1_000_000.0);


                long startGA = System.nanoTime();
                tspService.geneticAlgorithm(cities, 0);
                long endGA = System.nanoTime();
                gaTimes.add((endGA - startGA) / 1_000_000.0);

                System.out.printf("%5d | %6.2f | %6.2f | %6.2f%n",
                        round,
                        nnTimes.get(round - 1),
                        dpTimes.get(round - 1),
                        gaTimes.get(round - 1));
            }

            assertEquals(rounds, nnTimes.size());
            assertEquals(rounds, dpTimes.size());
            assertEquals(rounds, gaTimes.size());
        }


    }


    // Helper method
    private List<city> generateRandomCities(int count) {
        Random random = new Random(42); // Fixed seed for reproducibility
        List<city> cities = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            cities.add(new city(
                    random.nextDouble() * 100,
                    random.nextDouble() * 100
            ));
        }

        return cities;
    }
}