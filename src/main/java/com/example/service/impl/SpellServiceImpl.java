package com.example.service.impl;

import com.example.entity.Spell;
import com.example.repository.SpellRepository;
import com.example.service.SpellService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Cacheable("spells")
public class SpellServiceImpl implements SpellService {

    private final SpellRepository spellRepository;

    public SpellServiceImpl(SpellRepository spellRepository) {
        this.spellRepository = spellRepository;
    }

    @Override
    public Spell getOrCreate(String name) {
        Spell spell = spellRepository.getByName(name);
        if (spell == null) {
            spell = new Spell(name);
            spell = spellRepository.save(spell);
        }
        return spell;
    }
}
