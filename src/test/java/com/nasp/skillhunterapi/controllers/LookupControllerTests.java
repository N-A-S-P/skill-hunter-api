package com.nasp.skillhunterapi.controllers;

import com.nasp.skillhunterapi.dto.LookupResponse;
import com.nasp.skillhunterapi.service.LookupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LookupController.class)
public class LookupControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LookupService lookupService;

    @Test
    @DisplayName("GET /api/lookup/{lookupType} should return mapped lookup values")
    void happyPath() throws Exception {
        when(lookupService.getLookup("testLookup"))
                .thenReturn(List.of(
                        new LookupResponse("TYPE_1", "Type 1"),
                        new LookupResponse("TYPE_2", "Type 2")
                ));

        mockMvc.perform(get("/api/lookup/testLookup"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].value").value("TYPE_1"))
                .andExpect(jsonPath("$[0].display").value("Type 1"));
    }
}
