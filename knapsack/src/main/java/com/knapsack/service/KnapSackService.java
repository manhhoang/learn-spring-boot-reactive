package com.knapsack.service;

import com.knapsack.model.Item;
import com.knapsack.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KnapSackService {

    private final ItemRepository itemRepository;

    @Autowired
    public KnapSackService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> optimizing(int capacity, List<Item> items) {
        int N = items.size();
        items.add(0, new Item());
        int[][] opt = new int[N + 1][capacity + 1];
        boolean[][] sol = new boolean[N + 1][capacity + 1];

        for (int i = 1; i <= N; i++) {
            for (int j = 0; j <= capacity; j++) {
                int opt1 = opt[i - 1][j];
                int opt2 = Integer.MIN_VALUE;
                if (j >= items.get(i).getWeight())
                    opt2 = opt[i - 1][j - items.get(i).getWeight()] + items.get(i).getPrice();

                opt[i][j] = Math.max(opt1, opt2);
                sol[i][j] = opt2 > opt1;
            }
        }

        int[] selected = new int[N + 1];
        for (int n = N, w = capacity; n > 0; n--) {
            if (sol[n][w]) {
                selected[n] = 1;
                w = w - items.get(n).getWeight();
            } else
                selected[n] = 0;
        }

        List<Item> retItems = new ArrayList<>();
        for (int i = 1; i < N + 1; i++) {
            if (selected[i] == 1) {
                retItems.add(items.get(i));
            }
        }
        this.itemRepository.save(retItems);
        return retItems;
    }
}
