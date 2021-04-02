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
public class HealEventParser implements EventParser {

    // [00:20:24.222] npc_dota_hero_ember_spirit's dota_unknown healEvents npc_dota_hero_ember_spirit for 400 health (218->618)
    // [00:18:15.687] npc_dota_hero_death_prophet's item_bottle healEvents npc_dota_hero_death_prophet for 245 health (846->1091)
    // [00:11:20.255] npc_dota_hero_bloodseeker's bloodseeker_bloodrage healEvents npc_dota_hero_bloodseeker for 55 health (474->529)
    private static final Pattern HEAL_EVENT_PATTERN =
            Pattern.compile("\\[(.+)\\] npc_dota_hero_(\\w+)'s (\\w+) heals npc_dota_hero_(\\w+) for (\\d+) health");

    private static final String UNKNOWN = "dota_unknown";

    private static final String ITEM_PREFIX = "item_";

    private final HeroService heroService;

    private final ItemService itemService;

    private final SpellService spellService;

    private final TimeUtil timeUtil;

    public HealEventParser(
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
        Matcher matcher = HEAL_EVENT_PATTERN.matcher(logLine);
        if (matcher.find()) {
            Hero hero = heroService.getOrCreate(matcher.group(2));
            Hero targetHero = heroService.getOrCreate(matcher.group(4));
            match.addMatchHero(targetHero);
            Item item = null;
            Spell spell = null;
            if (matcher.group(3).startsWith(ITEM_PREFIX)) {
                item = itemService.getOrCreate(matcher.group(3).replace(ITEM_PREFIX, StringUtils.EMPTY));
            } else if (!matcher.group(3).equals(UNKNOWN)) {
                spell = spellService.getOrCreate(matcher.group(3));
            }
            MatchHero matchHero = match.addMatchHero(hero);
            matchHero.addHealEvent(new HealEvent(matchHero, item, spell, targetHero, Integer.valueOf(matcher.group(5)),
                    timeUtil.eventTimeToMilliseconds(matcher.group(1))));
        }
    }
}
