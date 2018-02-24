package com.ANZR.Ergo.parser;

import java.util.ArrayList;

public class ExtractedDirectory {

    private String name;
    private ArrayList<String> classPaths = new ArrayList<>();
    private ArrayList<ExtractedDirectory> packages = new ArrayList<>();

    public ExtractedDirectory(String name) {
        this.name=name;
    }

    public void addPackage(ExtractedDirectory load) {
        packages.add(load);
    }

    /** Setters And Getters */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getClassPaths() {
        return classPaths;
    }

    public void setClassPaths(ArrayList<String> classes) {
        this.classPaths = classes;
    }

    public ArrayList<ExtractedDirectory> getPackages() {
        return packages;
    }

}
