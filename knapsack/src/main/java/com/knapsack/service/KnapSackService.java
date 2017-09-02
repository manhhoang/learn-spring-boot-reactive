package com.knapsack.service;

import com.knapsack.model.Item;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KnapSackService {

    public List<Item> optimizing(double capacity, List<Item> items) {
        List<Item> retItems = new ArrayList<>();
        for(Item it: items) {
            retItems.add(it);
        }
        return retItems;
    }
}
