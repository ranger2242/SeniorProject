package com.ANZR.Ergo.transformer;

import com.ANZR.Ergo.parser.ExtractedClass;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.Arrays;
import java.util.Set;

public class BoatAnchorTransformer {

    public static Object[][] generateBoatAnchorMatrix(Set<ExtractedClass> classes) {

        ExtractedClass[] classesArray = classes.toArray(new ExtractedClass[classes.size()]);
        Object[][] boatAnchorMatrix = new Object[classesArray.length][3];

        for (int i = 0; i < classesArray.length; i++) {
            boatAnchorMatrix[i][0] = checkClass(classesArray[i], classesArray);
            boatAnchorMatrix[i][1] = classesArray[i].getName();
            boatAnchorMatrix[i][2] = classesArray[i].getClassPath();
        }

        return boatAnchorMatrix;
    }

    private static int checkClass(ExtractedClass singleClass, ExtractedClass[] classesArray) {

        String[] classNames = new String[classesArray.length];
        for (int i = 0; i < classesArray.length; i++) {
            classNames[i] = classesArray[i].getName();
        }
        int inClass = searchForReferences(singleClass.getTypeObject(), singleClass, classNames);
        int outClass = getReferncesToClass(singleClass, classesArray);

        if (inClass > 0 || outClass > 0) {
            return 1;
        } else {
            return 0;
        }

    }

    private static int searchForReferences(Node node, ExtractedClass targetClass, String[] classNames) {

        int isUsed = 0;

        if (node instanceof VariableDeclarator) {
            VariableDeclarator variableDeclarator = (VariableDeclarator) node;
            if (variableDeclarator.getType() instanceof ClassOrInterfaceType) {
                ClassOrInterfaceType type = variableDeclarator.getType().asClassOrInterfaceType();
                if (Arrays.asList(classNames).contains(type.asString()) && !targetClass.getName().equals(type.asString())) {
                    return 1;
                }
            }
        } else {
            for (Node child : node.getChildNodes()) {
                isUsed += searchForReferences(child, targetClass, classNames);
            }
        }

        if (isUsed > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    private static int getReferncesToClass(ExtractedClass singleClass, ExtractedClass[] classesArray) {

        String[] classNames = new String[classesArray.length];
        for (int i = 0; i < classesArray.length; i++) {
            classNames[i] = classesArray[i].getName();
        }

        int totalCount = 0;
        for (ExtractedClass extractedClass : classesArray) {
            totalCount += searchForReferencesToClass(extractedClass.getTypeObject(), extractedClass, singleClass, classNames);
        }
        return totalCount;
    }

    private static int searchForReferencesToClass(Node node, ExtractedClass currentClass, ExtractedClass targetClass, String[] classNames) {

        int refernces = 0;

        if (node instanceof VariableDeclarator) {
            VariableDeclarator variableDeclarator = (VariableDeclarator) node;
            if (variableDeclarator.getType() instanceof ClassOrInterfaceType) {
                ClassOrInterfaceType type = variableDeclarator.getType().asClassOrInterfaceType();
                if (Arrays.asList(classNames).contains(type.asString()) && targetClass.getName().equals(type.asString())) {
                    return 1;
                }
            }
        } else {
            for (Node child : node.getChildNodes()) {
                refernces += searchForReferencesToClass(child, currentClass, targetClass, classNames);
            }
        }
        if (refernces > 0) {
            return 1;
        } else {
            return 0;
        }
    }

}
