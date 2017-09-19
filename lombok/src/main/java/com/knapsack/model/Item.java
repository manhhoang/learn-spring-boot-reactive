package com.knapsack.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int weight;

    private int price;

    public int getWeight() {
        return weight;
    }

    public Item() {}

    public Item(int weight, int price) {
        this.weight = weight;
        this.price = price;
    }
}
