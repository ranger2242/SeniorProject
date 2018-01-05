import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.FieldAccessExpr;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Transformer {

    private static Set<ExtractedClass> classes = new LinkedHashSet<>();
    private static Set<Enum> enums = new LinkedHashSet<>();

    public Transformer(Set<ExtractedClass> classes, Set<Enum> enums){
        this.classes = classes;
        this.enums = enums;
    }

    // Method not yet implemented
    public static int[][][] transform(){

        int[][] encapsulationMatrix = generateEncapsulationMatrix();

        int[][][] dummyData = {{{4, 5, 7,}, {2, 8, 9}}};
        return dummyData;
    }

    private static int[][] generateEncapsulationMatrix(){
        ArrayList<ExtractedClass> extractedClasses  = new ArrayList<>(classes);
        int[][] matrix = new int[extractedClasses.size()][extractedClasses.size()];

        for( int i = 0; i < extractedClasses.size(); i++){
            ExtractedClass singleClass = extractedClasses.get(i);
            List<Node> accessExprs = searchForFieldAccessExpr(singleClass.getTypeObject());

            //Make matrix here
            


        }

        return matrix;
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











