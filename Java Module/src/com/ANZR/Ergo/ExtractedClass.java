package com.ANZR.Ergo;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.*;

@SuppressWarnings("ALL")
class ExtractedClass {

    private String name;
    private String parent = "";
    private ArrayList<Variable> variables = new ArrayList<>();
    private ArrayList<Constructor> constructors = new ArrayList<>();
    private ArrayList<Method> methods = new ArrayList<>();
    private ArrayList<String> code = new ArrayList<>();
    private ArrayList<String> extensions = new ArrayList<>();
    private ArrayList<String> implementations = new ArrayList<>();
    private ArrayList<ExtractedClass> classes = new ArrayList<>();
    private TypeDeclaration<?> typeObject = null;
    private ArrayList<Enum> enums = new ArrayList<>();
    private final ArrayList<ImportDeclaration> imports = new ArrayList<>();
    private boolean isInterface = false;

    //Constructors
    public ExtractedClass() {

    }

    public ExtractedClass(TypeDeclaration<?> typeD) {

        this.name = typeD.getNameAsString();
        this.parent = findParent(typeD);
        this.variables = findVariables(typeD);
        this.methods = findMethods(typeD);
        this.constructors = findConstructors(typeD);
        this.code = lineToList(typeD.toString());
        this.classes = findNests((ClassOrInterfaceDeclaration) typeD);
        this.typeObject = typeD;
        this.enums = findEnum(typeD.asClassOrInterfaceDeclaration());
        this.extensions = findInheritance(typeD.asClassOrInterfaceDeclaration());
        this.implementations = findInterface(typeD.asClassOrInterfaceDeclaration());
        this.isInterface =  ((ClassOrInterfaceDeclaration) typeD).isInterface();

    }


    //Methods
    private ArrayList<String> findInterface(ClassOrInterfaceDeclaration cid) {
        ArrayList<String> impl = new ArrayList<>();
        for (ClassOrInterfaceType ext : cid.getImplementedTypes()) {
            impl.add(ext.getName().asString());
        }
        return impl;
    }

    private ArrayList<String> findInheritance(ClassOrInterfaceDeclaration cid) {
        ArrayList<String> extend = new ArrayList<>();

        for (ClassOrInterfaceType ext : cid.getExtendedTypes()) {
            extend.add(ext.getName().asString());
        }
        return extend;
    }

    private static ArrayList<Method> findMethods(TypeDeclaration<?> inputClass) {
        ArrayList<Method> output = new ArrayList<>();
        for (int i = 0; i < inputClass.getMethods().size(); i++) {
            output.add(new Method(inputClass.getMethods().get(i)));
        }
        return output;
    }

    private static ArrayList<Constructor> findConstructors(TypeDeclaration<?> inputClass){
        ArrayList<Constructor> constructors = new ArrayList<>();

        for (ConstructorDeclaration constructorDeclaration : inputClass.getChildNodesByType(ConstructorDeclaration.class)) {
            constructors.add(new Constructor(constructorDeclaration));
        }
        return constructors;
    }

    private static ArrayList<Variable> findVariables(TypeDeclaration<?> cl) {
        ArrayList<Variable> variables = new ArrayList<>();

        for (FieldDeclaration f : cl.getFields()) {
            for (VariableDeclarator v : f.getVariables()) {
                variables.add(new Variable(v, f.getModifiers()));
            }
        }
        return variables;

    }

    private ArrayList<ExtractedClass> findNests(ClassOrInterfaceDeclaration classAsDeclaration) {
        List<ClassOrInterfaceDeclaration> nestedClasses = classAsDeclaration.getChildNodesByType(ClassOrInterfaceDeclaration.class);
        ArrayList<ExtractedClass> classes = new ArrayList<>();
        for (ClassOrInterfaceDeclaration nestedClass : nestedClasses) {
            if(nestedClass.getParentNode().get().equals(classAsDeclaration)){
                classes.add(new ExtractedClass(nestedClass));
            }
        }
        return classes;
    }

    private ArrayList<Enum> findEnum(ClassOrInterfaceDeclaration cid) {
        ArrayList<Enum> e = new ArrayList<>();
        List<EnumDeclaration> d = cid.getChildNodesByType(EnumDeclaration.class);
        d.forEach(x -> e.add(new Enum(x)));
        return e;
    }

    private String findParent(TypeDeclaration<?> typeD) {
        if (typeD.isNestedType()) {
            TypeDeclaration<?> a = (TypeDeclaration<?>) typeD.getParentNode().get();
            return a.getName().asString();
        }
        return "";
    }

    private static ArrayList<String> lineToList(String code) {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(code.split("\r\n")));
        return list;
    }

    public boolean equals(ExtractedClass e) {
        return this.name.equals(e.name) && this.parent.equals(e.parent);
    }

    //Prints
    private void printClasses(String s) {
        Logger.out(s + name);
    }

    public void printClass( int depth) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            indent.append("    ");
        }
        String indent2 = indent + "    ";
        String indent3 = indent2 + "    ";

        if(isInterface()){
            System.out.println(indent + "Interface: " + getName());
        }else {
            System.out.println(indent + "Class: " + getName());
        }


        System.out.println(indent2 + "Imports");
        imports.forEach(x -> System.out.println(indent3 + x.getName().asString()));


        System.out.println(indent2 + "Vars: ");
        variables.forEach(x -> System.out.println(indent3 + x.toString()));

        System.out.println(indent2 + "Methods: ");
        methods.forEach(x->x.print(depth + 1));

        System.out.println(indent2 + "Constructors: ");
        constructors.forEach(x -> System.out.println(indent3 + x.getDescription()));

        if (classes.size() > 0) {
            System.out.println(indent2 + "Nested Classes: ");
            for (ExtractedClass e : getClasses()) {
                System.out.println(indent2 + "-----------");
                e.printClass( depth + 1);
                System.out.println(indent2 + "-----------");
            }
        }
    }


    //Setters And Getters
    public void setExtensions(ArrayList<String> extensions) {
        this.extensions = extensions;
    }

    public void setImplementations(ArrayList<String> implementations) {
        this.implementations = implementations;
    }

    public void setEnums(ArrayList<Enum> enums) {
        this.enums = enums;
    }

    public ArrayList<String> getExtensions() {
        return extensions;
    }

    public ArrayList<String> getImplementations() {
        return implementations;
    }

    public String getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public ArrayList<ExtractedClass> getClasses() {
        return classes;
    }

    public ArrayList<String> getCode() {
        return code;
    }

    public ArrayList<Method> getMethods() {
        return methods;
    }

    public ArrayList<Variable> getVariables() {
        return variables;
    }

    public ArrayList<ImportDeclaration> getImports() {
        return imports;
    }

    public TypeDeclaration<?> getTypeObject() {
        return typeObject;
    }

    public void setClasses(ArrayList<ExtractedClass> classes) {
        this.classes = classes;
    }

    public void setVariables(ArrayList<Variable> variables) {
        this.variables = variables;
    }

    public void setImports(NodeList<ImportDeclaration> imports) {
        this.imports.clear();
        this.imports.addAll(imports);
    }

    public void setMethods(ArrayList<Method> methods) {
        this.methods = methods;
    }

    public void setCode(ArrayList<String> code) {
        this.code = code;
    }

    public void setTypeObject(TypeDeclaration<?> typeObject) {
        this.typeObject = typeObject;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public ArrayList<Constructor> getConstructors() {
        return constructors;
    }

    public void setConstructors(ArrayList<Constructor> constructors) {
        this.constructors = constructors;
    }

    private boolean isInterface() {
        return isInterface;
    }

    public void setInterface(boolean anInterface) {
        isInterface = anInterface;
    }

}
