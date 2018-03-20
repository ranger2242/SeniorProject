package com.ANZR.Ergo.plugin;

public class AntiPattern {
    private String name;
    private String message;

    public AntiPattern(String name, int numberFound) {
        this.name = name;
        this.message = Integer.toString(numberFound);
    }

    public AntiPattern(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

}
