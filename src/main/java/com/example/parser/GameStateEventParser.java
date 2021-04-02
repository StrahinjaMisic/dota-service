package com.example.parser;

import com.example.entity.Match;
import com.example.util.TimeUtil;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GameStateEventParser implements EventParser {

    // [00:37:22.851] game state is now 6
    private static final Pattern MATCH_DURATION_PATTERN = Pattern.compile("\\[(.+)\\] game state is now 6");

    private final TimeUtil timeUtil;

    public GameStateEventParser(TimeUtil timeUtil) {
        this.timeUtil = timeUtil;
    }

    @Override
    public void parseEvent(String logLine, Match match) {
        Matcher matcher = MATCH_DURATION_PATTERN.matcher(logLine);
        if (matcher.find()) {
            match.setDuration(timeUtil.eventTimeToMilliseconds(matcher.group(1)));
        }
    }
}
