package com.example.repository;

import com.example.entity.Match;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends PagingAndSortingRepository<Match, Long> {

    boolean existsById(Long payloadHash);

    List<Match> getByIsIngested(Boolean isIngested);
}
