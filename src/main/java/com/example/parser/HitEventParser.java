package com.example.parser;

import com.example.entity.*;
import com.example.service.HeroService;
import com.example.service.ItemService;
import com.example.service.SpellService;
import com.example.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class HitEventParser implements EventParser {

    // [00:11:25.687] npc_dota_hero_puck hitEvents npc_dota_hero_bloodseeker with puck_illusory_orb for 57 damage (533->476)
    // [00:11:26.120] npc_dota_hero_puck hitEvents npc_dota_hero_bane with dota_unknown for 44 damage (589->545)
    private static final Pattern HIT_EVENT_PATTERN =
            Pattern.compile("\\[(.+)\\] npc_dota_hero_(\\w+) hits npc_dota_hero_(\\w+) with (\\w+) for (\\d+)");

    private static final String UNKNOWN = "dota_unknown";

    private static final String ITEM_PREFIX = "item_";

    private final HeroService heroService;

    private final ItemService itemService;

    private final SpellService spellService;

    private final TimeUtil timeUtil;

    public HitEventParser(
            HeroService heroService,
            ItemService itemService,
            SpellService spellService,
            TimeUtil timeUtil
    ) {
        this.heroService = heroService;
        this.itemService = itemService;
        this.spellService = spellService;
        this.timeUtil = timeUtil;
    }

    @Override
    public void parseEvent(String logLine, Match match) {
        Matcher matcher = HIT_EVENT_PATTERN.matcher(logLine);
        if (matcher.find()) {
            Hero hero = heroService.getOrCreate(matcher.group(2));
            Hero targetHero = heroService.getOrCreate(matcher.group(3));
            match.addMatchHero(targetHero);
            Item item = null;
            Spell spell = null;
            String hitWith = matcher.group(4);
            if (hitWith.startsWith(ITEM_PREFIX)) {
                item = itemService.getOrCreate(hitWith.replace(ITEM_PREFIX, StringUtils.EMPTY));
            } else if (!hitWith.equals(UNKNOWN)) {
                spell = spellService.getOrCreate(hitWith);
            }
            MatchHero matchHero = match.addMatchHero(hero);
            matchHero.addHitEvent(new HitEvent(matchHero, item, spell, targetHero, Integer.valueOf(matcher.group(5)),
                    timeUtil.eventTimeToMilliseconds(matcher.group(1))));
        }
    }
}
