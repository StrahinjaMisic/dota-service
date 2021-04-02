package com.example.service;

import com.example.entity.Spell;

public interface SpellService {

    Spell getOrCreate(String name);
}
