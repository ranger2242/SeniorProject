import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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
        Variable[][] vars = extractVariables(method);
        return new Method(name, type, vars[0], vars[1]);
    }


    private static Variable[][] extractVariables(MethodDeclaration m) {
        ArrayList<Variable> params = new ArrayList<>();
        for (Parameter p : m.getParameters()) {
            params.add(new Variable(p.getType().asString(), p.getName().asString()));
        }
        ArrayList<Variable> instance = new ArrayList<>();
        for (Node n : m.getChildNodes()) {
            if (n instanceof BlockStmt) {
                instance = ClassBuilder.findVariables(new ArrayList<>(Collections.singletonList(n.toString())));
            }
        }

        Variable[] v1 = Arrays.copyOf(params.toArray(), params.size(), Variable[].class);
        Variable[] v2 = Arrays.copyOf(instance.toArray(), instance.size(), Variable[].class);
        return new Variable[][]{v1, v2};

    }

}
