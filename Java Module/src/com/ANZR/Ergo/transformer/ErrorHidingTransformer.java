package com.ANZR.Ergo.transformer;

import com.ANZR.Ergo.parser.ExtractedClass;
import com.ANZR.Ergo.parser.Method;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.TryStmt;

import java.util.Set;

public class ErrorHidingTransformer {

    public static Object[][] generateErrorHidingMatrix(Set<ExtractedClass> classes) {

        ExtractedClass[] classesArray = classes.toArray(new ExtractedClass[classes.size()]);
        Object[][] errorHidingMatrix = new Object[classesArray.length][3];


        for(int i = 0; i < classesArray.length; i++){
            errorHidingMatrix[i][0] = checkClass(classesArray[i]);
            errorHidingMatrix[i][1] = classesArray[i].getName();
            errorHidingMatrix[i][2] = classesArray[i].getClassPath();
        }

        return errorHidingMatrix;
    }


    private static int checkClass(ExtractedClass singleClass){
        int numberFound = 0;
        for(Method method : singleClass.getMethods()){
            if (checkMethod(method)){
                numberFound++;
            }
        }
        return numberFound;

    }


    private static boolean checkMethod(Method method){

        if(!method.getMethodDeclaration().getBody().isPresent()){
            //No method body...?
            return false;
        }

        NodeList<Statement> statements = method.getMethodDeclaration().getBody().get().getStatements();

        for(Statement statement : statements){
            if(statement.isTryStmt()){
                TryStmt tryStmt = statement.asTryStmt();
                NodeList<CatchClause> catchClause = tryStmt.getCatchClauses();

                for(CatchClause clause : catchClause){
                    if (clause.getBody().getStatements().size() == 0){
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
