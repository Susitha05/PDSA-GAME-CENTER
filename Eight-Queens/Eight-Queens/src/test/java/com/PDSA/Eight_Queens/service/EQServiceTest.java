package com.PDSA.Eight_Queens.service;

import com.PDSA.Eight_Queens.data.*;
import com.PDSA.Eight_Queens.dto.GameSubmissionRequest;
import com.PDSA.Eight_Queens.dto.GameSubmissionResponse;
import com.PDSA.Eight_Queens.exception.InvalidSolutionException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EQServiceTest {

    @Mock
    private EightQueensRepository gameRepository;

    @Mock
    private ComputationResultRepository computationRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private EQService eqService;

    private int[] validSolution;
    private int[] invalidSolution;

    @BeforeEach
    void setUp() {
        validSolution = new int[] { 0, 4, 7, 5, 2, 6, 1, 3 }; // Valid 8-Queens solution
        invalidSolution = new int[] { 0, 1, 2, 3, 4, 5, 6, 7 }; // Queens attacking each other
    }

    @Test
    void testIsValidSolution_ValidBoard() {
        assertTrue(eqService.isValidSolution(validSolution));
    }

    @Test
    void testIsValidSolution_InvalidBoard_Attacking() {
        assertFalse(eqService.isValidSolution(invalidSolution));
    }

    @Test
    void testIsValidSolution_NullBoard() {
        assertFalse(eqService.isValidSolution(null));
    }

    @Test
    void testIsValidSolution_InvalidSize() {
        int[] wrongSize = new int[] { 0, 4, 7, 5, 2 };
        assertFalse(eqService.isValidSolution(wrongSize));
    }

    @Test
    void testIsValidSolution_OutOfRange() {
        int[] outOfRange = new int[] { 0, 4, 7, 5, 2, 6, 1, 9 };
        assertFalse(eqService.isValidSolution(outOfRange));
    }

    @Test
    void testSubmitSolution_ValidNewSolution() throws Exception {
        GameSubmissionRequest request = new GameSubmissionRequest("John", validSolution, 30, 15);
        String boardJson = "[0,4,7,5,2,6,1,3]";

        when(objectMapper.writeValueAsString(validSolution)).thenReturn(boardJson);
        when(gameRepository.existsByBoard(boardJson)).thenReturn(false);
        when(gameRepository.countDistinctBoards()).thenReturn(1L);
        when(gameRepository.save(any(EightQueens.class))).thenReturn(new EightQueens());

        GameSubmissionResponse response = eqService.submitSolution(request);

        assertTrue(response.isCorrect());
        assertFalse(response.isAlreadyFound());
        assertEquals(2, response.getTotalFoundSolutions()); // 1 + 1
        verify(gameRepository, times(1)).save(any(EightQueens.class));
    }

    @Test
    void testSubmitSolution_ValidDuplicateSolution() throws Exception {
        GameSubmissionRequest request = new GameSubmissionRequest("Jane", validSolution, 45, 20);
        String boardJson = "[0,4,7,5,2,6,1,3]";

        when(objectMapper.writeValueAsString(validSolution)).thenReturn(boardJson);
        when(gameRepository.existsByBoard(boardJson)).thenReturn(true);
        when(gameRepository.countDistinctBoards()).thenReturn(5L);
        when(gameRepository.save(any(EightQueens.class))).thenReturn(new EightQueens());

        GameSubmissionResponse response = eqService.submitSolution(request);

        assertTrue(response.isCorrect());
        assertTrue(response.isAlreadyFound());
        assertEquals(5, response.getTotalFoundSolutions());
        verify(gameRepository, times(1)).save(any(EightQueens.class));
    }

    @Test
    void testSubmitSolution_InvalidSolution() {
        GameSubmissionRequest request = new GameSubmissionRequest("Bob", invalidSolution, 60, 25);

        assertThrows(InvalidSolutionException.class, () -> {
            eqService.submitSolution(request);
        });

        verify(gameRepository, never()).save(any(EightQueens.class));
    }

    @Test
    void testFindAllSolutionsSequential() {
        when(computationRepository.save(any(ComputationResult.class)))
                .thenReturn(new ComputationResult("SEQUENTIAL", 92, 1000L));

        var result = eqService.findAllSolutionsSequential();

        assertNotNull(result);
        assertEquals("SEQUENTIAL", result.getComputationType());
        assertEquals(92, result.getTotalSolutions());
        assertTrue(result.getTimeTakenMs() >= 0);
        verify(computationRepository, times(1)).save(any(ComputationResult.class));
    }

    @Test
    void testGetGameStatistics() {
        when(gameRepository.count()).thenReturn(10L);
        when(gameRepository.countDistinctBoards()).thenReturn(5L);

        var stats = eqService.getGameStatistics();

        assertNotNull(stats);
        assertEquals(10, stats.getTotalSubmissions());
        assertEquals(5, stats.getUniqueSolutionsFound());
        assertEquals(92, stats.getTotalPossibleSolutions());
    }

    @Test
    void testResetGame() {
        doNothing().when(gameRepository).deleteAll();

        eqService.resetGame();

        verify(gameRepository, times(1)).deleteAll();
    }
}
