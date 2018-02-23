package com.ANZR.Ergo.parser;


import com.ANZR.Ergo.io.FileHandler;
import com.ANZR.Ergo.io.Logger;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Parser {

    private ArrayList<ExtractedClass> extractedClasses = new ArrayList<>();
    private final ArrayList<Enum> globalEnums = new ArrayList<>();

    private Parser(ExtractedDir dir) {
        parseDirectory(dir);
    }

    public static ProjectData<Set<ExtractedClass>, Set<Enum>> parseProject(ExtractedDir[] directories){
        Set<ExtractedClass> classes = new LinkedHashSet<>();
        Set<Enum> globalEnums = new LinkedHashSet<>();
        ProjectData<Set<ExtractedClass>, Set<Enum>> projectData = new ProjectData<>(classes, globalEnums);

        for (ExtractedDir directory : directories){
            ProjectData<Set<ExtractedClass>, Set<Enum>> directoryData = parseClientDirectory(directory);
            projectData.parsedClasses.addAll(directoryData.parsedClasses);
            projectData.globalEnums.addAll(directoryData.globalEnums);
        }
        return projectData;
    }

    public static ProjectData<Set<ExtractedClass>, Set<Enum>> parseClientDirectory(ExtractedDir directory){
        Set<ExtractedClass> parsedClasses = new LinkedHashSet<>();
        Set<Enum> globalEnums = new LinkedHashSet<>();

        try {
            Parser parser = new Parser(directory);
            parsedClasses = new LinkedHashSet<>(parser.getExtractedClasses());
            globalEnums = new LinkedHashSet<>(parser.getGlobalEnums());

        }catch (ParseProblemException e) {
            Logger.slog("!!!!!!!!!!!!!!!!!!!!!!!\nPARSEERROR\n!!!!!!!!!!!!!!!!!!!!!!!!!");
        }

        return new ProjectData<>(parsedClasses,globalEnums);
    }

    private void parseDirectory(ExtractedDir e) {

        for (String s : e.getClassPaths()) {
            System.out.println(s);
            ArrayList<ExtractedClass> newClasses = parseCode(s);
            if (newClasses != null) {
                extractedClasses.addAll(newClasses);
            }
        }
        e.getPackages().forEach(this::parseDirectory);
    }


    public static ProjectData<ArrayList<Set<ExtractedClass>>, ArrayList<Set<Enum>>> parseDirectories(ArrayList<ExtractedDir> directories){
        ArrayList<Set<ExtractedClass>> parsedClasses = new ArrayList<>();
        ArrayList<Set<Enum>> globalEnums = new ArrayList<>();
        for (ExtractedDir dir : directories) {
            try {
                Parser parser = new Parser(dir);
                parsedClasses.add(new LinkedHashSet<>(parser.getExtractedClasses()));
                globalEnums.add(new LinkedHashSet<>(parser.getGlobalEnums()));

            }catch (ParseProblemException e) {
                Logger.slog("!!!!!!!!!!!!!!!!!!!!!!!\nPARSEERROR\n!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
        }
        return new ProjectData<>(parsedClasses,globalEnums);
    }


    private ArrayList<ExtractedClass> parseCode(String dir) {
        String code = FileHandler.load(new File(dir));
        ArrayList<ExtractedClass> listOfClasses = new ArrayList<>();
        try {
            //System.out.println("PROCESSING: " + dir);
            CompilationUnit cu = JavaParser.parse(code);
            NodeList<TypeDeclaration<?>> classes = cu.getTypes();
            NodeList<ImportDeclaration> imports = cu.getImports();

            for (TypeDeclaration<?> cl : classes) {
                if (cl.isEnumDeclaration()) {
                    globalEnums.add(new Enum(cl.asEnumDeclaration()));
                } else if (cl.isClassOrInterfaceDeclaration()) {
                    //Assumed a class or interface
                    ExtractedClass newClass = new ExtractedClass(cl, dir);
                    newClass.setImports(imports);
                    listOfClasses.addAll(createClassList(newClass));
                }
            }
            return listOfClasses;
        } catch (ParseProblemException e) {
            System.out.println("PARSE ERROR: " + dir);
            //e.printStackTrace();
            return null;
        }catch (StackOverflowError e) {
            System.out.println("STACK OVERFLOW ERROR: " + dir);
            // e.printStackTrace();
            return null;
        }
    }

    private ArrayList<ExtractedClass> createClassList(ExtractedClass extractedClass) {

        ArrayList<ExtractedClass> list = new ArrayList<>();
        list.add(extractedClass);

        for (ExtractedClass r : extractedClass.getClasses()) {
            list.addAll(createClassList(r));
        }
        return list;
    }

    private void printAllChildNodes(ClassOrInterfaceDeclaration cid) {
        cid.getChildNodes().forEach(x -> Logger.out(x.getClass().toString()));
    }

    private static String listToLine(ArrayList<String> code) {
        StringBuilder in = new StringBuilder();

        for (String line : code) {
            in.append(line).append("\r\n");
        }
        return in.toString();
    }

    private static ArrayList<String> removeWhiteSpace(ArrayList<String> strings) {
        return strings.stream().filter(x -> x.length() > 0).collect(Collectors.toCollection(ArrayList::new));
    }

    //Setters And Getters
    public ArrayList<ExtractedClass> getExtractedClasses() {
        return extractedClasses;
    }

    public void setExtractedClasses(ArrayList<ExtractedClass> extractedClasses) {
        this.extractedClasses = extractedClasses;
    }

    public ArrayList<Enum> getGlobalEnums() {
        return globalEnums;
    }


}
