package com.example.parser;

import com.example.DotaTestFactory;
import com.example.entity.Match;
import com.example.service.HeroService;
import com.example.service.ItemService;
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
public class HealEventParserTest {

    @Mock
    private HeroService heroService;

    @Mock
    private SpellService spellService;

    @Mock
    private ItemService itemService;

    @Mock
    private TimeUtil timeUtil;

    @InjectMocks
    private HealEventParser healEventParser;

    @Test
    public void testParseEventWhenHealIsPerformedWithItem() {
        String heroName = "death_prophet";
        String itemName = "bottle";
        Match match = DotaTestFactory.createMatch();
        when(heroService.getOrCreate(anyString())).thenReturn(DotaTestFactory.createHero(heroName),
                DotaTestFactory.createHero(heroName));
        when(itemService.getOrCreate(anyString())).thenReturn(DotaTestFactory.createItem(itemName));
        healEventParser.parseEvent("[00:13:55.217] npc_dota_hero_death_prophet's item_bottle heals " +
                "npc_dota_hero_death_prophet for 95 health (865->960)", match);
        verify(heroService, times(2)).getOrCreate(heroName);
        verify(itemService, times(1)).getOrCreate(itemName);
        verify(spellService, never()).getOrCreate(anyString());
        verify(timeUtil, times(1)).eventTimeToMilliseconds("00:13:55.217");
    }

    @Test
    public void testParseEventWhenHealIsPerformedWithSpell() {
        String heroName = "bloodseeker";
        String targetHeroName = "bane";
        String spellName = "bloodseeker_bloodrage";
        Match match = DotaTestFactory.createMatch();
        when(heroService.getOrCreate(anyString())).thenReturn(DotaTestFactory.createHero(heroName),
                DotaTestFactory.createHero(targetHeroName));
        when(spellService.getOrCreate(anyString())).thenReturn(DotaTestFactory.createSpell(spellName));
        healEventParser.parseEvent("[00:17:29.432] npc_dota_hero_bloodseeker's bloodseeker_bloodrage heals " +
                "npc_dota_hero_bane for 180 health (626->806)", match);
        verify(heroService, times(1)).getOrCreate(heroName);
        verify(heroService, times(1)).getOrCreate(targetHeroName);
        verify(spellService, times(1)).getOrCreate(spellName);
        verify(itemService, never()).getOrCreate(anyString());
        verify(timeUtil, times(1)).eventTimeToMilliseconds("00:17:29.432");
    }

    @Test
    public void testParseEventWhenHealIsWithDotaUnknown() {
        String heroName = "snapfire";
        Match match = DotaTestFactory.createMatch();
        when(heroService.getOrCreate(anyString())).thenReturn(DotaTestFactory.createHero(heroName),
                DotaTestFactory.createHero(heroName));
        healEventParser.parseEvent("[00:25:40.345] npc_dota_hero_snapfire's dota_unknown heals " +
                "npc_dota_hero_snapfire for 400 health (1302->1702)", match);
        verify(heroService, times(2)).getOrCreate(heroName);
        verify(spellService, never()).getOrCreate(anyString());
        verify(itemService, never()).getOrCreate(anyString());
        verify(timeUtil, times(1)).eventTimeToMilliseconds("00:25:40.345");
    }

    @Test
    public void testParseEventWhenWhenEventIsIgnored() {
        Match match = DotaTestFactory.createMatch();
        healEventParser.parseEvent("[00:25:40.545] npc_dota_hero_death_prophet uses item_phase_boots", match);
        verify(heroService, never()).getOrCreate(anyString());
        verify(spellService, never()).getOrCreate(anyString());
        verify(itemService, never()).getOrCreate(anyString());
        verify(timeUtil, never()).eventTimeToMilliseconds(anyString());
    }
}
