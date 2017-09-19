package com.knapsack.service;

import com.knapsack.model.Item;
import com.knapsack.repository.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class KnapSackServiceTest {

    @InjectMocks
    private KnapSackService knapSackService;

    @Mock
    private ItemRepository itemRepository;

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMockCreation() {
        assertNotNull(knapSackService);
    }

    @Test
    public void testGetOptimizer() {
        int capacity = 5;
        List<Item> items = new ArrayList<>();
        Item item = new Item(4, 6);
        items.add(item);
        item = new Item(3, 10);
        items.add(item);
        when(itemRepository.save(items)).thenReturn(null);
        List<Item> results = knapSackService.optimizing(capacity, items);
        assertEquals(3, results.get(0).getWeight());
        assertEquals(10, results.get(0).getPrice());
    }
}
