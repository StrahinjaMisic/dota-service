package com.example.service;

import com.example.DotaTestFactory;
import com.example.entity.Match;
import com.example.model.HeroStatsModel;
import com.example.model.MatchStatsModel;
import com.example.model.SimpleMatchModel;
import com.example.parser.EventParserService;
import com.example.repository.MatchRepository;
import com.example.service.impl.MatchServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.example.DotaTestFactory.MATCH_ID;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatchServiceImplTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private EventParserService eventParserService;

    @InjectMocks
    private MatchServiceImpl matchService;

    @Test
    public void testIngestMatch() throws IOException {
        String payload = DotaTestFactory.getPayload();
        when(matchRepository.existsById(anyLong())).thenReturn(false);
        doAnswer(returnsFirstArg()).when(matchRepository).save(any(Match.class));
        SimpleMatchModel simpleMatchModel = matchService.ingestMatch(payload);
        Assert.assertNotNull(simpleMatchModel);
        Assert.assertEquals(MATCH_ID, simpleMatchModel.getId());
        Assert.assertTrue(!simpleMatchModel.getIsIngested());
        verify(matchRepository, times(1)).save(any(Match.class));
        verify(eventParserService, times(1)).parsePayload(any(Match.class));
    }

    @Test
    public void testIngestMatchThrowsEntityExistsException() throws IOException {
        String payload = DotaTestFactory.getPayload();
        when(matchRepository.existsById(anyLong())).thenReturn(true);
        Assert.assertThrows(EntityExistsException.class, () -> matchService.ingestMatch(payload));
        verify(matchRepository, never()).save(any(Match.class));
        verify(eventParserService, never()).parsePayload(any(Match.class));
    }

    @Test
    public void testGet() throws IOException {
        Match match = DotaTestFactory.createIngestedMatch();
        when(matchRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(match)));
        Page<SimpleMatchModel> results = matchService.getMatches(Pageable.unpaged());
        List<SimpleMatchModel> content = results.getContent();
        Assert.assertEquals(1, content.size());
        SimpleMatchModel simpleMatchModel = content.get(0);
        Assert.assertEquals(match.getId(), simpleMatchModel.getId());
        Assert.assertEquals(match.getIsIngested(), simpleMatchModel.getIsIngested());
        Assert.assertEquals(match.getDuration(), simpleMatchModel.getDuration());
        Assert.assertEquals(10, simpleMatchModel.getHeroes().size());
    }

    @Test
    public void testGetMatchStats() throws IOException {
        Match match = DotaTestFactory.createIngestedMatch();
        when(matchRepository.findById(anyLong())).thenReturn(Optional.of(match));
        MatchStatsModel result = matchService.getMatchStats(MATCH_ID);
        Assert.assertEquals(match.getId(), result.getId());
        Assert.assertEquals(match.getIsIngested(), result.getIsIngested());
        Assert.assertEquals(match.getDuration(), result.getDuration());
        Assert.assertEquals(match.getMatchHeroes().size(), result.getHeroes().size());
        HeroStatsModel heroStatsModel = result.getHeroes().get(0);
        Assert.assertEquals(match.getDuration(), result.getDuration());
        Assert.assertEquals("snapfire", heroStatsModel.getName());
        Assert.assertEquals(2, heroStatsModel.getKills());
        Assert.assertEquals(7, heroStatsModel.getDeaths());
        Assert.assertEquals(151, heroStatsModel.getDamage().getInstances());
        Assert.assertEquals(9448, heroStatsModel.getDamage().getTotal());
        Assert.assertEquals(302, heroStatsModel.getDamage().getMax());
        Assert.assertEquals(62, heroStatsModel.getDamage().getAverage());
        Assert.assertEquals(3, heroStatsModel.getHeal().getInstances());
        Assert.assertEquals(1025, heroStatsModel.getHeal().getTotal());
        Assert.assertEquals(400, heroStatsModel.getHeal().getMax());
        Assert.assertEquals(341, heroStatsModel.getHeal().getAverage());
        Assert.assertEquals(2, heroStatsModel.getSpells().size());
        Assert.assertEquals("snapfire_firesnap_cookie", heroStatsModel.getSpells().get(0).getName());
        Assert.assertEquals(12, heroStatsModel.getSpells().get(0).getCasts());
        Assert.assertEquals(34, heroStatsModel.getItems().size());
        Assert.assertEquals("clarity", heroStatsModel.getItems().get(0).getName());
        Assert.assertEquals(526693, heroStatsModel.getItems().get(0).getBuyTime());
    }

    @Test
    public void testGetMatchStatsThrowsNoResultException() {
        when(matchRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assert.assertThrows(NoResultException.class, () -> matchService.getMatchStats(MATCH_ID));
    }
}
