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
public class HitEventParserTest {

    @Mock
    private HeroService heroService;

    @Mock
    private SpellService spellService;

    @Mock
    private ItemService itemService;

    @Mock
    private TimeUtil timeUtil;

    @InjectMocks
    private HitEventParser hitEventParser;

    @Test
    public void testParseEventWhenHitIsPerformedWithItem() {
        String heroName = "dragon_knight";
        String targetHeroName = "bloodseeker";
        String itemName = "maelstrom";
        Match match = DotaTestFactory.createMatch();
        when(heroService.getOrCreate(anyString())).thenReturn(DotaTestFactory.createHero(heroName),
                DotaTestFactory.createHero(targetHeroName));
        when(itemService.getOrCreate(anyString())).thenReturn(DotaTestFactory.createItem(itemName));
        hitEventParser.parseEvent("[00:24:12.367] npc_dota_hero_dragon_knight hits npc_dota_hero_bloodseeker" +
                " with item_maelstrom for 117 damage (1253->1136)", match);
        verify(heroService, times(1)).getOrCreate(heroName);
        verify(heroService, times(1)).getOrCreate(targetHeroName);
        verify(itemService, times(1)).getOrCreate(itemName);
        verify(spellService, never()).getOrCreate(anyString());
        verify(timeUtil, times(1)).eventTimeToMilliseconds("00:24:12.367");
    }

    @Test
    public void testParseEventWhenHitIsPerformedWithSpell() {
        String heroName = "abyssal_underlord";
        String targetHeroName = "bloodseeker";
        String spellName = "abyssal_underlord_firestorm";
        Match match = DotaTestFactory.createMatch();
        when(heroService.getOrCreate(anyString())).thenReturn(DotaTestFactory.createHero(heroName),
                DotaTestFactory.createHero(targetHeroName));
        when(spellService.getOrCreate(anyString())).thenReturn(DotaTestFactory.createSpell(spellName));
        hitEventParser.parseEvent("[00:10:42.031] npc_dota_hero_abyssal_underlord hits " +
                "npc_dota_hero_bloodseeker with abyssal_underlord_firestorm for 18 damage (720->702)", match);
        verify(heroService, times(1)).getOrCreate(heroName);
        verify(heroService, times(1)).getOrCreate(targetHeroName);
        verify(spellService, times(1)).getOrCreate(spellName);
        verify(itemService, never()).getOrCreate(anyString());
        verify(timeUtil, times(1)).eventTimeToMilliseconds("00:10:42.031");
    }

    @Test
    public void testParseEventWhenHitIsWithDotaUnknown() {
        String heroName = "bane";
        String targetHeroName = "abyssal_underlord";
        Match match = DotaTestFactory.createMatch();
        when(heroService.getOrCreate(anyString())).thenReturn(DotaTestFactory.createHero(heroName),
                DotaTestFactory.createHero(targetHeroName));
        hitEventParser.parseEvent("[00:10:42.031] npc_dota_hero_bane hits npc_dota_hero_abyssal_underlord " +
                "with dota_unknown for 51 damage (740->689)", match);
        verify(heroService, times(1)).getOrCreate(heroName);
        verify(heroService, times(1)).getOrCreate(targetHeroName);
        verify(spellService, never()).getOrCreate(anyString());
        verify(itemService, never()).getOrCreate(anyString());
        verify(timeUtil, times(1)).eventTimeToMilliseconds("00:10:42.031");
    }

    @Test
    public void testParseEventWhenWhenEventIsIgnored() {
        Match match = DotaTestFactory.createMatch();
        hitEventParser.parseEvent("[00:25:40.545] npc_dota_hero_death_prophet uses item_phase_boots", match);
        verify(heroService, never()).getOrCreate(anyString());
        verify(spellService, never()).getOrCreate(anyString());
        verify(itemService, never()).getOrCreate(anyString());
        verify(timeUtil, never()).eventTimeToMilliseconds(anyString());
    }
}
