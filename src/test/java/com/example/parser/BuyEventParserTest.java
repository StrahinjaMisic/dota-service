package com.example.parser;

import com.example.DotaTestFactory;
import com.example.entity.Match;
import com.example.service.HeroService;
import com.example.service.ItemService;
import com.example.util.TimeUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BuyEventParserTest {

    @Mock
    private HeroService heroService;

    @Mock
    private ItemService itemService;

    @Mock
    private TimeUtil timeUtil;

    @InjectMocks
    private BuyEventParser buyEventParser;

    @Test
    public void testParseEvent() {
        String heroName = "snapfire";
        String itemName = "ward_observer";
        Match match = DotaTestFactory.createMatch();
        when(heroService.getOrCreate(anyString())).thenReturn(DotaTestFactory.createHero(heroName));
        when(itemService.getOrCreate(anyString())).thenReturn(DotaTestFactory.createItem(itemName));
        buyEventParser.parseEvent("[00:26:04.306] npc_dota_hero_snapfire buys item item_ward_observer", match);
        verify(heroService, times(1)).getOrCreate(heroName);
        verify(itemService, times(1)).getOrCreate(itemName);
        verify(timeUtil, times(1)).eventTimeToMilliseconds("00:26:04.306");
    }

    @Test
    public void testParseEventWhenEventIsIgnored() {
        Match match = DotaTestFactory.createMatch();
        buyEventParser.parseEvent("[00:25:40.545] npc_dota_hero_death_prophet uses item_phase_boots", match);
        verify(heroService, never()).getOrCreate(anyString());
        verify(itemService, never()).getOrCreate(anyString());
        verify(timeUtil, never()).eventTimeToMilliseconds(anyString());
    }
}
