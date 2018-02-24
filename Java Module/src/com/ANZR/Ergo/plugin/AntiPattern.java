package com.ANZR.Ergo.plugin;

public class AntiPattern {
    private String name;
    private int numberFound;

    public AntiPattern(String name, int numberFound){
        this.name = name;
        this.numberFound = numberFound;
    }


    public String getName() {
        return name;
    }

    public int getNumberFound() {
        return numberFound;
    }

}
