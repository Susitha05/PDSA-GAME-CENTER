package com.example.TowerofHanoi.algorithms;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Hanoi3PegRecursiveTest {

    private Hanoi3PegRecursive algorithm;

    @BeforeEach
    void setUp() {
        algorithm = new Hanoi3PegRecursive();
    }

    @Test
    void testSolveWithOneDisks() {
        List<String> moves = algorithm.solve(1);
        assertEquals(1, moves.size());
        assertEquals(1, algorithm.getMoveCount());
    }

    @Test
    void testSolveWithTwoDisks() {
        List<String> moves = algorithm.solve(2);
        assertEquals(3, moves.size());
        assertEquals(3, algorithm.getMoveCount());
    }

    @Test
    void testSolveWithThreeDisks() {
        List<String> moves = algorithm.solve(3);
        assertEquals(7, moves.size());
        assertEquals(7, algorithm.getMoveCount());
    }

    @Test
    void testSolveWithFourDisks() {
        List<String> moves = algorithm.solve(4);
        assertEquals(15, moves.size());
        assertEquals(15, algorithm.getMoveCount());
    }

    @Test
    void testSolveWithFiveDisks() {
        List<String> moves = algorithm.solve(5);
        int expectedMoves = (int) Math.pow(2, 5) - 1;
        assertEquals(expectedMoves, moves.size());
        assertEquals(31, algorithm.getMoveCount());
    }

    @Test
    void testSolveWithSevenDisks() {
        List<String> moves = algorithm.solve(7);
        int expectedMoves = (int) Math.pow(2, 7) - 1;
        assertEquals(expectedMoves, moves.size());
        assertEquals(127, algorithm.getMoveCount());
    }

    @Test
    void testMoveSequenceFormat() {
        List<String> moves = algorithm.solve(3);
        for (String move : moves) {
            assertTrue(move.matches("[A-C] → [A-C]"));
        }
    }

    @Test
    void testFirstMoveFromA() {
        List<String> moves = algorithm.solve(3);
        assertTrue(moves.get(0).startsWith("A"));
    }

    @Test
    void testLastMoveToC() {
        List<String> moves = algorithm.solve(3);
        assertTrue(moves.get(moves.size() - 1).endsWith("C"));
    }

    @Test
    void testMultipleCallsClearPreviousMoves() {
        algorithm.solve(3);
        List<String> moves = algorithm.solve(2);
        assertEquals(3, moves.size());
    }

    @Test
    void testSolveWithTenDisks() {
        List<String> moves = algorithm.solve(10);
        int expectedMoves = (int) Math.pow(2, 10) - 1;
        assertEquals(expectedMoves, moves.size());
        assertEquals(1023, algorithm.getMoveCount());
    }

    @Test
    void testNoDuplicateConsecutiveMoves() {
        List<String> moves = algorithm.solve(5);
        for (int i = 0; i < moves.size() - 1; i++) {
            assertNotEquals(moves.get(i), moves.get(i + 1));
        }
    }

    @Test
    void testExecutionTime() {
        long startTime = System.nanoTime();
        algorithm.solve(10);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        assertTrue(duration > 0);
    }
}
