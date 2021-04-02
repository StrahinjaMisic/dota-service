package com.example.service;

import com.example.entity.Hero;

public interface HeroService {

    Hero getOrCreate(String name);
}
