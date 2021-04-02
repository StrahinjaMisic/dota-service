package com.example.parser;

import com.example.entity.*;
import com.example.service.HeroService;
import com.example.service.ItemService;
import com.example.util.TimeUtil;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BuyEventParser implements EventParser {

    // [00:11:23.821] npc_dota_hero_rubick buyEvents item item_magic_stick
    private static final Pattern BUY_EVENT_PATTERN =
            Pattern.compile("\\[(.+)\\] npc_dota_hero_(\\w+) buys item item_(\\w+)");

    private final HeroService heroService;

    private final ItemService itemService;

    private final TimeUtil timeUtil;

    public BuyEventParser(
            HeroService heroService,
            ItemService itemService,
            TimeUtil timeUtil
    ) {
        this.heroService = heroService;
        this.itemService = itemService;
        this.timeUtil = timeUtil;
    }

    @Override
    public void parseEvent(String logLine, Match match) {
        Matcher matcher = BUY_EVENT_PATTERN.matcher(logLine);
        if (matcher.find()) {
            Hero hero = heroService.getOrCreate(matcher.group(2));
            Item item = itemService.getOrCreate(matcher.group(3));
            MatchHero matchHero = match.addMatchHero(hero);
            matchHero.addBuyEvent(new BuyEvent(matchHero, item, timeUtil.eventTimeToMilliseconds(matcher.group(1))));
        }
    }
}
