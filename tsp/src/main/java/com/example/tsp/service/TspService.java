package com.example.tsp.service;

import com.example.tsp.model.city;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service class implementing three different algorithms to solve the Traveling Salesman Problem (TSP)
 *
 * Algorithm 1: Nearest Neighbor (Greedy) - O(n²) time complexity
 * Algorithm 2: Dynamic Programming (Held-Karp) - O(n²·2^n) time complexity
 * Algorithm 3: Genetic Algorithm - O(generations × population × n) time complexity
 */
@Service
public class TspService {

    /**
     * Algorithm 1: Nearest Neighbor (Greedy Approach)
     *
     * Time Complexity: O(n²)
     * Space Complexity: O(n)
     *
     * Description:
     * - Starts from the given city (usually home city)
     * - At each step, moves to the nearest unvisited city
     * - Continues until all cities are visited
     *
     * Advantages:
     * - Very fast execution
     * - Simple to implement
     * - Works well for large datasets
     *
     * Disadvantages:
     * - Does not guarantee optimal solution
     * - Typically produces paths 25% longer than optimal
     *
     * @param cities List of cities to visit
     * @param startIndex Index of starting city (home city)
     * @return List of city indices representing the path order
     */
    public List<Integer> nearestNeighbor(List<city> cities, int startIndex) {
        int n = cities.size();
        boolean[] visited = new boolean[n];
        List<Integer> order = new ArrayList<>();

        int current = startIndex;
        order.add(current);
        visited[current] = true;

        // Visit remaining cities
        for (int step = 1; step < n; step++) {
            int nextCity = -1;
            double shortest = Double.MAX_VALUE;

            // Find nearest unvisited city
            for (int i = 0; i < n; i++) {
                if (!visited[i]) {
                    double d = distance(cities.get(current), cities.get(i));
                    if (d < shortest) {
                        shortest = d;
                        nextCity = i;
                    }
                }
            }

            if (nextCity != -1) {
                order.add(nextCity);
                visited[nextCity] = true;
                current = nextCity;
            }
        }

        return order;
    }

    /**
     * Algorithm 2: Dynamic Programming (Held-Karp Algorithm)
     *
     * Time Complexity: O(n² × 2^n)
     * Space Complexity: O(n × 2^n)
     *
     * Description:
     * - Uses bitmask to represent sets of visited cities
     * - Computes optimal subpaths and combines them
     * - Guarantees finding the optimal solution
     *
     * Advantages:
     * - Finds guaranteed optimal solution
     * - More efficient than brute force O(n!)
     *
     * Disadvantages:
     * - Exponential time and space complexity
     * - Only practical for small n (typically n ≤ 15)
     * - High memory usage
     *
     * Note: Falls back to Nearest Neighbor for n > 12 to prevent performance issues
     *
     * @param cities List of cities to visit
     * @param startIndex Index of starting city
     * @return List of city indices representing optimal path
     */
    public List<Integer> dynamicProgramming(List<city> cities, int startIndex) {
        int n = cities.size();

        // For larger sets, fall back to nearest neighbor to prevent exponential explosion
        if (n > 12) {
            return nearestNeighbor(cities, startIndex);
        }

        // Build distance matrix
        double[][] dist = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dist[i][j] = distance(cities.get(i), cities.get(j));
            }
        }

        int fullMask = (1 << n) - 1;

        // dp[mask][i] = minimum distance to visit all cities in mask, ending at city i
        double[][] dp = new double[1 << n][n];

        // parent[mask][i] = previous city in optimal path to reach state (mask, i)
        int[][] parent = new int[1 << n][n];

        // Initialize DP table with infinity
        for (double[] row : dp) {
            Arrays.fill(row, Double.MAX_VALUE);
        }

        // Base case: start at startIndex
        dp[1 << startIndex][startIndex] = 0;

        // Fill DP table
        for (int mask = 0; mask <= fullMask; mask++) {
            for (int last = 0; last < n; last++) {
                // Skip if this city is not in the current mask
                if ((mask & (1 << last)) == 0) continue;

                // Skip if this state hasn't been reached yet
                if (dp[mask][last] == Double.MAX_VALUE) continue;

                // Try adding each unvisited city
                for (int next = 0; next < n; next++) {
                    // Skip if city already visited
                    if ((mask & (1 << next)) != 0) continue;

                    int newMask = mask | (1 << next);
                    double newDist = dp[mask][last] + dist[last][next];

                    if (newDist < dp[newMask][next]) {
                        dp[newMask][next] = newDist;
                        parent[newMask][next] = last;
                    }
                }
            }
        }

        // Find the best ending city
        double minDist = Double.MAX_VALUE;
        int lastCity = -1;
        for (int i = 0; i < n; i++) {
            if (dp[fullMask][i] < minDist) {
                minDist = dp[fullMask][i];
                lastCity = i;
            }
        }

        // Reconstruct path
        List<Integer> path = new ArrayList<>();
        int mask = fullMask;
        int current = lastCity;

        while (mask != 0) {
            path.add(0, current);
            int prev = parent[mask][current];
            mask ^= (1 << current);
            current = prev;
        }

        return path;
    }

    /**
     * Algorithm 3: Genetic Algorithm
     *
     * Time Complexity: O(G × P × n) where G = generations, P = population size
     * Space Complexity: O(P × n)
     *
     * Description:
     * - Mimics natural evolution to find near-optimal solutions
     * - Maintains population of candidate solutions
     * - Uses selection, crossover, and mutation to evolve solutions
     *
     * Process:
     * 1. Initialize random population of tours
     * 2. Evaluate fitness (inverse of distance)
     * 3. Select parents using tournament selection
     * 4. Create offspring through crossover
     * 5. Apply mutation to maintain diversity
     * 6. Replace population with new generation
     * 7. Repeat for multiple generations
     *
     * Advantages:
     * - Scales well to larger problems
     * - Often finds near-optimal solutions
     * - Avoids getting stuck in local optima
     *
     * Disadvantages:
     * - Non-deterministic (results may vary)
     * - Slower than greedy for small datasets
     * - Requires parameter tuning
     *
     * @param cities List of cities to visit
     * @param startIndex Index of starting city (must be first in path)
     * @return List of city indices representing near-optimal path
     */
    public List<Integer> geneticAlgorithm(List<city> cities, int startIndex) {
        int n = cities.size();

        // Genetic algorithm parameters
        int populationSize = Math.min(100, n * 10);
        int generations = Math.min(500, n * 50);
        double mutationRate = 0.15;
        int tournamentSize = 5;

        List<List<Integer>> population = new ArrayList<>();
        Random random = new Random();

        // Initialize population with random tours (all starting from startIndex)
        for (int i = 0; i < populationSize; i++) {
            List<Integer> individual = new ArrayList<>();

            // Add all cities except start city
            for (int j = 0; j < n; j++) {
                if (j != startIndex) {
                    individual.add(j);
                }
            }

            // Shuffle to create random tour
            Collections.shuffle(individual, random);

            // Add start city at beginning
            individual.add(0, startIndex);

            population.add(individual);
        }

        // Evolution loop
        for (int gen = 0; gen < generations; gen++) {
            // Calculate fitness for all individuals
            Map<List<Integer>, Double> fitness = new HashMap<>();
            for (List<Integer> ind : population) {
                double dist = pathDistance(cities, ind);
                // Fitness is inverse of distance (higher is better)
                fitness.put(ind, 1.0 / (dist + 1));
            }

            // Create new population
            List<List<Integer>> newPopulation = new ArrayList<>();

            // Elitism: Keep the best individual
            List<Integer> best = population.get(0);
            double bestFitness = fitness.get(best);
            for (List<Integer> ind : population) {
                if (fitness.get(ind) > bestFitness) {
                    best = ind;
                    bestFitness = fitness.get(ind);
                }
            }
            newPopulation.add(new ArrayList<>(best));

            // Generate rest of population through selection, crossover, and mutation
            while (newPopulation.size() < populationSize) {
                // Selection: Tournament selection
                List<Integer> parent1 = tournamentSelect(population, fitness, random, tournamentSize);
                List<Integer> parent2 = tournamentSelect(population, fitness, random, tournamentSize);

                // Crossover: Ordered crossover
                List<Integer> child = orderedCrossover(parent1, parent2, startIndex, random);

                // Mutation: Swap mutation
                if (random.nextDouble() < mutationRate) {
                    swapMutate(child, startIndex, random);
                }

                newPopulation.add(child);
            }

            population = newPopulation;
        }

        // Return best solution from final population
        List<Integer> bestSolution = population.get(0);
        double bestDist = pathDistance(cities, bestSolution);

        for (List<Integer> ind : population) {
            double dist = pathDistance(cities, ind);
            if (dist < bestDist) {
                bestDist = dist;
                bestSolution = ind;
            }
        }

        return bestSolution;
    }

    /**
     * Tournament Selection: Select an individual using tournament
     * Randomly picks tournamentSize individuals and returns the best
     */
    private List<Integer> tournamentSelect(List<List<Integer>> population,
                                           Map<List<Integer>, Double> fitness,
                                           Random random,
                                           int tournamentSize) {
        List<Integer> best = population.get(random.nextInt(population.size()));
        double bestFitness = fitness.get(best);

        for (int i = 1; i < tournamentSize; i++) {
            List<Integer> competitor = population.get(random.nextInt(population.size()));
            double compFitness = fitness.get(competitor);

            if (compFitness > bestFitness) {
                best = competitor;
                bestFitness = compFitness;
            }
        }

        return best;
    }

    /**
     * Ordered Crossover (OX): Preserves relative order of cities
     * Takes a segment from parent1 and fills remaining cities from parent2
     */
    private List<Integer> orderedCrossover(List<Integer> parent1,
                                           List<Integer> parent2,
                                           int startIndex,
                                           Random random) {
        int n = parent1.size();
        List<Integer> child = new ArrayList<>(Collections.nCopies(n, -1));

        // Start city must be at position 0
        child.set(0, startIndex);

        // Select random segment from parent1
        int start = random.nextInt(n - 1) + 1;
        int end = random.nextInt(n - start) + start;

        // Copy segment from parent1
        for (int i = start; i <= end; i++) {
            child.set(i, parent1.get(i));
        }

        // Fill remaining positions with cities from parent2 in order
        int currentPos = 1;
        for (int i = 1; i < n; i++) {
            if (currentPos == start) {
                currentPos = end + 1;
            }
            if (currentPos >= n) break;

            int city = parent2.get(i);
            if (!child.contains(city)) {
                child.set(currentPos++, city);
            }
        }

        return child;
    }

    /**
     * Swap Mutation: Randomly swap two cities (excluding start city)
     */
    private void swapMutate(List<Integer> individual, int startIndex, Random random) {
        int n = individual.size();
        if (n <= 2) return; // Can't mutate if only 2 cities

        // Select two random positions (excluding position 0 which is start city)
        int i = random.nextInt(n - 1) + 1;
        int j = random.nextInt(n - 1) + 1;

        // Swap
        Collections.swap(individual, i, j);
    }

    /**
     * Calculate Euclidean distance between two cities
     * Scaled by factor of 10 to represent kilometers
     *
     * Formula: distance = sqrt((x2-x1)² + (y2-y1)²) × 10
     *
     * @param a First city
     * @param b Second city
     * @return Distance in kilometers
     */
    public double distance(city a, city b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        // Scale to kilometers (assuming map is 1000km × 1000km)
        return Math.sqrt(dx * dx + dy * dy) * 10;
    }

    /**
     * Calculate total distance of a path
     * Sums up distances between consecutive cities in the path
     *
     * @param cities List of all cities
     * @param order List of indices representing the path order
     * @return Total distance of the path in kilometers
     */
    public double pathDistance(List<city> cities, List<Integer> order) {
        double total = 0;
        for (int i = 0; i < order.size() - 1; i++) {
            total += distance(cities.get(order.get(i)), cities.get(order.get(i + 1)));
        }
        return total;
    }
}