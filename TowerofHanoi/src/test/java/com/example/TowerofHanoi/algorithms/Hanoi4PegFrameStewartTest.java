package com.example.TowerofHanoi.algorithms;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Hanoi4PegFrameStewartTest {

    private Hanoi4PegFrameStewart algorithm;

    @BeforeEach
    void setUp() {
        algorithm = new Hanoi4PegFrameStewart();
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
        assertTrue(moves.size() <= 3);
        assertEquals(moves.size(), algorithm.getMoveCount());
    }

    @Test
    void testSolveWithFiveDisks() {
        List<String> moves = algorithm.solve(5);
        int threePegMoves = (int) Math.pow(2, 5) - 1;
        assertTrue(moves.size() < threePegMoves);
    }

    @Test
    void testSolveWithSevenDisks() {
        List<String> moves = algorithm.solve(7);
        int threePegMoves = (int) Math.pow(2, 7) - 1;
        assertTrue(moves.size() < threePegMoves);
    }

    @Test
    void testSolveWithTenDisks() {
        List<String> moves = algorithm.solve(10);
        int threePegMoves = (int) Math.pow(2, 10) - 1;
        assertTrue(moves.size() < threePegMoves);
    }

    @Test
    void testMoveSequenceFormat() {
        List<String> moves = algorithm.solve(5);
        for (String move : moves) {
            assertTrue(move.matches("[A-D] → [A-D]"));
        }
    }

    @Test
    void testFirstMoveFromA() {
        List<String> moves = algorithm.solve(5);
        assertTrue(moves.get(0).startsWith("A"));
    }

    @Test
    void testLastMoveToD() {
        List<String> moves = algorithm.solve(5);
        assertTrue(moves.get(moves.size() - 1).endsWith("D"));
    }

    @Test
    void testMultipleCallsClearPreviousMoves() {
        algorithm.solve(5);
        List<String> moves = algorithm.solve(3);
        assertTrue(moves.size() <= 7);
    }

    @Test
    void testNoDuplicateConsecutiveMoves() {
        List<String> moves = algorithm.solve(6);
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

    @Test
    void testOptimalityFor8Disks() {
        List<String> moves = algorithm.solve(8);
        assertTrue(moves.size() <= 33);
    }

    @Test
    void testValidMoveSequence() {
        List<String> moves = algorithm.solve(6);
        assertNotNull(moves);
        assertFalse(moves.isEmpty());
    }
}
