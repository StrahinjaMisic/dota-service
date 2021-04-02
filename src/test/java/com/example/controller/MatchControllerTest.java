package com.example.controller;

import com.example.DotaTestFactory;
import com.example.exception.GlobalExceptionHandler;
import com.example.model.MatchStatsModel;
import com.example.service.MatchService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import java.util.List;

import static com.example.DotaTestFactory.MATCH_ID;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MatchControllerTest {

    @Mock
    private MatchService matchService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new MatchController(matchService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    public void testIngestMatch() throws Exception {
        when(matchService.ingestMatch(anyString())).thenReturn(DotaTestFactory.createSimpleMatchModel());
        this.mockMvc.perform(post("/matches")
                .contentType(MediaType.TEXT_PLAIN)
                .content(DotaTestFactory.getPayload()))
                .andExpect(status().isOk());
    }

    @Test
    public void testIngestMatchWhenPayloadIsEmptyOrNull() throws Exception {
        this.mockMvc.perform(post("/matches")
                .contentType(MediaType.TEXT_PLAIN)
                .content(StringUtils.EMPTY))
                .andExpect(status().isBadRequest());

        this.mockMvc.perform(post("/matches")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testIngestMatchWhenMatchIsAlreadyIngested() throws Exception {
        when(matchService.ingestMatch(anyString())).thenThrow(EntityExistsException.class);
        this.mockMvc.perform(post("/matches")
                .contentType(MediaType.TEXT_PLAIN)
                .content(DotaTestFactory.getPayload()))
                .andExpect(status().isConflict());
    }

    @Test
    public void testGetMatches() throws Exception {
        when(matchService.getMatches(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(DotaTestFactory.createSimpleMatchModel())));
        this.mockMvc.perform(get("/matches")).andExpect(status().isOk());
    }

    @Test
    public void testGetMatchStats() throws Exception {
        when(matchService.getMatchStats(anyLong())).thenReturn(new MatchStatsModel());
        this.mockMvc.perform(get("/matches/" + MATCH_ID)).andExpect(status().isOk());
    }

    @Test
    public void testGetMatchStatsWhenMatchIsNotFound() throws Exception {
        when(matchService.getMatchStats(anyLong())).thenThrow(NoResultException.class);
        this.mockMvc.perform(get("/matches/" + MATCH_ID)).andExpect(status().isNotFound());
    }
}
