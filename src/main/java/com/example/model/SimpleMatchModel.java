package com.example.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SimpleMatchModel {

    private Long id;

    private Long duration;

    private Boolean isIngested;

    private List<SimpleHeroModel> heroes;
}
