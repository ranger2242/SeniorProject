package com.ANZR.Ergo.parser;


import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;

import java.util.ArrayList;

class Constructor {

    private ArrayList<Variable> parameters = new ArrayList<>();
    private ConstructorDeclaration constructorDeclaration;


    Constructor(ConstructorDeclaration constructorDeclaration) {
        this.constructorDeclaration = constructorDeclaration;

        for (Parameter p : constructorDeclaration.getParameters()) {
            VariableDeclarator v = new VariableDeclarator(p.getType(), p.getName());
            parameters.add(new Variable(v, p.getModifiers()));
        }

    }

    public String getDescription() {
        StringBuilder a = new StringBuilder(constructorDeclaration.getName().asString() + "(");


        if (parameters.size() > 0) {
            a.append(parameters.get(0).type).append(" ").append(parameters.get(0).name);
            for (int i = 1; i < parameters.size(); i++) {
                a.append(", ").append(parameters.get(i).type).append(" ").append(parameters.get(i).name);
            }
        }

        a.append(")");
        return a.toString();
    }

    /** Setters And Getters */

    public ArrayList<Variable> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<Variable> parameters) {
        this.parameters = parameters;
    }

    public ConstructorDeclaration getConstructorDeclaration() {
        return constructorDeclaration;
    }

    public void setConstructorDeclaration(ConstructorDeclaration constructorDeclaration) {
        this.constructorDeclaration = constructorDeclaration;
    }


}
