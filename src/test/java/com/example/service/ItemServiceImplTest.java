package com.example.service;

import com.example.DotaTestFactory;
import com.example.entity.Item;
import com.example.repository.ItemRepository;
import com.example.service.impl.ItemServiceImpl;
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
public class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    public void testGetOrCreateWhenItemExists() {
        String name = "bottle";
        Item item = DotaTestFactory.createItem(name);
        when(itemRepository.getByName(anyString())).thenReturn(item);
        Item result = itemService.getOrCreate(name);
        Assert.assertEquals(item, result);
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    public void testGetOrCreateWhenItemDoesNotExist() {
        String name = "bottle";
        when(itemRepository.getByName(anyString())).thenReturn(null);
        doAnswer(returnsFirstArg()).when(itemRepository).save(any(Item.class));
        Item result = itemService.getOrCreate(name);
        Assert.assertEquals(name, result.getName());
        verify(itemRepository, times(1)).save(any(Item.class));
    }
}
