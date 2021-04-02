package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeroStatsModel {

    private String name;

    private int kills;

    private int deaths;

    private DamageStatsModel damage;

    private HealStatsModel heal;

    private List<SpellStatsModel> spells;

    private List<ItemStatsModel> items;
}
