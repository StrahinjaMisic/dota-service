package com.example.parser;

import com.example.DotaTestFactory;
import com.example.entity.Match;
import com.example.util.TimeUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameStateEventParserTest {

    @Mock
    private TimeUtil timeUtil;

    @InjectMocks
    private GameStateEventParser gameStateEventParser;

    @Test
    public void testParseEvent() {
        Match match = DotaTestFactory.createMatch();
        gameStateEventParser.parseEvent("[00:37:22.851] game state is now 6", match);
        verify(timeUtil, times(1)).eventTimeToMilliseconds("00:37:22.851");
    }

    @Test
    public void testParseEventWhenEventIsIgnored() {
        Match match = DotaTestFactory.createMatch();
        gameStateEventParser.parseEvent("[00:08:41.094] game state is now 4", match);
        verify(timeUtil, never()).eventTimeToMilliseconds(anyString());
    }
}
