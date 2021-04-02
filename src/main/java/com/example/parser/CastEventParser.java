package com.example.parser;

import com.example.entity.*;
import com.example.service.HeroService;
import com.example.service.SpellService;
import com.example.util.TimeUtil;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CastEventParser implements EventParser {

    // [00:08:43.460] npc_dota_hero_abyssal_underlord castEvents ability abyssal_underlord_firestorm (lvl 2) on dota_unknown
    private static final Pattern CAST_EVENT_PATTERN =
            Pattern.compile("\\[(.+)\\] npc_dota_hero_(\\w+) casts ability (\\w+) \\(lvl (\\d)\\) on npc_dota_hero_(\\w+)");

    private final HeroService heroService;

    private final SpellService spellService;

    private final TimeUtil timeUtil;

    public CastEventParser(
            HeroService heroService,
            SpellService spellService,
            TimeUtil timeUtil
    ) {
        this.heroService = heroService;
        this.spellService = spellService;
        this.timeUtil = timeUtil;
    }

    @Override
    public void parseEvent(String logLine, Match match) {
        Matcher matcher = CAST_EVENT_PATTERN.matcher(logLine);
        if (matcher.find()) {
            Hero hero = heroService.getOrCreate(matcher.group(2));
            Spell spell = spellService.getOrCreate(matcher.group(3));
            Hero targetHero = heroService.getOrCreate(matcher.group(5));
            match.addMatchHero(targetHero);
            MatchHero matchHero = match.addMatchHero(hero);
            matchHero.addCastEvent(new CastEvent(matchHero, spell, targetHero, Integer.valueOf(matcher.group(4)),
                    timeUtil.eventTimeToMilliseconds(matcher.group(1))));
        }
    }
}
