package com.ANZR.Ergo;

import java.util.ArrayList;

/**
 * Created by Chris Cavazos on 10/17/2017.
 */
public class ExtractedDir {

    private String name;
    private ArrayList<String> classPaths = new ArrayList<>();
    private ArrayList<ExtractedDir> packages = new ArrayList<>();

    public ExtractedDir() {

    }

    public ExtractedDir(String name) {
        this.name=name;
    }

    public ExtractedDir(String name, ArrayList<String> classes) {
        this.name = name;
        this.classPaths = classes;
    }

    public void addPackage(ExtractedDir load) {
        packages.add(load);
    }


    //Setters And Getters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getClassPaths() {
        return classPaths;
    }

    public void setClasses(ArrayList<String> classes) {
        this.classPaths = classes;
    }

    public ArrayList<ExtractedDir> getPackages() {
        return packages;
    }

    public void setPackages(ArrayList<ExtractedDir> packages) {
        this.packages = packages;
    }

}
