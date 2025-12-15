package com.example.TowerofHanoi.service;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.TowerofHanoi.dto.HanoiStartResponse;
import com.example.TowerofHanoi.dto.HanoiSubmitRequest;
import com.example.TowerofHanoi.dto.HanoiSubmitResponse;
import com.example.TowerofHanoi.entity.HanoiGameRound;
import com.example.TowerofHanoi.repository.HanoiRepository;

@ExtendWith(MockitoExtension.class)
class HanoiServiceTest {

    @Mock
    private HanoiRepository hanoiRepository;

    @InjectMocks
    private HanoiService hanoiService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testStartNewRound() {
        HanoiStartResponse response = hanoiService.startNewRound();
        
        assertNotNull(response);
        assertNotNull(response.getRoundId());
        assertNotNull(response.getDiskCount());
        assertNotNull(response.getPegCount());
        assertTrue(response.getDiskCount() >= 5);
        assertTrue(response.getDiskCount() <= 10);
        assertEquals(3, response.getPegCount());
    }

    @Test
    void testStartMultipleRoundsGenerateUniqueIds() {
        HanoiStartResponse response1 = hanoiService.startNewRound();
        HanoiStartResponse response2 = hanoiService.startNewRound();
        
        assertNotEquals(response1.getRoundId(), response2.getRoundId());
    }

    @Test
    void testSubmitRoundWithValidMoves() {
        List<String> validMoves = Arrays.asList(
            "A → C", "A → B", "C → B",
            "A → C", "B → A", "B → C", "A → C"
        );
        
        HanoiSubmitRequest request = new HanoiSubmitRequest();
        request.setRoundId("test-round-id");
        request.setMoves(validMoves);
        
        when(hanoiRepository.save(any(HanoiGameRound.class))).thenReturn(new HanoiGameRound());
        
        HanoiSubmitResponse response = hanoiService.submitRound(request);
        
        assertNotNull(response);
        assertEquals(7, response.getUserMoveCount());
        assertNotNull(response.getAlgorithmResults());
        verify(hanoiRepository, times(1)).save(any(HanoiGameRound.class));
    }

    @Test
    void testSubmitRoundWithNullRequest() {
        HanoiSubmitResponse response = hanoiService.submitRound(null);
        
        assertNotNull(response);
        assertFalse(response.getIsValid());
        assertTrue(response.getMessage().contains("Invalid request"));
    }

    @Test
    void testSubmitRoundWithNullRoundId() {
        HanoiSubmitRequest request = new HanoiSubmitRequest();
        request.setMoves(Arrays.asList("A → B"));
        
        HanoiSubmitResponse response = hanoiService.submitRound(request);
        
        assertNotNull(response);
        assertFalse(response.getIsValid());
    }

    @Test
    void testSubmitRoundWithNullMoves() {
        HanoiSubmitRequest request = new HanoiSubmitRequest();
        request.setRoundId("test-id");
        
        HanoiSubmitResponse response = hanoiService.submitRound(request);
        
        assertNotNull(response);
        assertFalse(response.getIsValid());
    }

    @Test
    void testSubmitRoundWithInvalidMove() {
        List<String> invalidMoves = Arrays.asList(
            "A → B", "A → B"
        );
        
        HanoiSubmitRequest request = new HanoiSubmitRequest();
        request.setRoundId("test-round-id");
        request.setMoves(invalidMoves);
        
        HanoiSubmitResponse response = hanoiService.submitRound(request);
        
        assertNotNull(response);
        assertFalse(response.getIsValid());
    }

    @Test
    void testAlgorithmResultsContainAllAlgorithms() {
        List<String> validMoves = Arrays.asList(
            "A → C", "A → B", "C → B",
            "A → C", "B → A", "B → C", "A → C"
        );
        
        HanoiSubmitRequest request = new HanoiSubmitRequest();
        request.setRoundId("test-round-id");
        request.setMoves(validMoves);
        
        when(hanoiRepository.save(any(HanoiGameRound.class))).thenReturn(new HanoiGameRound());
        
        HanoiSubmitResponse response = hanoiService.submitRound(request);
        
        assertNotNull(response.getAlgorithmResults());
        assertTrue(response.getAlgorithmResults().containsKey("recursive3Peg"));
        assertTrue(response.getAlgorithmResults().containsKey("iterative3Peg"));
        assertTrue(response.getAlgorithmResults().containsKey("frameStewart4Peg"));
        assertTrue(response.getAlgorithmResults().containsKey("dynamic4Peg"));
    }

    @Test
    void testAlgorithmResultsHaveExecutionTime() {
        List<String> validMoves = Arrays.asList(
            "A → C", "A → B", "C → B",
            "A → C", "B → A", "B → C", "A → C"
        );
        
        HanoiSubmitRequest request = new HanoiSubmitRequest();
        request.setRoundId("test-round-id");
        request.setMoves(validMoves);
        
        when(hanoiRepository.save(any(HanoiGameRound.class))).thenReturn(new HanoiGameRound());
        
        HanoiSubmitResponse response = hanoiService.submitRound(request);
        
        HanoiSubmitResponse.AlgorithmResult recursive = response.getAlgorithmResults().get("recursive3Peg");
        assertNotNull(recursive.getExecutionTimeNanos());
        assertTrue(recursive.getExecutionTimeNanos() > 0);
        assertNotNull(recursive.getExecutionTimeMs());
    }

    @Test
    void testAlgorithmResultsHaveMoveSequences() {
        List<String> validMoves = Arrays.asList(
            "A → C", "A → B", "C → B",
            "A → C", "B → A", "B → C", "A → C"
        );
        
        HanoiSubmitRequest request = new HanoiSubmitRequest();
        request.setRoundId("test-round-id");
        request.setMoves(validMoves);
        
        when(hanoiRepository.save(any(HanoiGameRound.class))).thenReturn(new HanoiGameRound());
        
        HanoiSubmitResponse response = hanoiService.submitRound(request);
        
        HanoiSubmitResponse.AlgorithmResult recursive = response.getAlgorithmResults().get("recursive3Peg");
        assertNotNull(recursive.getSequence());
        assertFalse(recursive.getSequence().isEmpty());
    }

    @Test
    void testDatabaseSaveOnValidSubmission() {
        List<String> validMoves = Arrays.asList(
            "A → C", "A → B", "C → B",
            "A → C", "B → A", "B → C", "A → C"
        );
        
        HanoiSubmitRequest request = new HanoiSubmitRequest();
        request.setRoundId("test-round-id");
        request.setMoves(validMoves);
        
        when(hanoiRepository.save(any(HanoiGameRound.class))).thenReturn(new HanoiGameRound());
        
        hanoiService.submitRound(request);
        
        verify(hanoiRepository, times(1)).save(any(HanoiGameRound.class));
    }

    @Test
    void testNoDatabaseSaveOnInvalidSubmission() {
        HanoiSubmitRequest request = new HanoiSubmitRequest();
        request.setRoundId(null);
        request.setMoves(Arrays.asList("A → B"));
        
        hanoiService.submitRound(request);
        
        verify(hanoiRepository, never()).save(any(HanoiGameRound.class));
    }
}
