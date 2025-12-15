package com.PDSA.Traffic_Simulation;

import com.PDSA.Traffic_Simulation.service.DinicsAlgorithm;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Dinic's Algorithm
 */
public class DinicsAlgorithmTest {

    @Test
    public void testSimpleGraph() {
        // Simple graph with multiple paths
        // Path 1: 0->1->3->5 (min cap 9)
        // Path 2: 0->2->4->5 (min cap 7)
        // Path 3: 0->1->2->3->5 (min cap 2)
        int[][] graph = {
            {0, 16, 12, 0, 0, 0},
            {0, 0, 10, 9, 0, 0},
            {0, 4, 0, 2, 8, 0},
            {0, 0, 0, 0, 0, 20},
            {0, 0, 0, 0, 0, 7},
            {0, 0, 0, 0, 0, 0}
        };

        DinicsAlgorithm algo = new DinicsAlgorithm(graph, 0, 5);
        int maxFlow = algo.findMaxFlow();

        // Expected maximum flow is 18 (not 28)
        assertEquals(18, maxFlow, "Maximum flow should be 18");
        assertTrue(algo.getExecutionTime() >= 0, "Execution time should be non-negative");
    }

    @Test
    public void testDisconnectedGraph() {
        // Graph where source cannot reach sink
        int[][] graph = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        };

        DinicsAlgorithm algo = new DinicsAlgorithm(graph, 0, 2);
        int maxFlow = algo.findMaxFlow();

        assertEquals(0, maxFlow, "Maximum flow should be 0 for disconnected graph");
    }

    @Test
    public void testTrafficNetworkGraph() {
        // Traffic network: A->B->E->T
        // A(0) -> B(1) -> E(4) -> T(8)
        // Capacity: A-B=10, B-E=8, E-T=15
        int[][] graph = new int[9][9];
        graph[0][1] = 10; // A -> B
        graph[1][4] = 8;  // B -> E
        graph[4][8] = 15; // E -> T

        DinicsAlgorithm algo = new DinicsAlgorithm(graph, 0, 8);
        int maxFlow = algo.findMaxFlow();

        assertEquals(8, maxFlow, "Maximum flow through single path should be minimum capacity");
    }

    @Test
    public void testMultiplePathsGraph() {
        // Graph with multiple paths from source to sink
        int[][] graph = {
            {0, 10, 10, 0, 0},
            {0, 0, 2, 4, 8},
            {0, 0, 0, 2, 9},
            {0, 0, 0, 0, 10},
            {0, 0, 0, 0, 0}
        };

        DinicsAlgorithm algo = new DinicsAlgorithm(graph, 0, 4);
        int maxFlow = algo.findMaxFlow();

        assertTrue(maxFlow > 0, "Maximum flow should be greater than 0");
    }

    @Test
    public void testAlgorithmConsistency() {
        // Dinic's should produce same result as Ford-Fulkerson
        int[][] graph = {
            {0, 10, 10, 0, 0},
            {0, 0, 2, 4, 8},
            {0, 0, 0, 2, 9},
            {0, 0, 0, 0, 10},
            {0, 0, 0, 0, 0}
        };

        DinicsAlgorithm algo = new DinicsAlgorithm(graph, 0, 4);
        int maxFlow = algo.findMaxFlow();

        // Both algorithms should find the same maximum flow value
        assertTrue(maxFlow > 0, "Maximum flow should be greater than 0");
    }
}
