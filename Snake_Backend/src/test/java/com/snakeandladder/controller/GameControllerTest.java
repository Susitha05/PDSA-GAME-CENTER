package com.snakeandladder.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testStartGameSuccess() throws Exception {
        mockMvc.perform(post("/api/game/start").param("n", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId").exists())
                .andExpect(jsonPath("$.board").exists())
                .andExpect(jsonPath("$.choices").isArray());
    }

    @Test
    public void testStartGameInvalidSize() throws Exception {
        mockMvc.perform(post("/api/game/start").param("n", "5"))
                .andExpect(status().isBadRequest()); // 5 < 6
        
        mockMvc.perform(post("/api/game/start").param("n", "13"))
                .andExpect(status().isBadRequest()); // 13 > 12
    }

    @Test
    public void testStatsEndpoint() throws Exception {
        mockMvc.perform(get("/api/game/stats"))
                .andExpect(status().isOk());
    }
}
