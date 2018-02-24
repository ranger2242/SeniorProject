package com.ANZR.Ergo.transformer;

import com.ANZR.Ergo.parser.ExtractedClass;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.FieldAccessExpr;

import java.util.*;

public class Transformer {

    private Set<ExtractedClass> classes;
    private Set<com.ANZR.Ergo.parser.Enum> enums;


    public Transformer(Set<ExtractedClass> classes, Set<com.ANZR.Ergo.parser.Enum> enums){
        this.classes = classes;
        this.enums = enums;
    }

    /**
     * Creates a data structure to be sent to the Ergo Server
     * @return An Object matrix, First layer is anti-pattern type, Other 2 layers are pattern specific
     */
    public Object[][][] transform(){
        Object[][][] data = new Object[2][][];
        data[0] = GodObjectTransformer.generateGodObjectMatrix(classes);
        data[1] = LongMethodTransformer.generateMatrix(classes);
        return data;
    }

}











