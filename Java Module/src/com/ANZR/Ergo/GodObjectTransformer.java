package com.ANZR.Ergo;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class GodObjectTransformer {

    public static Object[][] generateGodObjectMatrix(Set<ExtractedClass> classes){

        ExtractedClass[] classesArray =  classes.toArray(new ExtractedClass[classes.size()]);
        Object[][] godMatrix = new Object[classesArray.length][4];
        int totalReferencesInProject = getTotalNumberOfReferences(classes);

        for( int i = 0; i < classesArray.length; i++ ){
            ExtractedClass singleClass = classesArray[i];
            godMatrix[i][0] = getReferncesFromClass(classes, singleClass); // gets number of refernces from class to others
            godMatrix[i][1] = singleClass.getMethods().size(); // number of methods
            godMatrix[i][2] = singleClass.getVariables().size(); // number of varibles
            godMatrix[i][3] = totalReferencesFromClassReferncesRatio( totalReferencesInProject, (int) godMatrix[i][2]);
//            godMatrix[i][4] = singleClass.getName();
        }
        return godMatrix;

    }

    public ArrayList<Object[][][]> transformTrainingData(
            ProjectData<ArrayList<Set<ExtractedClass>>, ArrayList<Set<Enum>>> arr){
        ArrayList<Object[][][]> matrices = new ArrayList<>();
        for (Set<ExtractedClass> set : arr.parsedClasses) {
            Transformer transformer = new Transformer(set, arr.globalEnums.get(arr.parsedClasses.indexOf(set)));
            matrices.add(transformer.transform());
        }
        Logger.slog("Data Transformed");
        return matrices;
    }

    public Object[][][] transformData(
            ProjectData<ArrayList<Set<ExtractedClass>>, ArrayList<Set<Enum>>> projectClassData){
        Set<ExtractedClass> set = projectClassData.parsedClasses.get(0);
        Transformer transformer = new Transformer(set, projectClassData.globalEnums.get(projectClassData.parsedClasses.indexOf(set)));
        Object[][][] matrices = transformer.transform();
        Logger.slog("Data Transformed");
        return matrices;
    }

    private static double totalReferencesToClassReferncesRatio(int total, int referncesToClass){
        if(total == 0){
            return 0;
        }
        return (double) referncesToClass / (double) total;
    }

    private static double totalReferencesFromClassReferncesRatio(int total, int referncesFromClass){
        if(total == 0){
            return 0;
        }
        return (double) referncesFromClass / (double) total;
    }

    private static int getTotalNumberOfReferences(Set<ExtractedClass> classes){
        ExtractedClass[] classesArray =  classes.toArray(new ExtractedClass[classes.size()]);
        int total = 0;

        for (ExtractedClass extractedClass : classesArray) {
            total += getReferncesFromClass(classes, extractedClass);
        }
        return total;
    }

    private static int getReferncesFromClass(Set<ExtractedClass> classes, ExtractedClass singleClass){
        ExtractedClass[] classesArray =  classes.toArray(new ExtractedClass[classes.size()]);
        String[] classNames = new String[classesArray.length];
        for (int i = 0; i < classesArray.length; i++) {
            classNames[i] = classesArray[i].getName();
        }
        return searchForReferences(singleClass.getTypeObject(), singleClass, classNames);
    }

    private static int searchForReferences(Node node, ExtractedClass targetClass, String[] classNames){

        int refernces = 0;

        if(node instanceof VariableDeclarator){
            VariableDeclarator variableDeclarator = (VariableDeclarator) node;
            if(variableDeclarator.getType() instanceof ClassOrInterfaceType){
                ClassOrInterfaceType type = variableDeclarator.getType().asClassOrInterfaceType();
                if ( Arrays.asList(classNames).contains(type.asString()) && !targetClass.getName().equals(type.asString()) ){
                    return 1;
                }
            }
        }else{
            for(Node child: node.getChildNodes()){
                refernces += searchForReferences(child, targetClass, classNames);
            }
        }
        return refernces;
    }

    private static int getReferncesToClass(Set<ExtractedClass> classes, ExtractedClass singleClass){
        ExtractedClass[] classesArray =  classes.toArray(new ExtractedClass[classes.size()]);
        String[] classNames = new String[classesArray.length];
        for (int i = 0; i < classesArray.length; i++) {
            classNames[i] = classesArray[i].getName();
        }

        int totalCount = 0;
        for (ExtractedClass extractedClass : classesArray) {
            totalCount += searchForReferencesToClass(extractedClass.getTypeObject(), extractedClass ,singleClass, classNames);
        }

        return totalCount;
    }

    private static int searchForReferencesToClass(Node node, ExtractedClass currentClass, ExtractedClass targetClass, String[] classNames){

        int refernces = 0;

        if(node instanceof VariableDeclarator){
            VariableDeclarator variableDeclarator = (VariableDeclarator) node;
            if(variableDeclarator.getType() instanceof ClassOrInterfaceType){
                ClassOrInterfaceType type = variableDeclarator.getType().asClassOrInterfaceType();
                if ( Arrays.asList(classNames).contains(type.asString()) && targetClass.getName().equals(type.asString()) ){
                    return 1;
                }
            }
        }else{
            for(Node child: node.getChildNodes()){
                refernces += searchForReferencesToClass(child, currentClass, targetClass, classNames);
            }
        }
        return refernces;
    }


}
