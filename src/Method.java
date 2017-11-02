import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Method {

    private String name;
    private String type;
    private ArrayList<Variable> params = new ArrayList<>();
    private ArrayList<Variable> instanceVars = new ArrayList<>();
    private ArrayList<String> operations = new ArrayList<>();
    private MethodDeclaration md = null;

    public Method(MethodDeclaration method) {
        this.name = method.getNameAsString();
        this.type = method.getType().asString();
        this.params = extractParams(method);
        this.instanceVars = extractInstance(method);
        this.md = method;
        parseOperations();
    }


    private static ArrayList<Variable> extractInstance(MethodDeclaration m) {

        ArrayList<Variable> instance = new ArrayList<>();
        Node l = null;

        for (Node n : m.getChildNodes()) {
            if (n instanceof BlockStmt) {
                l = n;
            }
        }
        try {
            List<VariableDeclarationExpr> e = l != null ? l.getChildNodesByType(VariableDeclarationExpr.class) : null;
            for (VariableDeclarationExpr expr : e) {
                VariableDeclarator v = expr.getVariables().get(0);
                instance.add(new Variable(v, expr.getModifiers()));
            }

        } catch (NullPointerException ignored) {

        }
        return instance;
    }

    private static ArrayList<Variable> extractParams(MethodDeclaration m) {

        ArrayList<Variable> params = new ArrayList<>();
        for (Parameter p : m.getParameters()) {
            VariableDeclarator v = new VariableDeclarator(p.getType(), p.getName());
            params.add(new Variable(v, p.getModifiers()));
        }
        return params;

    }

    private void parseOperations() {
        String code = md.getChildNodesByType(BlockStmt.class).toString();
        ArrayList<String> spCode = new ArrayList<>(Arrays.asList(code.split("\r\n")));
        operations = spCode.stream().filter(x -> x.contains(".") || x.contains(" new ")).collect(Collectors.toCollection(ArrayList::new));
    }

    public void print(int depth) {
        String s1 = "" + String.join("", Collections.nCopies(depth + 1, "\t"));
        String s2 = s1 + "\t";

        StringBuilder tag = new StringBuilder(String.join("", Collections.nCopies(depth, "\t")));
        for (Modifier m : md.getModifiers()) {
            tag.append(m.asString()).append(" ");
        }
        tag.append(type).append(" ").append(name);
        Main.out(tag.toString());
        if (params.size() > 0) {
            Main.out(s1 + "Parameters:");
            params.forEach(x -> Main.out(s2 + x.toString()));
            Main.out("");
        }
        if (instanceVars.size() > 0) {
            Main.out(s1 + "Instance:");
            instanceVars.forEach(x -> Main.out(s2 + x.toString()));
            Main.out("");
        }
        if (operations.size() > 0) {
            Main.out(s1 + "Operations:");
            operations.forEach(x -> Main.out(s2 + x));
            Main.out("");

        }
        Main.out(s1 + "--" + name + " END--\n");


    }


    //Setters and Getters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getOperations() {
        return operations;
    }
}
