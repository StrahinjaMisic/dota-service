package com.example.service;

import com.example.DotaTestFactory;
import com.example.entity.Hero;
import com.example.repository.HeroRepository;
import com.example.service.impl.HeroServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HeroServiceImplTest {

    @Mock
    private HeroRepository heroRepository;

    @InjectMocks
    private HeroServiceImpl heroService;

    @Test
    public void testGetOrCreateWhenHeroExists() {
        String name = "snapfire";
        Hero hero = DotaTestFactory.createHero(name);
        when(heroRepository.getByName(anyString())).thenReturn(hero);
        Hero result = heroService.getOrCreate(name);
        Assert.assertEquals(hero, result);
        verify(heroRepository, never()).save(any(Hero.class));
    }

    @Test
    public void testGetOrCreateWhenHeroDoesNotExist() {
        String name = "snapfire";
        when(heroRepository.getByName(anyString())).thenReturn(null);
        doAnswer(returnsFirstArg()).when(heroRepository).save(any(Hero.class));
        Hero result = heroService.getOrCreate(name);
        Assert.assertEquals(name, result.getName());
        verify(heroRepository, times(1)).save(any(Hero.class));
    }
}
