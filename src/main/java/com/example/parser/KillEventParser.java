package com.example.parser;

import com.example.entity.*;
import com.example.service.HeroService;
import com.example.util.TimeUtil;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class KillEventParser implements EventParser {

    // [00:13:06.296] npc_dota_hero_pangolier is killed by npc_dota_hero_mars
    private static final Pattern KILL_LOG_PATTERN =
            Pattern.compile("\\[(.+)\\] npc_dota_hero_(\\w+) is killed by npc_dota_hero_(\\w+)");

    private final HeroService heroService;

    private final TimeUtil timeUtil;

    public KillEventParser(
            HeroService heroService,
            TimeUtil timeUtil
    ) {
        this.heroService = heroService;
        this.timeUtil = timeUtil;
    }

    @Override
    public void parseEvent(String logLine, Match match) {
        Matcher matcher = KILL_LOG_PATTERN.matcher(logLine);
        if (matcher.find()) {
            Hero attackerHero = heroService.getOrCreate(matcher.group(3));
            Hero targetHero = heroService.getOrCreate(matcher.group(2));
            Long timestamp = timeUtil.eventTimeToMilliseconds(matcher.group(1));

            MatchHero killMatchHero = match.addMatchHero(attackerHero);
            killMatchHero.addKillsEvent(new KillEvent(killMatchHero, targetHero, timestamp));

            MatchHero deathMatchHero = match.addMatchHero(targetHero);
            deathMatchHero.addDeathEvent(new DeathEvent(deathMatchHero, attackerHero, timestamp));
        }
    }
}
