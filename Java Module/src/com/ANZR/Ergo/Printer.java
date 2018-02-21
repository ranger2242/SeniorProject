package com.ANZR.Ergo;

import java.util.ArrayList;

public class Printer {

    public static void printProjectFiles(Folder root) {
        printFile(root, 0);
    }

    private static void printFile(Folder folder, int numTab){
        ArrayList<Folder> folders = folder.getFolders();

        if(folder.isClass())
            System.out.println(getTabString(numTab) + "Class: " + folder.getName());
        else System.out.println(getTabString(numTab) + folder.getName());
        for (Folder f : folders) {
            printFile(f, numTab + 1);
        }
    }

    private static String getTabString(int numberOfTabs){
        String tab = "";
        for (int i =0; i < numberOfTabs; i++)
            tab = tab.concat("\t");
        return tab;
    }


}
