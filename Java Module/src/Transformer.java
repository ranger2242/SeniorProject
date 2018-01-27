import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.*;

public class Transformer {

    private static Set<ExtractedClass> classes = new LinkedHashSet<>();
    private static Set<Enum> enums = new LinkedHashSet<>();


    public Transformer(Set<ExtractedClass> classes, Set<Enum> enums){
        this.classes = classes;
        this.enums = enums;
    }

    // Method not yet implemented
    public static double[][][] transform(){
        double[][][] data = new double[1][][];
        data[0] = GodObjectTransformer.generateGodObjectMatrix(classes);
        return data;
    }

    private static String getClassString(ExtractedClass extractedClass){
        return extractedClass.getTypeObject().toString();
    }

    private static List<Node> searchForFieldAccessExpr(Node node){
        List<Node> nodes = new ArrayList<>();

        if( node instanceof FieldAccessExpr){
            nodes.add(node);
            return nodes;
        }

        for (Node child : node.getChildNodes()) {
            nodes.addAll(searchForFieldAccessExpr(child));
        }

        return nodes;
    }


}











