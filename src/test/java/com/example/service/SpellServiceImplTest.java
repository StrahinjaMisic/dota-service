package com.example.service;

import com.example.DotaTestFactory;
import com.example.entity.Spell;
import com.example.repository.SpellRepository;
import com.example.service.impl.SpellServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpellServiceImplTest {

    @Mock
    private SpellRepository spellRepository;

    @InjectMocks
    private SpellServiceImpl spellService;

    @Test
    public void testGetOrCreateWhenSpellExists() {
        String name = "snapfire_gobble_up";
        Spell spell = DotaTestFactory.createSpell(name);
        when(spellRepository.getByName(anyString())).thenReturn(spell);
        Spell result = spellService.getOrCreate(name);
        Assert.assertEquals(spell, result);
        verify(spellRepository, never()).save(any(Spell.class));
    }

    @Test
    public void testGetOrCreateWhenSpellDoesNotExist() {
        String name = "snapfire_gobble_up";
        when(spellRepository.getByName(anyString())).thenReturn(null);
        doAnswer(returnsFirstArg()).when(spellRepository).save(any(Spell.class));
        Spell result = spellService.getOrCreate(name);
        Assert.assertEquals(name, result.getName());
        verify(spellRepository, times(1)).save(any(Spell.class));
    }
}
