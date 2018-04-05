package com.ANZR.Ergo.plugin;

import com.ANZR.Ergo.io.DataLoader;


public class AntiPattern {

    enum Priority {
        HIGH, LOW
    }

    private String name;
    private String message;
    private Priority priority;


    public AntiPattern(String name, int numberFound) {
        this.name = name;
        this.message = Integer.toString(numberFound);
        setPriority(name);

    }

    public AntiPattern(String name, String message) {
        this.name = name;
        this.message = message;
        setPriority(name);
    }

    private void setPriority(String name) {

        String[] highPriorityPatterns = DataLoader.getHighPriorityAntiPatternNames();

        for (String s : highPriorityPatterns) {
            if (name.equals(s)) {
                priority = Priority.HIGH;
                return;
            }
        }

        priority = Priority.LOW;
    }

    //Getters

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public Priority getPriority() {
        return priority;
    }

}
