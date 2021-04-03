package com.example.it;

import com.example.DotaTestFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class IT {

    @Container
    public static PostgreSQLContainer DATABASE = new PostgreSQLContainer("postgres:10").withDatabaseName("data");

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", DATABASE::getJdbcUrl);
        registry.add("spring.datasource.username", DATABASE::getUsername);
        registry.add("spring.datasource.password", DATABASE::getPassword);
    }

    @Test
    public void test() throws Exception {
        // Create match
        String payload = DotaTestFactory.getPayload();
        MockHttpServletResponse response = ingestPayload(payload);
        JsonNode match = OBJECT_MAPPER.readTree(response.getContentAsString());
        String id = match.get("id").asText();
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assert.assertEquals(DotaTestFactory.getMatchesResponse(), response.getContentAsString());

        // Try ingesting the same payload
        response = ingestPayload(payload);
        Assert.assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());

        // Get non existing match
        response = getMatchStats("1");
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());

        // Get matches
        response = getMatches();
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        JsonNode content = OBJECT_MAPPER.readTree(response.getContentAsString());
        Assert.assertEquals(DotaTestFactory.getMatchesResponse(), content.get("content").get(0).toString());

        // Get match stats
        response = getMatchStats(id);
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assert.assertEquals(DotaTestFactory.getMatchStatsResponse(), response.getContentAsString());
    }

    private MockHttpServletResponse ingestPayload(String payload) throws Exception {
        return mockMvc.perform(post("/matches").content(payload).contentType(MediaType.TEXT_PLAIN)).andReturn().getResponse();
    }

    private MockHttpServletResponse getMatches() throws Exception {
        return mockMvc.perform(get("/matches")).andReturn().getResponse();
    }

    private MockHttpServletResponse getMatchStats(String id) throws Exception {
        return mockMvc.perform(get("/matches/" + id)).andReturn().getResponse();
    }
}

