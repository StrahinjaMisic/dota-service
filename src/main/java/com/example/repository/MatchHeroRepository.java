package com.example.repository;

import com.example.entity.MatchHero;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchHeroRepository extends CrudRepository<MatchHero, Long> {
}
