package com.example.TowerofHanoi.controller;

import com.example.TowerofHanoi.dto.HanoiStartResponse;
import com.example.TowerofHanoi.dto.HanoiSubmitRequest;
import com.example.TowerofHanoi.dto.HanoiSubmitResponse;
import com.example.TowerofHanoi.service.HanoiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HanoiController.class)
class HanoiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HanoiService hanoiService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testStartNewRound() throws Exception {
        HanoiStartResponse mockResponse = new HanoiStartResponse();
        mockResponse.setRoundId("test-round-id");
        mockResponse.setDiskCount(7);
        mockResponse.setPegCount(3);

        when(hanoiService.startNewRound()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/hanoi/start"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.roundId").value("test-round-id"))
                .andExpect(jsonPath("$.diskCount").value(7))
                .andExpect(jsonPath("$.pegCount").value(3));
    }

    @Test
    void testSubmitRoundWithValidData() throws Exception {
        List<String> moves = Arrays.asList("A → C", "A → B", "C → B");
        HanoiSubmitRequest request = new HanoiSubmitRequest();
        request.setRoundId("test-round-id");
        request.setMoves(moves);

        HanoiSubmitResponse mockResponse = new HanoiSubmitResponse();
        mockResponse.setIsValid(true);
        mockResponse.setMessage("Success! Valid solution.");
        mockResponse.setUserMoveCount(3);
        mockResponse.setAlgorithmResults(new HashMap<>());

        when(hanoiService.submitRound(any(HanoiSubmitRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/hanoi/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isValid").value(true))
                .andExpect(jsonPath("$.message").value("Success! Valid solution."))
                .andExpect(jsonPath("$.userMoveCount").value(3));
    }

    @Test
    void testSubmitRoundWithInvalidData() throws Exception {
        List<String> moves = Arrays.asList("A → B", "A → B");
        HanoiSubmitRequest request = new HanoiSubmitRequest();
        request.setRoundId("test-round-id");
        request.setMoves(moves);

        HanoiSubmitResponse mockResponse = new HanoiSubmitResponse();
        mockResponse.setIsValid(false);
        mockResponse.setMessage("Invalid move sequence");
        mockResponse.setUserMoveCount(2);

        when(hanoiService.submitRound(any(HanoiSubmitRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/hanoi/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isValid").value(false))
                .andExpect(jsonPath("$.message").value("Invalid move sequence"));
    }

    @Test
    void testSubmitRoundWithNullRequest() throws Exception {
        mockMvc.perform(post("/api/hanoi/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSubmitRoundWithMissingRoundId() throws Exception {
        List<String> moves = Arrays.asList("A → B");
        HanoiSubmitRequest request = new HanoiSubmitRequest();
        request.setMoves(moves);

        mockMvc.perform(post("/api/hanoi/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSubmitRoundWithMissingMoves() throws Exception {
        HanoiSubmitRequest request = new HanoiSubmitRequest();
        request.setRoundId("test-round-id");

        mockMvc.perform(post("/api/hanoi/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testStartEndpointContentType() throws Exception {
        HanoiStartResponse mockResponse = new HanoiStartResponse();
        mockResponse.setRoundId("test-id");
        mockResponse.setDiskCount(5);
        mockResponse.setPegCount(3);

        when(hanoiService.startNewRound()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/hanoi/start"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testSubmitEndpointContentType() throws Exception {
        List<String> moves = Arrays.asList("A → C");
        HanoiSubmitRequest request = new HanoiSubmitRequest();
        request.setRoundId("test-id");
        request.setMoves(moves);

        HanoiSubmitResponse mockResponse = new HanoiSubmitResponse();
        mockResponse.setIsValid(true);
        mockResponse.setMessage("Success");
        mockResponse.setUserMoveCount(1);
        mockResponse.setAlgorithmResults(new HashMap<>());

        when(hanoiService.submitRound(any(HanoiSubmitRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/hanoi/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCorsEnabled() throws Exception {
        HanoiStartResponse mockResponse = new HanoiStartResponse();
        mockResponse.setRoundId("test-id");
        mockResponse.setDiskCount(5);
        mockResponse.setPegCount(3);

        when(hanoiService.startNewRound()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/hanoi/start")
                        .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk());
    }
}
