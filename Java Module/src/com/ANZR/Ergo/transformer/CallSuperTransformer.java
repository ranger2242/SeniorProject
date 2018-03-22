package com.ANZR.Ergo.transformer;

import com.ANZR.Ergo.parser.ExtractedClass;
import com.ANZR.Ergo.parser.Method;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;

import java.util.Set;

public class CallSuperTransformer {

    public static Object[][] generateCallSuperMatrix(Set<ExtractedClass> classes) {

        ExtractedClass[] classesArray = classes.toArray(new ExtractedClass[classes.size()]);
        Object[][] callSuperMatrix = new Object[classesArray.length][3];

        for (int i = 0; i < classesArray.length; i++) {
            callSuperMatrix[i][0] = checkClass(classesArray[i]);
            callSuperMatrix[i][1] = classesArray[i].getName();
            callSuperMatrix[i][2] = classesArray[i].getClassPath();
        }


        return callSuperMatrix;
    }

    private static int checkClass(ExtractedClass singleClass) {
        int numberFound = 0;
        for (Method method : singleClass.getMethods()) {
            if (checkMethod(method)) {
                numberFound++;
            }
        }
        return numberFound;

    }


    private static boolean checkMethod(Method method) {

        if (!method.getMethodDeclaration().getBody().isPresent()) {
            //No method body...?
            return false;
        }

        NodeList<Statement> statements = method.getMethodDeclaration().getBody().get().getStatements();

        for (Statement statement : statements) {
            if (statement.isExpressionStmt()) {
                ExpressionStmt expressionStmt = statement.asExpressionStmt();
                Expression expression = expressionStmt.getExpression();

                if (expression.isMethodCallExpr()) {
                    MethodCallExpr methodCallExpr = expression.asMethodCallExpr();
                    if (methodCallExpr.getScope().isPresent()) {
                        if (methodCallExpr.getScope().get().isSuperExpr()) {
                            if (methodCallExpr.getName().toString().equals(method.getName())) {
                                return true;
                            }
                        }
                    }
                }
            }

        }
        return false;
    }

}

