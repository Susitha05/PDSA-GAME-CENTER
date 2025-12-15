package com.PDSA.Traffic_Simulation.service;

import java.util.*;

/**
 * Edmonds-Karp Algorithm (BFS-based Ford-Fulkerson) implementation for finding maximum flow in a network
 * Time Complexity: O(V * E^2) - More efficient than DFS Ford-Fulkerson
 */
public class DinicsAlgorithm {

    private int[][] graph;
    private int source;
    private int sink;
    private long executionTimeNanos;  // in nanoseconds
    private long executionTimeMicros; // in microseconds
    private long executionTimeMillis; // in milliseconds

    public DinicsAlgorithm(int[][] graph, int source, int sink) {
        this.graph = deepCopyGraph(graph);
        this.source = source;
        this.sink = sink;
    }

    /**
     * Finds maximum flow from source to sink using BFS (Edmonds-Karp algorithm)
     * Uses breadth-first search to find shortest augmenting paths
     */
    public int findMaxFlow() {
        long startTime = System.nanoTime();

        int[][] residualGraph = deepCopyGraph(graph);
        int maxFlow = 0;
        int[] parent;

        // Keep finding augmenting paths using BFS until none exist
        while ((parent = findPathBFS(residualGraph, source, sink)) != null) {
            // Find minimum capacity along the path
            int pathFlow = Integer.MAX_VALUE;
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, residualGraph[u][v]);
            }

            // Update residual capacities
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                residualGraph[u][v] -= pathFlow;
                residualGraph[v][u] += pathFlow;
            }

            maxFlow += pathFlow;
        }

        long endTime = System.nanoTime();
        executionTimeNanos = endTime - startTime;
        executionTimeMicros = executionTimeNanos / 1_000;      // Convert to microseconds
        executionTimeMillis = executionTimeNanos / 1_000_000;  // Convert to milliseconds

        // Log timing information
        System.out.println("[EdmondsKarp] Max Flow: " + maxFlow + 
                         " | Time: " + executionTimeNanos + "ns | " + 
                         executionTimeMicros + "µs | " + 
                         executionTimeMillis + "ms");

        return maxFlow;
    }

    /**
     * BFS to find shortest augmenting path from source to sink
     * Returns parent array if path found, null otherwise
     */
    private int[] findPathBFS(int[][] residual, int source, int sink) {
        int n = residual.length;
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        boolean[] visited = new boolean[n];
        
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);
        visited[source] = true;

        while (!queue.isEmpty()) {
            int u = queue.poll();

            // Explore all neighbors
            for (int v = 0; v < n; v++) {
                // If not visited and there is available capacity
                if (!visited[v] && residual[u][v] > 0) {
                    visited[v] = true;
                    parent[v] = u;
                    
                    // If we reached the sink, we found an augmenting path
                    if (v == sink) {
                        return parent;
                    }
                    
                    queue.add(v);
                }
            }
        }

        return null;  // No augmenting path found
    }

    /**
     * Deep copy the graph adjacency matrix
     */
    private int[][] deepCopyGraph(int[][] graph) {
        int[][] copy = new int[graph.length][];
        for (int i = 0; i < graph.length; i++) {
            copy[i] = graph[i].clone();
        }
        return copy;
    }

    public long getExecutionTime() {
        return executionTimeNanos;
    }

    public long getExecutionTimeMicros() {
        return executionTimeMicros;
    }

    public long getExecutionTimeMillis() {
        return executionTimeMillis;
    }

    public long getExecutionTimeNanos() {
        return executionTimeNanos;
    }
}
