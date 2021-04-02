package com.example;

import org.hibernate.engine.spi.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {PersistenceContext.class})
class DotaServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}
