package com.example.parser;

import com.example.DotaTestFactory;
import com.example.entity.Match;
import com.example.service.HeroService;
import com.example.util.TimeUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KillEventParserTest {

    @Mock
    private HeroService heroService;

    @Mock
    private TimeUtil timeUtil;

    @InjectMocks
    private KillEventParser killEventParser;

    @Test
    public void testParseEvent() {
        String heroName = "death_prophet";
        String targetHeroName = "pangolier";
        Match match = DotaTestFactory.createMatch();
        when(heroService.getOrCreate(anyString())).thenReturn(DotaTestFactory.createHero(heroName),
                DotaTestFactory.createHero(targetHeroName));
        killEventParser.parseEvent("[00:25:14.951] npc_dota_hero_pangolier is killed by " +
                "npc_dota_hero_death_prophet", match);
        verify(heroService, times(1)).getOrCreate(heroName);
        verify(heroService, times(1)).getOrCreate(targetHeroName);
        verify(timeUtil, times(1)).eventTimeToMilliseconds("00:25:14.951");
    }

    @Test
    public void testParseEventWhenEventIsIgnored() {
        Match match = DotaTestFactory.createMatch();
        killEventParser.parseEvent("[00:24:29.496] npc_dota_neutral_polar_furbolg_champion is killed by " +
                "npc_dota_hero_abyssal_underlord", match);
        verify(heroService, never()).getOrCreate(anyString());
        verify(timeUtil, never()).eventTimeToMilliseconds(anyString());
    }
}
