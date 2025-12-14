package com.PDSA.Traffic_Simulation.service;

import java.util.*;

/**
 * Ford-Fulkerson Algorithm implementation for finding maximum flow in a network
 * Time Complexity: O(E * |max_flow|) where E is number of edges
 */
public class FordFulkersonAlgorithm {

    private int[][] graph;
    private int source;
    private int sink;
    private long executionTimeNanos;  // in nanoseconds
    private long executionTimeMicros; // in microseconds
    private long executionTimeMillis; // in milliseconds

    public FordFulkersonAlgorithm(int[][] graph, int source, int sink) {
        this.graph = deepCopyGraph(graph);
        this.source = source;
        this.sink = sink;
    }

    /**
     * Finds maximum flow from source to sink using DFS
     */
    public int findMaxFlow() {
        long startTime = System.nanoTime();

        int[][] residualGraph = deepCopyGraph(graph);
        int maxFlow = 0;
        int[] parent;

        while ((parent = findPathDFS(residualGraph, source, sink)) != null) {
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
        System.out.println("[FordFulkerson] Max Flow: " + maxFlow + 
                         " | Time: " + executionTimeNanos + "ns | " + 
                         executionTimeMicros + "µs | " + 
                         executionTimeMillis + "ms");

        return maxFlow;
    }

    /**
     * DFS to find an augmenting path from source to sink
     * Uses iterative DFS with proper visited tracking and parent recording
     */
    private int[] findPathDFS(int[][] residual, int source, int sink) {
        int n = residual.length;
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        boolean[] visited = new boolean[n];
        
        // Use recursive DFS approach wrapped in iterative manner
        Stack<Integer> stack = new Stack<>();
        stack.push(source);
        visited[source] = true;

        while (!stack.isEmpty()) {
            int u = stack.peek();
            boolean foundChild = false;

            // Try to find an unvisited neighbor with available capacity
            for (int v = 0; v < n; v++) {
                if (!visited[v] && residual[u][v] > 0) {
                    visited[v] = true;
                    parent[v] = u;
                    stack.push(v);
                    
                    // If we reached the sink, we found an augmenting path
                    if (v == sink) {
                        return parent;
                    }
                    
                    foundChild = true;
                    break;  // Continue with next node on stack
                }
            }

            // If no unvisited neighbor found, backtrack
            if (!foundChild) {
                stack.pop();
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
