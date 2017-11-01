import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.*;

class ExtractedClass {

    private String name;
    private String parent = "";
    private ArrayList<Variable> imports = new ArrayList<>();
    private ArrayList<Variable> variables = new ArrayList<>();
    private ArrayList<Constructor> constructors = new ArrayList<>();
    private ArrayList<Method> methods = new ArrayList<>();
    private ArrayList<String> code = new ArrayList<>();
    private ArrayList<String> extensions = new ArrayList<>();
    private ArrayList<String> implementations = new ArrayList<>();
    private ArrayList<ExtractedClass> classes = new ArrayList<>();
    private TypeDeclaration<?> typeObject = null;
    private ArrayList<Enum> enums = new ArrayList<>();
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
                TypeDeclaration<?> n = nestedClass;
                classes.add(new ExtractedClass(n));
            }
        }
        return classes;
    }

    ArrayList<Enum> findEnum(ClassOrInterfaceDeclaration cid) {
        ArrayList<Enum> e = new ArrayList<>();
        List<EnumDeclaration> d = cid.getChildNodesByType(EnumDeclaration.class);
        d.forEach(x -> e.add(new Enum(x)));
        return e;
    }

    String findParent(TypeDeclaration<?> typeD) {
        if (typeD.isNestedType()) {
            TypeDeclaration<?> a = (TypeDeclaration<?>) typeD.getParentNode().get();
            return a.getName().asString();
        }
        return "";
    }

    public static ArrayList<String> lineToList(String code) {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(code.split("\r\n")));
        return list;
    }

    public String toString() {

        StringBuilder str = new StringBuilder(name + "\n");

        if (variables != null) {
            str.append("Variables\n");
            for (Variable var : variables) {
                str.append("-").append(var.type).append(" ").append(var.name).append("\n");
            }
        }

        if (methods != null) {
            str.append("Functions\n");
            for (Method func : methods) {
                str.append("-").append(func.type).append(" ").append(func.name).append("\n");
            }
        }

        return str.toString();
    }

    public boolean equals(ExtractedClass e) {
        return this.name.equals(e.name) && this.parent.equals(e.parent);
    }


    //Prints
    private void printClasses(String s) {
        Main.out(s + name);
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

    public ArrayList<Variable> getImports() {
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

    public void setImports(ArrayList<Variable> imports) {
        this.imports = imports;
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

    public boolean isInterface() {
        return isInterface;
    }

    public void setInterface(boolean anInterface) {
        isInterface = anInterface;
    }

}
