package com.example.scheduler;

import com.example.DotaTestFactory;
import com.example.entity.Match;
import com.example.parser.EventParserService;
import com.example.service.MatchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatchPayloadSchedulerTest {

    @Mock
    private MatchService matchService;

    @Mock
    private EventParserService eventParserService;

    @InjectMocks
    private MatchPayloadScheduler matchPayloadScheduler;

    @Test
    public void testIngestPayload() throws IOException {
        when(matchService.getAllMatchesPendingIngestion()).thenReturn(List.of(DotaTestFactory.createMatchWithPayload()));
        matchPayloadScheduler.ingestPayload();
        verify(eventParserService, times(1)).parsePayload(any(Match.class));
    }
}
