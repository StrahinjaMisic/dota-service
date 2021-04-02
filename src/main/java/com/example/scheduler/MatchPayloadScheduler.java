package com.example.scheduler;

import com.example.entity.Match;
import com.example.parser.EventParserService;
import com.example.service.MatchService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class MatchPayloadScheduler {

    private static final long INITIAL_DELAY = 1000;

    private static final long FIXED_RATE = 1000;

    private final MatchService matchService;

    private final EventParserService eventParserService;

    public MatchPayloadScheduler(
            MatchService matchService,
            EventParserService eventParserService
    ) {
        this.matchService = matchService;
        this.eventParserService = eventParserService;
    }

    @Transactional
    @Scheduled(initialDelay = INITIAL_DELAY, fixedRate = FIXED_RATE)
    public void ingestPayload() {
        List<Match> matches = matchService.getAllMatchesPendingIngestion();
        for (Match match : matches) {
            eventParserService.parsePayload(match);
        }
    }
}
