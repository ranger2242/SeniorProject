import java.util.ArrayList;
import java.util.Set;

public class LongMethodTransformer {

    public static Object[][] generateMatrix(Set<ExtractedClass> classes){
        Object[][] matrix = new Object[classes.size()][];

        int i = 0;
        for(ExtractedClass extractedClass : classes){
            ArrayList<Method> methods = extractedClass.getMethods();
            Object[] vector = new Object[methods.size()];
            for(int j = 0; j< methods.size(); j++){
                Method method = methods.get(j);
                String methodBody = method.getMethodDeclaration().getBody().toString();
                int numberOfLines = methodBody.split("\n").length;
                vector[j] = numberOfLines;
            }
            matrix[i] = vector;
            i++;
        }
        return matrix;
    }

}
