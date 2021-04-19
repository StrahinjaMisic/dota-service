package com.example.repository;

import com.example.entity.Spell;
import org.springframework.data.repository.CrudRepository;

public interface SpellRepository extends CrudRepository<Spell, Long> {

    Spell getByName(String name);
}
