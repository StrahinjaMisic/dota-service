package com.example.service.impl;

import com.example.entity.*;
import com.example.model.*;
import com.example.repository.MatchRepository;
import com.example.service.MatchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;

    public MatchServiceImpl(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public SimpleMatchModel ingestMatch(String payload) {
        Long id = Long.valueOf(Math.abs(payload.hashCode()));
        if (matchRepository.existsById(id)) {
            throw new EntityExistsException(String.format("Match log already ingested under id %s", id));
        }
        Match match = matchRepository.save(new Match(id, payload));
        return toSimpleMatchModel(match);
    }

    @Override
    public List<Match> getAllMatchesPendingIngestion() {
        return matchRepository.getByIsIngested(Boolean.FALSE);
    }

    @Override
    public Page<SimpleMatchModel> getMatches(Pageable pageable) {
        return matchRepository.findAll(pageable).map(match -> toSimpleMatchModel(match));
    }

    @Override
    public MatchStatsModel getMatchStats(Long id) {
        Match match = matchRepository.findById(id).orElseThrow(
                () -> new NoResultException(String.format("Match %s not found", id))
        );
        return getMatchStats(match);
    }

    private SimpleMatchModel toSimpleMatchModel(Match match) {
        SimpleMatchModel matchModel = new SimpleMatchModel();
        matchModel.setId(match.getId());
        matchModel.setIsIngested(match.getIsIngested());
        matchModel.setDuration(match.getDuration());
        if (!match.getMatchHeroes().isEmpty()) {
            List<SimpleHeroModel> heroModels = new ArrayList<>();
            for (MatchHero matchHero : match.getMatchHeroes()) {
                SimpleHeroModel heroModel = new SimpleHeroModel();
                heroModel.setName(matchHero.getHero().getName());
                heroModels.add(heroModel);
            }
            matchModel.setHeroes(heroModels);
        }
        return matchModel;
    }

    private MatchStatsModel getMatchStats(Match match) {
        MatchStatsModel matchStatsModel = new MatchStatsModel();
        matchStatsModel.setId(match.getId());
        matchStatsModel.setIsIngested(match.getIsIngested());
        matchStatsModel.setDuration(match.getDuration());
        if (!match.getMatchHeroes().isEmpty()) {
            List<HeroStatsModel> heroStatsModelList = new ArrayList<>();
            for (MatchHero matchHero : match.getMatchHeroes()) {
                HeroStatsModel heroStatsModel = new HeroStatsModel(matchHero.getHero().getName(),
                        matchHero.getKillEvents().size(), matchHero.getDeathEvents().size(), generateDamageStats(matchHero),
                        generateHealStats(matchHero), generateSpellStats(matchHero), generateItemStats(matchHero));
                heroStatsModelList.add(heroStatsModel);
            }
            matchStatsModel.setHeroes(heroStatsModelList);
        }
        return matchStatsModel;
    }

    private DamageStatsModel generateDamageStats(MatchHero matchHero) {
        DamageStatsModel damageStatsModel = new DamageStatsModel();
        int totalDamage = matchHero.getHitEvents().stream().mapToInt(hit -> hit.getDamage()).sum();
        int maxDamage = matchHero.getHitEvents().stream().mapToInt(hit -> hit.getDamage()).max().orElse(0);
        damageStatsModel.setInstances(matchHero.getHitEvents().size());
        damageStatsModel.setMax(maxDamage);
        if (totalDamage != 0) {
            damageStatsModel.setAverage(totalDamage / matchHero.getHitEvents().size());
        }
        damageStatsModel.setTotal(totalDamage);
        return damageStatsModel;
    }

    private HealStatsModel generateHealStats(MatchHero matchHero) {
        HealStatsModel healStatsModel = new HealStatsModel();
        int totalHealAmount = matchHero.getHealEvents().stream().mapToInt(hit -> hit.getAmount()).sum();
        int maxHealAmount = matchHero.getHealEvents().stream().mapToInt(hit -> hit.getAmount()).max().orElse(0);
        healStatsModel.setInstances(matchHero.getHealEvents().size());
        healStatsModel.setMax(maxHealAmount);
        if (totalHealAmount != 0) {
            healStatsModel.setAverage(totalHealAmount / matchHero.getHealEvents().size());
        }
        healStatsModel.setTotal(totalHealAmount);
        return healStatsModel;
    }

    private List<SpellStatsModel> generateSpellStats(MatchHero matchHero) {
        Set<Spell> spells = matchHero.getCastEvents().stream()
                .map(castEvent -> castEvent.getSpell()).collect(Collectors.toSet());
        Map<String, SpellStatsModel> spellModelMap = new HashMap<>();
        for (Spell spell : spells) {
            SpellStatsModel spellStatsModel = new SpellStatsModel();
            spellStatsModel.setName(spell.getName());
            spellModelMap.put(spell.getName(), spellStatsModel);
        }
        for (CastEvent castEvent : matchHero.getCastEvents()) {
            SpellStatsModel spellStatsModel = spellModelMap.get(castEvent.getSpell().getName());
            spellStatsModel.setCasts(spellStatsModel.getCasts() + 1);
        }
        List<SpellStatsModel> spellStatsModels = spellModelMap.values().stream().collect(Collectors.toList());
        Collections.sort(spellStatsModels, Comparator.comparing(SpellStatsModel::getCasts).thenComparing(SpellStatsModel::getName));
        return spellStatsModels;
    }

    private List<ItemStatsModel> generateItemStats(MatchHero matchHero) {
        List<ItemStatsModel> itemStatsModels = new ArrayList<>();
        for (BuyEvent buyEvent : matchHero.getBuyEvents()) {
            itemStatsModels.add(new ItemStatsModel(buyEvent.getItem().getName(), buyEvent.getTimestamp()));
        }
        Collections.sort(itemStatsModels, Comparator.comparing(ItemStatsModel::getBuyTime).thenComparing(ItemStatsModel::getName));
        return itemStatsModels;
    }
}
