package com.example.tsp.service;

import com.example.tsp.model.city;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TspService {


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

    public List<Integer> dynamicProgramming(List<city> cities, int startIndex) {
        int n = cities.size();

        if (n > 12) {
            return nearestNeighbor(cities, startIndex);
        }

        double[][] dist = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dist[i][j] = distance(cities.get(i), cities.get(j));
            }
        }

        int fullMask = (1 << n) - 1;


        double[][] dp = new double[1 << n][n];


        int[][] parent = new int[1 << n][n];


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

            Collections.shuffle(individual, random);
            individual.add(0, startIndex);

            population.add(individual);
        }


        for (int gen = 0; gen < generations; gen++) {
            // Calculate fitness for all individuals
            Map<List<Integer>, Double> fitness = new HashMap<>();
            for (List<Integer> ind : population) {
                double dist = pathDistance(cities, ind);

                fitness.put(ind, 1.0 / (dist + 1));
            }


            List<List<Integer>> newPopulation = new ArrayList<>();

            List<Integer> best = population.get(0);
            double bestFitness = fitness.get(best);
            for (List<Integer> ind : population) {
                if (fitness.get(ind) > bestFitness) {
                    best = ind;
                    bestFitness = fitness.get(ind);
                }
            }
            newPopulation.add(new ArrayList<>(best));


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


    private void swapMutate(List<Integer> individual, int startIndex, Random random) {
        int n = individual.size();
        if (n <= 2) return; // Can't mutate if only 2 cities

        // Select two random positions (excluding position 0 which is start city)
        int i = random.nextInt(n - 1) + 1;
        int j = random.nextInt(n - 1) + 1;

        // Swap
        Collections.swap(individual, i, j);
    }


    public double distance(city a, city b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        // Scale to kilometers (assuming map is 1000km × 1000km)
        return Math.sqrt(dx * dx + dy * dy) * 10;
    }


    public double pathDistance(List<city> cities, List<Integer> order) {
        double total = 0;
        for (int i = 0; i < order.size() - 1; i++) {
            total += distance(cities.get(order.get(i)), cities.get(order.get(i + 1)));
        }
        return total;
    }
}