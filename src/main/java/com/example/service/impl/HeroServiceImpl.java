package com.example.service.impl;

import com.example.entity.Hero;
import com.example.repository.HeroRepository;
import com.example.service.HeroService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Cacheable("heroes")
public class HeroServiceImpl implements HeroService {

    private final HeroRepository heroRepository;

    public HeroServiceImpl(HeroRepository heroRepository) {
        this.heroRepository = heroRepository;
    }

    @Override
    public Hero getOrCreate(String name) {
        Hero hero = heroRepository.getByName(name);
        if (hero == null) {
            hero = new Hero(name);
            hero = heroRepository.save(hero);
        }
        return hero;
    }
}

