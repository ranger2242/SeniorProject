package com.ANZR.Ergo.transformer;

import com.ANZR.Ergo.parser.ExtractedClass;
import com.ANZR.Ergo.parser.Variable;
import com.github.javaparser.ast.Modifier;

import java.util.ArrayList;
import java.util.Set;

public class ConstantInterfaceTransformer {

    public static Object[][] generateConstantInterfaceMatrix(Set<ExtractedClass> classes) {

        ArrayList<ExtractedClass> interfaces = new ArrayList<>();
        for (ExtractedClass singleClass : classes) {
            if (singleClass.isInterface()) {
                interfaces.add(singleClass);

            }
        }

        Object[][] matrix = new Object[interfaces.size()][4];


        for (int i = 0; i < interfaces.size(); i++) {
            ExtractedClass singleClass = interfaces.get(i);
            ArrayList<Variable> variables = singleClass.getVariables();
            int onlyFinal = 1;

            for (int j = 0; j < variables.size(); j++) {
                if (!variables.get(j).getModifiers().contains(Modifier.FINAL)) {
                    onlyFinal = 0;
                    break;
                }
            }

            if (variables.size() == 0) {
                onlyFinal = 0;
            }

            matrix[i][0] = singleClass.getMethods().size();
            matrix[i][1] = onlyFinal;
            matrix[i][2] = singleClass.getName();
            matrix[i][3] = singleClass.getClassPath();

        }

        return matrix;
    }

}
