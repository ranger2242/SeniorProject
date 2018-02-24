package com.ANZR.Ergo.plugin;

public class AntiPattern {
    private String name;
    private double percent;

    public String getName() {
        return name;
    }

    public double getPercent() {
        return percent;
    }

    public AntiPattern(String name, double percent){
        this.name = name;
        this.percent = percent;
    }
}
