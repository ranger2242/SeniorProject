package com.ANZR.Ergo.io;

import com.ANZR.Ergo.parser.ExtractedClass;
import com.ANZR.Ergo.parser.ProjectData;
import com.ANZR.Ergo.plugin.Folder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

/**
 * Created by Chris Cavazos on 2/5/2018.
 */
public class Logger {

    static boolean enabled=true;

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

    private static void printAllClasses(ProjectData<ArrayList<Set<ExtractedClass>>, ArrayList<Set<com.ANZR.Ergo.parser.Enum>>> classData) {
        for (Set<ExtractedClass> set : classData.parsedClasses) {
            for (ExtractedClass extractedClass : set) {
                if (extractedClass.getParent().equals("")) {
                    extractedClass.printClass(0);
                }
            }
        }
    }

    public static void out(String s) {
        if(enabled)
            System.out.println(s);
    }

    public static void outa(String s) {
        if(enabled)
            System.out.print(s);
    }

    public static void slog(String msg) {
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("MM/dd/yyyy::HH:mm:ss a");
        if(enabled)
            System.out.println(s.format(d) + ":\t" + msg);
    }

    public void printAll(ArrayList<ExtractedClass> classes){
        classes.forEach(x->printClass(x));
    }

    private void printClass(ExtractedClass e){
        Logger.out("class "+e.getName()+"{");
        e.getVariables().forEach(x->x.printAlt());
        e.getMethods().forEach(x->x.printAlt());
        Logger.out("}\n");
    }

}
