package com.example.util;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TimeUtilTest {

    @InjectMocks
    private TimeUtil timeUtil;

    @Test
    public void test() {
        Long timestamp = timeUtil.eventTimeToMilliseconds("00:37:22.851");
        Assert.assertEquals(2242851L, timestamp.longValue());
    }
}
