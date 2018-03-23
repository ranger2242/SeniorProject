package com.ANZR.Ergo.plugin;

import com.intellij.openapi.vfs.VirtualFile;

import java.util.ArrayList;

public class DirectoryElement {
    private String name;
    private ArrayList<DirectoryElement> childElements = new ArrayList<>();
    private ArrayList<AntiPattern> antiPatterns = new ArrayList<>();
    private VirtualFile virtualFile = null;

    public DirectoryElement(String name) {
        this.name = name;
    }

    public DirectoryElement(String name, VirtualFile file) {
        this.name = name;
        this.virtualFile = file;
//        addAntiPattern(new AntiPattern("god", .5));
    }

    public void addFolder(DirectoryElement directoryElement) {
        this.childElements.add(directoryElement);
    }

    public void addAntiPattern(AntiPattern pattern) {
        this.antiPatterns.add(pattern);
    }

    public String getName() {
        return name;
    }

    public ArrayList<DirectoryElement> getChildElements() {
        return childElements;
    }

    public ArrayList<AntiPattern> getAntiPatterns() {
        return antiPatterns;
    }

    public static int getAntiPatternCount(ArrayList<DirectoryElement> directoryElement) {
        int count = 0;
        for (DirectoryElement f : directoryElement) {
            count += (f.getAntiPatterns().size() + getAntiPatternCount(f.getChildElements()));
        }
        return count;
    }

    public VirtualFile getVirtualFile() {
        return virtualFile;
    }

    public boolean isDirectory() {
        return virtualFile == null;
    }

    public boolean isClass() {
        return virtualFile != null;
    }

}
