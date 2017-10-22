import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class MethodBuilder {

    private MethodBuilder() {
    }

    public static ArrayList<Method> parseClass(TypeDeclaration<?> inputClass) {
        ArrayList<Method> output = new ArrayList<>();
        for (int i = 0; i < inputClass.getMethods().size(); i++) {
            output.add(create(inputClass.getMethods().get(i)));
        }
        return output;
    }

    private static Method create(MethodDeclaration method) {
        String name = method.getNameAsString();
        String type = method.getType().asString();
        Variable[][] vars = extractMethodVariables(method);
        return new Method(name, type, vars[0], vars[1],method);
    }


    private static Variable[][] extractMethodVariables(MethodDeclaration m) {

        ArrayList<Variable> params = new ArrayList<>();
        for (Parameter p : m.getParameters()) {
            params.add(new Variable(p.getType().asString(), p.getName().asString(),p.getModifiers()));
        }
        ArrayList<Variable> instance = new ArrayList<>();
        Node l = null;

        for (Node n : m.getChildNodes()) {
            if (n instanceof BlockStmt) {
                l = n;
            }
        }
        try {
            List<VariableDeclarationExpr> e = l.getChildNodesByType(VariableDeclarationExpr.class);
            for (VariableDeclarationExpr expr : e) {
                VariableDeclarator v = expr.getVariables().get(0);
                instance.add(new Variable(v.getType().toString(), v.getName().asString(), expr.getModifiers()));
            }

        }catch (NullPointerException e){

        }
        Variable[] v1 = Arrays.copyOf(params.toArray(), params.size(), Variable[].class);
        Variable[] v2 = Arrays.copyOf(instance.toArray(), instance.size(), Variable[].class);
        return new Variable[][]{v1, v2};

    }

    public static ArrayList<Variable> findInstanceVariables(String rawCode) {
        ArrayList<Variable> variables = new ArrayList<>();

        String[] lines = rawCode.split("\r\n");
        for (String line : lines) {
            List<String> words = new ArrayList<>(Arrays.asList(line.split(" ")));
            while (words.contains("")) {
                words.remove("");
            }
            for (int i = 0; i < words.size(); i++) {
                if (words.get(i).equals("=") && i > 1) {
                    variables.add(new Variable(words.get(i - 2), words.get(i - 1),null));
                }
            }
            if (words.size() == 2) {
                variables.add(new Variable(words.get(0), words.get(1),null));
            }
        }
        return variables;

    }

}
