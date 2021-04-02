package com.example.parser;

import com.example.entity.Match;

public interface EventParser {

    void parseEvent(String logLine, Match match);
}
