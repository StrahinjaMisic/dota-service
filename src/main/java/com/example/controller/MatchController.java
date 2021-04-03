package com.example.controller;

import com.example.model.MatchStatsModel;
import com.example.model.SimpleMatchModel;
import com.example.service.MatchService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

@Validated
@RestController
@RequestMapping(value = "/matches", produces = MediaType.APPLICATION_JSON_VALUE)
public class MatchController {

    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @ApiOperation("Ingests Dota 2 combatlog")
    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<SimpleMatchModel> ingestMatch(@RequestBody @NotBlank String payload) {
        return ResponseEntity.ok(matchService.ingestMatch(payload));
    }

    @ApiOperation("Gets a list of matches")
    @GetMapping
    public ResponseEntity<Page<SimpleMatchModel>> getMatches(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(matchService.getMatches(pageable));
    }

    @ApiOperation("Gets match stats")
    @GetMapping("{id}")
    public ResponseEntity<MatchStatsModel> getMatchStats(@PathVariable("id") Long id) {
        return ResponseEntity.ok(matchService.getMatchStats(id));
    }
}
