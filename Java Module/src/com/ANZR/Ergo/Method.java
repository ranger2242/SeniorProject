package com.ANZR.Ergo;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class Method {

    private String name;
    private String type;
    private ArrayList<Variable> params = new ArrayList<>();
    private ArrayList<Variable> instanceVars = new ArrayList<>();
    private ArrayList<Expression> ops= new ArrayList<>();
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
        if (!md.getBody().isPresent()) {
            return;
        }

        ArrayList<Expression> calls = new ArrayList<>();
        BlockStmt code = md.getBody().get();
        for (Node s : code.getStatements()) {
            calls.addAll(lookThroughChildren(s));
        }
        ops= calls;

        //Print stuff
        /*Ergo.out(name);
        for (Expression expression : calls) {

            if (expression instanceof MethodCallExpr) {
                MethodCallExpr methodCallExpr = expression.asMethodCallExpr();
                String methodName = methodCallExpr.getNameAsString();
                if (methodCallExpr.getScope().isPresent()) {
                    String objectName = methodCallExpr.getScope().get().toString();
                    Ergo.out("---" + objectName + "." + methodName + methodCallExpr.getArguments().toString());
                } else {
                    Ergo.out("---" + methodName + methodCallExpr.getArguments().toString());
                }
            }else if (expression instanceof FieldAccessExpr) {
                FieldAccessExpr fieldAccessExpr = expression.asFieldAccessExpr();
                Ergo.out("---"  + fieldAccessExpr.toString());
            }

        }
*/
    }

    private ArrayList<Expression> lookThroughChildren(Node node) {
        ArrayList<Expression> expressions = new ArrayList<>();
        MethodCallExpr methodCallExpr = node instanceof MethodCallExpr ? ((MethodCallExpr) node) : null;
        FieldAccessExpr fieldAccessExpr = node instanceof FieldAccessExpr ? ((FieldAccessExpr) node) : null;

        if ((methodCallExpr == null) && (fieldAccessExpr == null)) {
            for (Node child : node.getChildNodes()) {
                expressions.addAll(lookThroughChildren(child));
            }
        } else if (methodCallExpr != null) {
            expressions.add(methodCallExpr);
        }else if (fieldAccessExpr != null)  {
            expressions.add(fieldAccessExpr);
        }

        return expressions;
    }

    public void print(int depth) {
        String s1 = "" + String.join("", Collections.nCopies(depth + 1, "\t"));
        String s2 = s1 + "\t";

        StringBuilder tag = new StringBuilder(String.join("", Collections.nCopies(depth, "\t")));
        for (Modifier m : md.getModifiers()) {
            tag.append(m.asString()).append(" ");
        }
        tag.append(type).append(" ").append(name);
        Logger.out(tag.toString());
        if (params.size() > 0) {
            Logger.out(s1 + "Parameters:");
            params.forEach(x -> Logger.out(s2 + x.toString()));
            Logger.out("");
        }
        if (instanceVars.size() > 0) {
            Logger.out(s1 + "Instance:");
            instanceVars.forEach(x -> Logger.out(s2 + x.toString()));
            Logger.out("");
        }
        if (ops.size() > 0) {
            Logger.out(s1 + "Operations:");
            ops.forEach(x -> Logger.out(s2 + x));
            Logger.out("");

        }
        Logger.out(s1 + "--" + name + " END--\n");


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

    public ArrayList<Expression> getOperations() {
        return ops;
    }

    public MethodDeclaration getMethodDeclaration() {
        return md;
    }

    public void printAlt() {
        String s = "";
        EnumSet<Modifier> modifiers = md.getModifiers();
        if (modifiers.contains(Modifier.PRIVATE) ) {
            s += "-";
        }
        if( modifiers.contains(Modifier.PROTECTED)){
            s += "#";
        }
        if( modifiers.contains(Modifier.PUBLIC)){
            s += "+";
        }


        s += name + "(";
        for (int i = 0; i < params.size(); i++) {
            Variable v = params.get(i);
            s += v.getType();
            if (i < params.size() - 1) {
                s += ", ";
            }
        }
        s += "):" + type;
        Logger.out(s);
    }


}
