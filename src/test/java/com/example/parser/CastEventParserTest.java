package com.example.parser;

import com.example.DotaTestFactory;
import com.example.entity.Match;
import com.example.service.HeroService;
import com.example.service.SpellService;
import com.example.util.TimeUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CastEventParserTest {

    @Mock
    private HeroService heroService;

    @Mock
    private SpellService spellService;

    @Mock
    private TimeUtil timeUtil;

    @InjectMocks
    private CastEventParser castEventParser;

    @Test
    public void testParseEvent() {
        String heroName = "snapfire";
        String spellName = "snapfire_gobble_up";
        Match match = DotaTestFactory.createMatch();
        when(heroService.getOrCreate(anyString())).thenReturn(DotaTestFactory.createHero(heroName));
        when(spellService.getOrCreate(anyString())).thenReturn(DotaTestFactory.createSpell(spellName));
        castEventParser.parseEvent("[00:24:10.567] npc_dota_hero_snapfire casts ability snapfire_gobble_up " +
                "(lvl 1) on npc_dota_hero_pangolier", match);
        verify(heroService, times(1)).getOrCreate(heroName);
        verify(spellService, times(1)).getOrCreate(spellName);
        verify(timeUtil, times(1)).eventTimeToMilliseconds("00:24:10.567");
    }

    @Test
    public void testParseEventWhenEventIsIgnored() {
        Match match = DotaTestFactory.createMatch();
        castEventParser.parseEvent("[00:24:11.367] npc_dota_hero_snapfire casts ability snapfire_spit_creep " +
                "(lvl 1) on dota_unknown", match);
        verify(heroService, never()).getOrCreate(anyString());
        verify(spellService, never()).getOrCreate(anyString());
        verify(timeUtil, never()).eventTimeToMilliseconds(anyString());
    }
}
