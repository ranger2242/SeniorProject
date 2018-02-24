package com.ANZR.Ergo.plugin;

import com.intellij.openapi.vfs.VirtualFile;
import java.util.ArrayList;

public class Folder {
    private String name;
    private ArrayList<Folder> folders = new ArrayList<>();
    private ArrayList<AntiPattern> antiPatterns = new ArrayList<>();
    private VirtualFile virtualFile = null;

    public Folder() {}

    public Folder(String name) {
        this.name = name;
    }

    public Folder(String name,VirtualFile file) {
        this.name = name;
        this.virtualFile = file;
//        addAntiPattern(new AntiPattern("god", .5));
    }

    public void addFolder(Folder folder){
        this.folders.add(folder);
    }

    public void addAntiPattern(AntiPattern pattern){
        this.antiPatterns.add(pattern);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Folder> getFolders() {
        return folders;
    }

    public ArrayList<AntiPattern> getAntiPatterns() {
        return antiPatterns;
    }

    public boolean isClass() {
        return virtualFile != null;
    }

    public static int getAntiPatternCount(ArrayList<Folder> folder){
        int temp = 0;
        for (Folder f : folder) {
            temp += (f.getAntiPatterns().size() + getAntiPatternCount(f.getFolders()));
        }
        return temp;
    }

    public VirtualFile getVirtualFile() {
        return virtualFile;
    }

}
