import com.github.javaparser.ast.body.TypeDeclaration;

import java.util.ArrayList;
import java.util.Collections;

class ExtractedClass {

    private int depth = 0;
    private String name;
    private String parent = "";
    private ArrayList<Variable> imports = new ArrayList<>();
    private ArrayList<Variable> variables = new ArrayList<>();
    private ArrayList<Method> methods = new ArrayList<>();
    private ArrayList<String> code = new ArrayList<>();
    private ArrayList<String> extensions = new ArrayList<>();
    private ArrayList<String> implementations = new ArrayList<>();
    private ArrayList<ExtractedClass> classes = new ArrayList<>();
    private TypeDeclaration<?> typeObject = null;

    public ExtractedClass() {

    }

    public ExtractedClass(String name, String parent, ArrayList<Variable> variables,
                          ArrayList<Method> methods, ArrayList<ExtractedClass> classes,
                          ArrayList<String> code, TypeDeclaration<?> type, int depth) {
        this.name = name;
        this.parent = parent;
        this.variables = variables;
        this.methods = methods;
        this.code = code;
        this.classes = classes;
        this.depth = depth;
        this.typeObject = type;
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

    private void printClasses(String s) {
        Main.out(s + name);
    }

    public ArrayList<String> getExtensions() {
        return extensions;
    }

    public ArrayList<String> getImplementations() {
        return implementations;
    }

    public void print() {
        String s1 = "" + String.join("", Collections.nCopies(depth - 1, "\t"));
        String s2 = s1 + "\t";

        System.out.println("----------------------------");
        Main.out(s1 + "Name: " + name);
        System.out.println("----------------------------");
        if (typeObject.isNestedType()) {
            Main.out(s1 + "Parent: " + parent);
            Main.out("");

        }
        if (extensions.size() > 0) {
            Main.out(s1 + "Extends:");
            extensions.forEach(x -> Main.out(s2 + x));
            Main.out("");
        }
        if (implementations.size() > 0) {
            Main.out(s1 + "Implements:");
            implementations.forEach(x -> Main.out(s2 + x));
            Main.out("");
        }
        if (variables.size() > 0) {
            Main.out(s1 + "Vars:");
            for (Variable v : variables) {
                v.print(s2);
            }
            Main.out("");

        }
        if (methods.size() > 0) {
            Main.out(s1 + "Methods:");
            for (Method m : methods) {
                m.print(depth);
            }
            Main.out("");
        }

        if (classes.size() > 0) {
            Main.out(s1 + "Classes:");
            classes.forEach(x -> x.printClasses(s1));
            Main.out("");
        }
        System.out.println("------------------------------------------");
    }

    public boolean equals(ExtractedClass e) {
        return this.name.equals(e.name) && this.parent.equals(e.parent);
    }

    public int getDepth() {
        return depth;
    }

    public void setExtensions(ArrayList<String> extensions) {
        this.extensions = extensions;
    }

    public void setImplementations(ArrayList<String> implementations) {
        this.implementations = implementations;
    }
}
