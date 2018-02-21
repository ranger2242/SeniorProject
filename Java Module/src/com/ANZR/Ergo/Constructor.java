package com.ANZR.Ergo;


import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;

import java.util.ArrayList;

class Constructor {

    private ArrayList<Variable> params = new ArrayList<>();
    private ConstructorDeclaration constructorDeclaration;


    Constructor(ConstructorDeclaration constructorDeclaration) {
        this.constructorDeclaration = constructorDeclaration;

        for (Parameter p : constructorDeclaration.getParameters()) {
            VariableDeclarator v = new VariableDeclarator(p.getType(), p.getName());
            params.add(new Variable(v, p.getModifiers()));
        }

    }


    //Setters And Getters
    public ArrayList<Variable> getParams() {
        return params;
    }

    public void setParams(ArrayList<Variable> params) {
        this.params = params;
    }

    public ConstructorDeclaration getConstructorDeclaration() {
        return constructorDeclaration;
    }

    public void setConstructorDeclaration(ConstructorDeclaration constructorDeclaration) {
        this.constructorDeclaration = constructorDeclaration;
    }

    public String getDescription() {
        StringBuilder a = new StringBuilder(constructorDeclaration.getName().asString() + "(");


        if ( params.size() > 0 ){
            a.append(params.get(0).type).append(" ").append(params.get(0).name);
            for (int i = 1; i < params.size(); i++) {
                a.append(", ").append(params.get(i).type).append(" ").append(params.get(i).name);
            }
        }


        a.append(")");
        return a.toString();
    }

}
