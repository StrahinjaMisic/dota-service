package com.example.service;

import com.example.entity.Match;
import com.example.model.MatchStatsModel;
import com.example.model.SimpleMatchModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MatchService {

    SimpleMatchModel ingestMatch(String payload);

    List<Match> getAllMatchesPendingIngestion();

    MatchStatsModel getMatchStats(Long id);

    Page<SimpleMatchModel> getMatches(Pageable pageable);
}
