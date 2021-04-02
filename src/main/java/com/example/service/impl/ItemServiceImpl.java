package com.example.service.impl;

import com.example.entity.Item;
import com.example.repository.ItemRepository;
import com.example.service.ItemService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Cacheable("items")
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Item getOrCreate(String name) {
        Item item = itemRepository.getByName(name);
        if (item == null) {
            item = new Item(name);
            item = itemRepository.save(item);
        }
        return item;
    }
}
