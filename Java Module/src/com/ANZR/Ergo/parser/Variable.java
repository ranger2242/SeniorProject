package com.ANZR.Ergo.parser;

import com.ANZR.Ergo.io.Logger;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.VariableDeclarator;

import java.util.EnumSet;

public class Variable {

    String name;
    String type;
    private EnumSet<Modifier> modifiers;

    Variable(VariableDeclarator variableDeclarator, EnumSet<Modifier> modifiers) {
        this.name = variableDeclarator.getNameAsString();
        this.type = variableDeclarator.getType().asString();
        this.modifiers = modifiers;
    }

    public String toString() {
        return type + " " + name;
    }

    public void print() {
        String s = "";
        if (modifiers.contains(Modifier.PRIVATE)) {
            s += "-";
        }
        if (modifiers.contains(Modifier.PROTECTED)) {
            s += "#";
        }
        if (modifiers.contains(Modifier.PUBLIC)) {
            s += "+";
        }
        s += name + ":" + type;
        Logger.out(s);
    }


    /** Setters And Getters */

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

    public EnumSet<Modifier> getModifiers() {
        return modifiers;
    }
}
