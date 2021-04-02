package com.example;

import com.example.entity.*;
import com.example.model.SimpleHeroModel;
import com.example.model.SimpleMatchModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DotaTestFactory {
    
    public static final Long MATCH_ID = 1555827785L;

    public static Hero createHero(String name) {
        Hero hero = new Hero();
        hero.setName(name);
        return hero;
    }

    public static Item createItem(String name) {
        Item item = new Item();
        item.setName(name);
        return item;
    }

    public static Spell createSpell(String name) {
        Spell spell = new Spell();
        spell.setName(name);
        return spell;
    }

    public static Match createIngestedMatch() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Match match = mapper.readValue(new File("src/test/resource/data/match.json"), Match.class);
        return match;
    }

    public static Match createMatch() {
        Match match = new Match();
        match.setId(MATCH_ID);
        return match;
    }

    public static Match createMatchWithPayload() throws IOException {
        Match match = createMatch();
        Payload payload = new Payload();
        payload.setText(getPayload());
        match.setPayload(payload);
        return match;
    }

    public static String getPayload() throws IOException {
        return new String(Files.readAllBytes(Paths.get("src/main/resources/data/combatlog_1.txt")));
    }

    public static String getMatchStatsResponse() throws IOException {
        return new String(Files.readAllBytes(Paths.get("src/test/resource/data/match_stats_response.json")));
    }

    public static String getMatchesResponse() throws IOException {
        return new String(Files.readAllBytes(Paths.get("src/test/resource/data/matches_response.json")));
    }

    public static SimpleMatchModel createSimpleMatchModel() {
        SimpleMatchModel simpleMatchModel = new SimpleMatchModel();
        simpleMatchModel.setId(MATCH_ID);
        simpleMatchModel.setDuration(2242851L);
        simpleMatchModel.setIsIngested(true);
        List<SimpleHeroModel> heroes = new ArrayList<>();
        heroes.add(createSimpleHeroModel("snapfire"));
        heroes.add(createSimpleHeroModel("dragon_knight"));
        heroes.add(createSimpleHeroModel("puck"));
        heroes.add(createSimpleHeroModel("mars"));
        heroes.add(createSimpleHeroModel("rubick"));
        heroes.add(createSimpleHeroModel("bloodseeker"));
        heroes.add(createSimpleHeroModel("pangolier"));
        heroes.add(createSimpleHeroModel("abyssal_underlord"));
        heroes.add(createSimpleHeroModel("death_prophet"));
        heroes.add(createSimpleHeroModel("bane"));
        return simpleMatchModel;
    }

    public static SimpleHeroModel createSimpleHeroModel(String name) {
        SimpleHeroModel simpleHeroModel = new SimpleHeroModel();
        simpleHeroModel.setName(name);
        return simpleHeroModel;
    }
}
