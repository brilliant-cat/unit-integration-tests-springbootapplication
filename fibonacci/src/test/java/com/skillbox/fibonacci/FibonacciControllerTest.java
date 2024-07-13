package com.skillbox.fibonacci;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FibonacciControllerTest extends PostgresTestContainerInitializer {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetNumberSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/fibonacci/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.index").value(10))
                .andExpect(jsonPath("$.value").value(55));
    }

    @Test
    public void testGetNumberBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/fibonacci/0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Index should be greater or equal to 1"));
    }

}
