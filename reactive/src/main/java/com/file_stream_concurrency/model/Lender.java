package com.file_stream_concurrency.model;

public class Lender {

    private String name;

    private double rate;

    private double available;

    public Lender(){

    }

    public Lender(String name, double rate, double available) {
        this.name = name;
        this.rate = rate;
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getAvailable() {
        return available;
    }

    public void setAvailable(double available) {
        this.available = available;
    }
}
