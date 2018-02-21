package com.ANZR.Ergo;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.EnumDeclaration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris Cavazos on 10/29/2017.
 */
class Enum {

    private String name = "";
    private EnumDeclaration dec = new EnumDeclaration();

    public Enum( EnumDeclaration dec) {
        this.name = dec.getNameAsString();
        this.dec = dec;
    }

    public void print(String s2) {
        StringBuilder mod= new StringBuilder();
        for(Modifier m : dec.getModifiers()){
            mod.append(m.asString()).append(" ");
        }
        Logger.out(s2 +mod+ name+":");
    }


    //Setters And Getters
    public List<String> getTypes(){
        List<String> e = new ArrayList<>();
        dec.getEntries().forEach(x -> e.add(x.getName().asString()));
        return e;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EnumDeclaration getDec() {
        return dec;
    }

    public void setDec(EnumDeclaration dec) {
        this.dec = dec;
    }

}
