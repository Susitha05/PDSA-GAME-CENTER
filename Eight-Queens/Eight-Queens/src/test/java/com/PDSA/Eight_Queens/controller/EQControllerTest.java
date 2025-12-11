package com.PDSA.Eight_Queens.controller;

import com.PDSA.Eight_Queens.dto.ComputationResultDTO;
import com.PDSA.Eight_Queens.dto.GameSubmissionRequest;
import com.PDSA.Eight_Queens.dto.GameSubmissionResponse;
import com.PDSA.Eight_Queens.service.EQService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EQController.class)
class EQControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EQService eqService;

    @Test
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/api/eight-queens/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Eight Queens API is running"));
    }

    @Test
    void testComputeSequential() throws Exception {
        ComputationResultDTO result = new ComputationResultDTO("SEQUENTIAL", 92, 1500L, "2025-12-09T00:00:00");
        when(eqService.findAllSolutionsSequential()).thenReturn(result);

        mockMvc.perform(post("/api/eight-queens/compute/sequential"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.computationType").value("SEQUENTIAL"))
                .andExpect(jsonPath("$.totalSolutions").value(92))
                .andExpect(jsonPath("$.timeTakenMs").value(1500));
    }

    @Test
    void testComputeThreaded() throws Exception {
        ComputationResultDTO result = new ComputationResultDTO("THREADED", 92, 800L, "2025-12-09T00:00:00");
        when(eqService.findAllSolutionsThreaded()).thenReturn(result);

        mockMvc.perform(post("/api/eight-queens/compute/threaded"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.computationType").value("THREADED"))
                .andExpect(jsonPath("$.totalSolutions").value(92))
                .andExpect(jsonPath("$.timeTakenMs").value(800));
    }

    @Test
    void testSubmitSolution_Valid() throws Exception {
        GameSubmissionRequest request = new GameSubmissionRequest("John", new int[] { 0, 4, 7, 5, 2, 6, 1, 3 }, 30, 15);
        GameSubmissionResponse response = new GameSubmissionResponse(true, "Success!", false, 1, 92);

        when(eqService.submitSolution(any(GameSubmissionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/eight-queens/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correct").value(true))
                .andExpect(jsonPath("$.alreadyFound").value(false));
    }

    @Test
    void testSubmitSolution_InvalidName() throws Exception {
        GameSubmissionRequest request = new GameSubmissionRequest("", new int[] { 0, 4, 7, 5, 2, 6, 1, 3 }, 30, 15);

        mockMvc.perform(post("/api/eight-queens/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetStatistics() throws Exception {
        EQService.GameStatistics stats = new EQService.GameStatistics(10, 5, 92, 0);
        when(eqService.getGameStatistics()).thenReturn(stats);

        mockMvc.perform(get("/api/eight-queens/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSubmissions").value(10))
                .andExpect(jsonPath("$.uniqueSolutionsFound").value(5))
                .andExpect(jsonPath("$.totalPossibleSolutions").value(92));
    }

    @Test
    void testGetAllComputations() throws Exception {
        List<ComputationResultDTO> results = Arrays.asList(
                new ComputationResultDTO("SEQUENTIAL", 92, 1500L, "2025-12-09T00:00:00"),
                new ComputationResultDTO("THREADED", 92, 800L, "2025-12-09T00:00:00"));
        when(eqService.getAllComputationResults()).thenReturn(results);

        mockMvc.perform(get("/api/eight-queens/computations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].computationType").value("SEQUENTIAL"))
                .andExpect(jsonPath("$[1].computationType").value("THREADED"));
    }

    @Test
    void testResetGame() throws Exception {
        mockMvc.perform(delete("/api/eight-queens/reset"))
                .andExpect(status().isOk())
                .andExpect(content().string("Game reset successfully. All player solutions cleared."));
    }
}
