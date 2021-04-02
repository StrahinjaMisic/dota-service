package com.example.service;

import com.example.entity.Item;

public interface ItemService {

    Item getOrCreate(String name);
}
