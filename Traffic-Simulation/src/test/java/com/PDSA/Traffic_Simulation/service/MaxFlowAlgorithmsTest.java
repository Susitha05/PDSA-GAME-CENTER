package com.PDSA.Traffic_Simulation.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Performance comparison tests for Ford-Fulkerson and Edmonds-Karp algorithms
 * Runs 15 rounds of each algorithm and displays all timing results in tables
 */
class MaxFlowAlgorithmsTest {

    private int[][] simpleGraph;
    private int[][] complexGraph;
    private int[][] largeGraph;

    @BeforeEach
    void setUp() {
        // Simple graph with 6 nodes
        simpleGraph = new int[][]{
            {0, 16, 13, 0, 0, 0},
            {0, 0, 10, 12, 0, 0},
            {0, 4, 0, 0, 14, 0},
            {0, 0, 9, 0, 0, 20},
            {0, 0, 0, 7, 0, 4},
            {0, 0, 0, 0, 0, 0}
        };

        // Complex graph with 4 nodes
        complexGraph = new int[][]{
            {0, 10, 10, 0},
            {0, 0, 2, 4},
            {0, 0, 0, 9},
            {0, 0, 0, 0}
        };

        // Larger graph with 8 nodes
        largeGraph = new int[][]{
            {0, 10, 5, 15, 0, 0, 0, 0},
            {0, 0, 4, 0, 9, 15, 0, 0},
            {0, 0, 0, 4, 0, 8, 0, 0},
            {0, 0, 0, 0, 0, 0, 16, 0},
            {0, 0, 0, 0, 0, 0, 15, 10},
            {0, 0, 0, 0, 0, 0, 0, 10},
            {0, 0, 0, 0, 0, 0, 0, 10},
            {0, 0, 0, 0, 0, 0, 0, 0}
        };
    }

    @Test
    @DisplayName("Performance Comparison: 15 rounds on Simple Graph")
    void testPerformanceSimpleGraph() {
        System.out.println("\n" + "=".repeat(120));
        System.out.println("PERFORMANCE TEST: SIMPLE GRAPH (6 nodes) - 15 ROUNDS");
        System.out.println("=".repeat(120));
        runPerformanceTest(simpleGraph, 0, 5, 15);
    }

    @Test
    @DisplayName("Performance Comparison: 15 rounds on Complex Graph")
    void testPerformanceComplexGraph() {
        System.out.println("\n" + "=".repeat(120));
        System.out.println("PERFORMANCE TEST: COMPLEX GRAPH (4 nodes) - 15 ROUNDS");
        System.out.println("=".repeat(120));
        runPerformanceTest(complexGraph, 0, 3, 15);
    }

    @Test
    @DisplayName("Performance Comparison: 15 rounds on Large Graph")
    void testPerformanceLargeGraph() {
        System.out.println("\n" + "=".repeat(120));
        System.out.println("PERFORMANCE TEST: LARGE GRAPH (8 nodes) - 15 ROUNDS");
        System.out.println("=".repeat(120));
        runPerformanceTest(largeGraph, 0, 7, 15);
    }

    @Test
    @DisplayName("Complete Performance Suite: All Graphs")
    void testCompletePerformanceSuite() {
        System.out.println("\n" + "=".repeat(120));
        System.out.println("COMPLETE PERFORMANCE SUITE - ALL GRAPHS - 15 ROUNDS EACH");
        System.out.println("=".repeat(120));
        
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                          SIMPLE GRAPH (6 nodes)                                                   ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
        runPerformanceTest(simpleGraph, 0, 5, 15);
        
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                          COMPLEX GRAPH (4 nodes)                                                  ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
        runPerformanceTest(complexGraph, 0, 3, 15);
        
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                          LARGE GRAPH (8 nodes)                                                    ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
        runPerformanceTest(largeGraph, 0, 7, 15);
    }

    private void runPerformanceTest(int[][] graph, int source, int sink, int rounds) {
        long[] ffNanos = new long[rounds];
        long[] ffMicros = new long[rounds];
        long[] ffMillis = new long[rounds];
        long[] ekNanos = new long[rounds];
        long[] ekMicros = new long[rounds];
        long[] ekMillis = new long[rounds];
        int ffMaxFlow = 0;
        int ekMaxFlow = 0;

        // Warm-up runs (not counted)
        System.out.println("\nPerforming warm-up runs...");
        for (int i = 0; i < 3; i++) {
            new FordFulkersonAlgorithm(graph, source, sink).findMaxFlow();
            new DinicsAlgorithm(graph, source, sink).findMaxFlow();
        }

        // Run Ford-Fulkerson
        System.out.println("Running Ford-Fulkerson Algorithm (" + rounds + " rounds)...");
        for (int i = 0; i < rounds; i++) {
            FordFulkersonAlgorithm ff = new FordFulkersonAlgorithm(graph, source, sink);
            ffMaxFlow = ff.findMaxFlow();
            ffNanos[i] = ff.getExecutionTimeNanos();
            ffMicros[i] = ff.getExecutionTimeMicros();
            ffMillis[i] = ff.getExecutionTimeMillis();
        }

        // Run Edmonds-Karp (BFS)
        System.out.println("Running Edmonds-Karp Algorithm (" + rounds + " rounds)...");
        for (int i = 0; i < rounds; i++) {
            DinicsAlgorithm ek = new DinicsAlgorithm(graph, source, sink);
            ekMaxFlow = ek.findMaxFlow();
            ekNanos[i] = ek.getExecutionTimeNanos();
            ekMicros[i] = ek.getExecutionTimeMicros();
            ekMillis[i] = ek.getExecutionTimeMillis();
        }

        // Display all results
        displayCompleteResultsTable(ffNanos, ffMicros, ffMillis, ekNanos, ekMicros, ekMillis, ffMaxFlow, ekMaxFlow);
        
        // Verify both algorithms produce same result
        assertEquals(ffMaxFlow, ekMaxFlow, 
            "Both algorithms should produce the same max flow");
    }

    private void displayCompleteResultsTable(long[] ffNanos, long[] ffMicros, long[] ffMillis, 
                                            long[] ekNanos, long[] ekMicros, long[] ekMillis,
                                            int ffMaxFlow, int ekMaxFlow) {
        System.out.println("\n" + "=".repeat(120));
        System.out.println("COMPLETE EXECUTION TIME RESULTS - ALL TIME UNITS");
        System.out.println("=".repeat(120));
        
        // Table header
        System.out.printf("%-6s │ %-15s │ %-15s │ %-15s │ %-15s │ %-15s │ %-15s%n", 
            "Round", "FF Nanos", "FF Micros", "FF Millis", "EK Nanos", "EK Micros", "EK Millis");
        System.out.println("─".repeat(120));

        // Display each round with all time units
        for (int i = 0; i < ffNanos.length; i++) {
            System.out.printf("%-6d │ %,15d │ %,15d │ %,15d │ %,15d │ %,15d │ %,15d%n", 
                (i + 1), 
                ffNanos[i], ffMicros[i], ffMillis[i],
                ekNanos[i], ekMicros[i], ekMillis[i]);
        }

        System.out.println("=".repeat(120));

        // Calculate statistics
        long ffNanosMin = Long.MAX_VALUE, ffNanosMax = 0, ffNanosSum = 0;
        long ffMicrosMin = Long.MAX_VALUE, ffMicrosMax = 0, ffMicrosSum = 0;
        long ffMillisMin = Long.MAX_VALUE, ffMillisMax = 0, ffMillisSum = 0;
        
        long ekNanosMin = Long.MAX_VALUE, ekNanosMax = 0, ekNanosSum = 0;
        long ekMicrosMin = Long.MAX_VALUE, ekMicrosMax = 0, ekMicrosSum = 0;
        long ekMillisMin = Long.MAX_VALUE, ekMillisMax = 0, ekMillisSum = 0;

        for (int i = 0; i < ffNanos.length; i++) {
            // Ford-Fulkerson stats
            ffNanosMin = Math.min(ffNanosMin, ffNanos[i]);
            ffNanosMax = Math.max(ffNanosMax, ffNanos[i]);
            ffNanosSum += ffNanos[i];
            
            ffMicrosMin = Math.min(ffMicrosMin, ffMicros[i]);
            ffMicrosMax = Math.max(ffMicrosMax, ffMicros[i]);
            ffMicrosSum += ffMicros[i];
            
            ffMillisMin = Math.min(ffMillisMin, ffMillis[i]);
            ffMillisMax = Math.max(ffMillisMax, ffMillis[i]);
            ffMillisSum += ffMillis[i];

            // Edmonds-Karp stats
            ekNanosMin = Math.min(ekNanosMin, ekNanos[i]);
            ekNanosMax = Math.max(ekNanosMax, ekNanos[i]);
            ekNanosSum += ekNanos[i];
            
            ekMicrosMin = Math.min(ekMicrosMin, ekMicros[i]);
            ekMicrosMax = Math.max(ekMicrosMax, ekMicros[i]);
            ekMicrosSum += ekMicros[i];
            
            ekMillisMin = Math.min(ekMillisMin, ekMillis[i]);
            ekMillisMax = Math.max(ekMillisMax, ekMillis[i]);
            ekMillisSum += ekMillis[i];
        }

        double ffNanosAvg = (double) ffNanosSum / ffNanos.length;
        double ffMicrosAvg = (double) ffMicrosSum / ffMicros.length;
        double ffMillisAvg = (double) ffMillisSum / ffMillis.length;
        
        double ekNanosAvg = (double) ekNanosSum / ekNanos.length;
        double ekMicrosAvg = (double) ekMicrosSum / ekMicros.length;
        double ekMillisAvg = (double) ekMillisSum / ekMillis.length;

        // Display statistics
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                            STATISTICAL SUMMARY                                                     ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
        
        System.out.println("\n┌─ NANOSECONDS ─────────────────────────────────────────────────────────────────────────────────────────────────────┐");
        System.out.printf("│ %-20s │ %,30d │ %,30d │%n", "Min Time (ns)", ffNanosMin, ekNanosMin);
        System.out.printf("│ %-20s │ %,30d │ %,30d │%n", "Max Time (ns)", ffNanosMax, ekNanosMax);
        System.out.printf("│ %-20s │ %,30.2f │ %,30.2f │%n", "Avg Time (ns)", ffNanosAvg, ekNanosAvg);
        System.out.printf("│ %-20s │ %,30d │ %,30d │%n", "Total Time (ns)", ffNanosSum, ekNanosSum);
        System.out.println("└───────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘");
        
        System.out.println("\n┌─ MICROSECONDS ────────────────────────────────────────────────────────────────────────────────────────────────────┐");
        System.out.printf("│ %-20s │ %,30d │ %,30d │%n", "Min Time (μs)", ffMicrosMin, ekMicrosMin);
        System.out.printf("│ %-20s │ %,30d │ %,30d │%n", "Max Time (μs)", ffMicrosMax, ekMicrosMax);
        System.out.printf("│ %-20s │ %,30.2f │ %,30.2f │%n", "Avg Time (μs)", ffMicrosAvg, ekMicrosAvg);
        System.out.printf("│ %-20s │ %,30d │ %,30d │%n", "Total Time (μs)", ffMicrosSum, ekMicrosSum);
        System.out.println("└───────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘");
        
        System.out.println("\n┌─ MILLISECONDS ────────────────────────────────────────────────────────────────────────────────────────────────────┐");
        System.out.printf("│ %-20s │ %,30d │ %,30d │%n", "Min Time (ms)", ffMillisMin, ekMillisMin);
        System.out.printf("│ %-20s │ %,30d │ %,30d │%n", "Max Time (ms)", ffMillisMax, ekMillisMax);
        System.out.printf("│ %-20s │ %,30.2f │ %,30.2f │%n", "Avg Time (ms)", ffMillisAvg, ekMillisAvg);
        System.out.printf("│ %-20s │ %,30d │ %,30d │%n", "Total Time (ms)", ffMillisSum, ekMillisSum);
        System.out.println("└───────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘");

        // Display max flow result
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                            MAX FLOW RESULTS                                                        ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.printf("  Ford-Fulkerson Max Flow: %d%n", ffMaxFlow);
        System.out.printf("  Edmonds-Karp Max Flow:   %d%n", ekMaxFlow);
        System.out.printf("  Results Match: %s%n", (ffMaxFlow == ekMaxFlow ? "✓ YES" : "✗ NO"));
        System.out.println("=".repeat(120));

        // Performance comparison
        double speedup = ffNanosAvg / ekNanosAvg;
        String fasterAlgo = speedup > 1 ? "Edmonds-Karp (BFS)" : "Ford-Fulkerson (DFS)";
        double speedupPercent = Math.abs((speedup - 1) * 100);
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                         PERFORMANCE COMPARISON                                                     ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.printf("  Faster Algorithm:    %s%n", fasterAlgo);
        System.out.printf("  Speed Improvement:   %.2f%% faster%n", speedupPercent);
        System.out.printf("  Speedup Factor:      %.2fx%n", Math.max(speedup, 1/speedup));
        System.out.println("=".repeat(120) + "\n");
    }
}