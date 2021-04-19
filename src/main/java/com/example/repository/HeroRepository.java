package com.example.repository;

import com.example.entity.Hero;
import org.springframework.data.repository.CrudRepository;

public interface HeroRepository extends CrudRepository<Hero, Long> {

    Hero getByName(String name);
}
