import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class GodObjectTransformer {

    public static double[][] generateGodObjectMatrix(Set<ExtractedClass> classes){

        ExtractedClass[] classesArray =  classes.toArray(new ExtractedClass[classes.size()]);
        double[][] godMatrix = new double[classesArray.length][8];

        int totalReferencesInProject = getTotalNumberOfReferences(classes);

        for( int i = 0; i < classesArray.length; i++ ){

            ExtractedClass singleClass = classesArray[i];
            godMatrix[i][0] = classesArray.length; //Number of Classes
            godMatrix[i][1] = getReferncesToClass(classes, singleClass); // gets number of refernces from other classes
            godMatrix[i][2] = getReferncesFromClass(classes, singleClass); // gets number of refernces from class to others
            godMatrix[i][3] = singleClass.getMethods().size(); // number of methods
            godMatrix[i][4] = singleClass.getVariables().size(); // number of varibles
            godMatrix[i][5] = (double) totalReferencesInProject;
            godMatrix[i][6] = totalReferencesToClassReferncesRatio( totalReferencesInProject, (int) godMatrix[i][1]);
            godMatrix[i][7] = totalReferencesFromClassReferncesRatio( totalReferencesInProject, (int) godMatrix[i][2]);

        }
        
        return godMatrix;

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
