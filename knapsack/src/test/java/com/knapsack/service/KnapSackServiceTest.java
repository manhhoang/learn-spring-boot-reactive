package com.knapsack.service;

import com.knapsack.model.Item;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class KnapSackServiceTest {

    @InjectMocks
    private KnapSackService knapSackService;

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
        double capacity = 5;
        List<Item> items = new ArrayList<>();
        Item item = new Item();
        items.add(item);
        List<Item> results = knapSackService.getOptimizer(capacity, items);
    }
}
