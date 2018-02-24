package com.ANZR.Ergo.transformer;

import com.ANZR.Ergo.parser.ExtractedClass;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.Arrays;
import java.util.Set;

import static com.ANZR.Ergo.io.Logger.slog;

class GodObjectTransformer {

    public static Object[][] generateGodObjectMatrix(Set<ExtractedClass> classes) {

        ExtractedClass[] classesArray = classes.toArray(new ExtractedClass[classes.size()]);
        Object[][] godMatrix = new Object[classesArray.length][6];
        int totalReferencesInProject = getTotalNumberOfReferences(classes);

        for (int i = 0; i < classesArray.length; i++) {
            ExtractedClass singleClass = classesArray[i];
            godMatrix[i][0] = getReferncesFromClass(classes, singleClass); // gets number of refernces from class to others
            godMatrix[i][1] = singleClass.getMethods().size(); // number of methods
            godMatrix[i][2] = singleClass.getVariables().size(); // number of varibles
            godMatrix[i][3] = totalReferencesFromClassReferncesRatio(totalReferencesInProject, (int) godMatrix[i][2]);
            godMatrix[i][4] = singleClass.getName();
            godMatrix[i][5] = singleClass.getClassPath();
        }
        return godMatrix;

    }

    private static double totalReferencesFromClassReferncesRatio(int total, int referncesFromClass) {
        if (total == 0) {
            return 0;
        }
        return (double) referncesFromClass / (double) total;
    }

    private static int getTotalNumberOfReferences(Set<ExtractedClass> classes) {
        ExtractedClass[] classesArray = classes.toArray(new ExtractedClass[classes.size()]);
        int total = 0;

        for (ExtractedClass extractedClass : classesArray) {
            total += getReferncesFromClass(classes, extractedClass);
        }
        return total;
    }

    private static int getReferncesFromClass(Set<ExtractedClass> classes, ExtractedClass singleClass) {
        ExtractedClass[] classesArray = classes.toArray(new ExtractedClass[classes.size()]);
        String[] classNames = new String[classesArray.length];
        for (int i = 0; i < classesArray.length; i++) {
            classNames[i] = classesArray[i].getName();
        }
        return searchForReferences(singleClass.getTypeObject(), singleClass, classNames);
    }

    private static int searchForReferences(Node node, ExtractedClass targetClass, String[] classNames) {

        int refernces = 0;

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
                refernces += searchForReferences(child, targetClass, classNames);
            }
        }
        return refernces;
    }


}
