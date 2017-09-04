package com.knapsack.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Item {

    @Id
    @GeneratedValue
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
