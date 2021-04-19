package com.example.repository;

import com.example.entity.Match;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MatchRepository extends PagingAndSortingRepository<Match, Long> {

    boolean existsById(Long payloadHash);
}
