package com.example.parser;

import com.example.entity.Match;
import com.example.repository.MatchRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EventParserService {

    private static final Pattern EVENTS_PATTERN = Pattern.compile("^.*?(casts|buys|uses|hits|heals|killed|state).*$");

    private final Map<String, EventParser> eventParsers = new HashMap<>();

    private final MatchRepository matchRepository;

    public EventParserService(
            MatchRepository matchRepository,
            CastEventParser castEventParser,
            BuyEventParser buyEventParser,
            UseEventParser useEventParser,
            HitEventParser hitEventParser,
            HealEventParser healEventParser,
            KillEventParser killEventParser,
            GameStateEventParser gameStateEventParser
    ) {
        this.matchRepository = matchRepository;
        eventParsers.put("casts", castEventParser);
        eventParsers.put("buys", buyEventParser);
        eventParsers.put("uses", useEventParser);
        eventParsers.put("hits", hitEventParser);
        eventParsers.put("heals", healEventParser);
        eventParsers.put("killed", killEventParser);
        eventParsers.put("state", gameStateEventParser);
    }

    public void parsePayload(Match match) {
        String[] logLines = match.getPayload().getText().split(System.lineSeparator());
        for (String logLine : logLines) {
            Matcher matcher = EVENTS_PATTERN.matcher(logLine);
            if (matcher.find()) {
                eventParsers.get(matcher.group(1)).parseEvent(logLine, match);
            }
        }
        match.setIsIngested(Boolean.TRUE);
        matchRepository.save(match);
    }
}
