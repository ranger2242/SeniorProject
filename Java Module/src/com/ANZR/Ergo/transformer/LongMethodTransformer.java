package com.ANZR.Ergo.transformer;

import com.ANZR.Ergo.parser.ExtractedClass;
import com.ANZR.Ergo.parser.Method;

import java.util.ArrayList;
import java.util.Set;

class LongMethodTransformer {

    public static Object[][] generateMatrix(Set<ExtractedClass> classes) {
        Object[][] matrix = new Object[classes.size()][];

        int i = 0;
        for (ExtractedClass extractedClass : classes) {
            ArrayList<Method> methods = extractedClass.getMethods();
            Object[] vector = new Object[methods.size() + 2];
            for (int j = 0; j < methods.size(); j++) {
                Method method = methods.get(j);
                String methodBody = method.getMethodDeclaration().getBody().toString();
                int numberOfLines = methodBody.split("\n").length;
                vector[j] = numberOfLines;
            }
            vector[vector.length - 2] = extractedClass.getName();
            vector[vector.length - 1] = extractedClass.getClassPath();

            matrix[i] = vector;
            i++;
        }
        return matrix;
    }

}
