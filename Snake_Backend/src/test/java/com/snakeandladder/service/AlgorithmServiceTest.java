package com.snakeandladder.service;

import com.snakeandladder.model.Board;
import com.snakeandladder.model.Ladder;
import com.snakeandladder.model.Snake;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AlgorithmServiceTest {

    @Autowired
    private AlgorithmService algorithmService;

    @Test
    public void testShortestPathWithLadder() {
        Board board = new Board();
        board.setSize(10);
        board.setTotalCells(100);
        
        // Ladder from 2 to 90
        Ladder ladder = new Ladder(2, 90);
        board.setLadders(Collections.singletonList(ladder));
        board.setSnakes(Collections.emptyList());

        // Expected: 1 -> 2(jumps to 90) -> 91..96 -> 100
        // Path: 1 --(roll 1)--> 2->90.
        // 90 --(roll 6)--> 96
        // 96 --(roll 4)--> 100
        // Wait: 90 -> 96 (1 throw), 96 -> 100 (1 throw). Total 3 throws?
        // 1->90 (1), 90->96 (2), 96->100 (3)?
        // Or 90 -> 100 is 10 steps. 6+4 = 2 throws.
        // Total: 1 (start to 90) + 2 (90 to 100) = 3 throws.
        
        // Better case: 1 -> (roll 1) -> 2 -> 94. 
        // 94 -> 100 (6). Total 2 throws.
        // Let's set Ladder 2 -> 94.
        
        board.setLadders(Collections.singletonList(new Ladder(2, 94)));
        
        // 1 -> 2(94) [throw 1]
        // 94 -> 100 [throw 1]
        // Total 2.
        
        int bfs = algorithmService.bfs(board);
        int dijkstra = algorithmService.dijkstra(board);
        int aStar = algorithmService.aStar(board);

        assertEquals(2, bfs, "BFS should find 2 throws");
        assertEquals(2, dijkstra, "Dijkstra should find 2 throws");
        assertEquals(2, aStar, "A* should find 2 throws");
    }

    @Test
    public void testNoSnakesLadders() {
        Board board = new Board();
        board.setSize(10); // 100 cells
        board.setTotalCells(100);
        board.setSnakes(Collections.emptyList());
        board.setLadders(Collections.emptyList());

        // 1->100 is 99 steps.
        // 99 / 6 = 16.5 -> 17 throws.
        
        assertEquals(17, algorithmService.bfs(board));
    }
}
