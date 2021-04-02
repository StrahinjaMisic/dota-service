package com.example.util;

import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.temporal.ChronoField;

@Component
public class TimeUtil {

    public Long eventTimeToMilliseconds(String time) {
        return LocalTime.parse(time).getLong(ChronoField.MILLI_OF_DAY);
    }
}
