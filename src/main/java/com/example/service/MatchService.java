package com.example.service;

import com.example.model.MatchStatsModel;
import com.example.model.SimpleMatchModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MatchService {

    SimpleMatchModel ingestMatch(String payload);

    MatchStatsModel getMatchStats(Long id);

    Page<SimpleMatchModel> getMatches(Pageable pageable);
}
