package com.example.parser;

import com.example.DotaTestFactory;
import com.example.entity.Match;
import com.example.repository.MatchRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EventParserServiceTest {

    @Mock
    private BuyEventParser buyEventParser;

    @Mock
    private CastEventParser castEventParser;

    @Mock
    private HealEventParser healEventParser;

    @Mock
    private HitEventParser hitEventParser;

    @Mock
    private KillEventParser killEventParser;

    @Mock
    private UseEventParser useEventParser;

    @Mock
    private GameStateEventParser gameStateEventParser;

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private EventParserService eventParserService;

    @Test
    public void testParsePayload() throws IOException {
        Match match = DotaTestFactory.createMatchWithPayload();
        eventParserService.parsePayload(match);
        Assert.assertTrue(match.getIsIngested());
        verify(buyEventParser, times(323)).parseEvent(anyString(), any(Match.class));
        verify(castEventParser, times(848)).parseEvent(anyString(), any(Match.class));
        verify(healEventParser, times(715)).parseEvent(anyString(), any(Match.class));
        verify(hitEventParser, times(2558)).parseEvent(anyString(), any(Match.class));
        verify(killEventParser, times(507)).parseEvent(anyString(), any(Match.class));
        verify(useEventParser, times(607)).parseEvent(anyString(), any(Match.class));
        verify(gameStateEventParser, times(8)).parseEvent(anyString(), any(Match.class));
        verify(matchRepository, times(1)).save(any(Match.class));
    }
}
