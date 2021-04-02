package com.example.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MatchStatsModel {

    private Long id;

    private Long duration;

    private Boolean isIngested;

    List<HeroStatsModel> heroes;

}
