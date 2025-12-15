package com.snakeandladder.service;

import com.snakeandladder.model.Board;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GameServiceTest {

    @Autowired
    private GameService gameService;

    @Test
    public void testGenerateBoard() {
        int n = 10;
        Board board = gameService.generateBoard(n);
        
        assertNotNull(board);
        assertEquals(100, board.getTotalCells());
        assertEquals(8, board.getSnakes().size(), "Should have N-2 snakes");
        assertEquals(8, board.getLadders().size(), "Should have N-2 ladders");
        
        // Check Validity
        for (var s : board.getSnakes()) {
            assertTrue(s.getStart() > s.getEnd(), "Snake should go down");
        }
        for (var l : board.getLadders()) {
             assertTrue(l.getStart() < l.getEnd(), "Ladder should go up");
        }
    }
}
