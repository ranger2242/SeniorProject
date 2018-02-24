package com.ANZR.Ergo.parser;

import com.ANZR.Ergo.io.Logger;
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
    private ArrayList<Variable> parameters;
    private ArrayList<Variable> instanceVariables;
    private ArrayList<Expression> operations;
    private MethodDeclaration methodDeclaration;

    Method(MethodDeclaration method) {
        this.name = method.getNameAsString();
        this.type = method.getType().asString();
        this.parameters = extractParams(method);
        this.instanceVariables = extractInstance(method);
        this.methodDeclaration = method;
        this.operations = parseOperations();
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

    private ArrayList<Expression> parseOperations() {
        if (!methodDeclaration.getBody().isPresent()) {
            return new ArrayList<>();
        }

        ArrayList<Expression> calls = new ArrayList<>();
        BlockStmt code = methodDeclaration.getBody().get();
        for (Node s : code.getStatements()) {
            calls.addAll(lookThroughChildren(s));
        }
        return calls;

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
        } else if (fieldAccessExpr != null) {
            expressions.add(fieldAccessExpr);
        }

        return expressions;
    }

    public void print(int depth) {
        String s1 = "" + String.join("", Collections.nCopies(depth + 1, "\t"));
        String s2 = s1 + "\t";

        StringBuilder tag = new StringBuilder(String.join("", Collections.nCopies(depth, "\t")));
        for (Modifier m : methodDeclaration.getModifiers()) {
            tag.append(m.asString()).append(" ");
        }
        tag.append(type).append(" ").append(name);
        Logger.out(tag.toString());
        if (parameters.size() > 0) {
            Logger.out(s1 + "Parameters:");
            parameters.forEach(x -> Logger.out(s2 + x.toString()));
            Logger.out("");
        }
        if (instanceVariables.size() > 0) {
            Logger.out(s1 + "Instance:");
            instanceVariables.forEach(x -> Logger.out(s2 + x.toString()));
            Logger.out("");
        }
        if (operations.size() > 0) {
            Logger.out(s1 + "Operations:");
            operations.forEach(x -> Logger.out(s2 + x));
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
        return operations;
    }

    public MethodDeclaration getMethodDeclaration() {
        return methodDeclaration;
    }

    public void printAlt() {
        String s = "";
        EnumSet<Modifier> modifiers = methodDeclaration.getModifiers();
        if (modifiers.contains(Modifier.PRIVATE)) {
            s += "-";
        }
        if (modifiers.contains(Modifier.PROTECTED)) {
            s += "#";
        }
        if (modifiers.contains(Modifier.PUBLIC)) {
            s += "+";
        }


        s += name + "(";
        for (int i = 0; i < parameters.size(); i++) {
            Variable v = parameters.get(i);
            s += v.getType();
            if (i < parameters.size() - 1) {
                s += ", ";
            }
        }
        s += "):" + type;
        Logger.out(s);
    }


}
