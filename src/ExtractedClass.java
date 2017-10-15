import java.util.ArrayList;

class ExtractedClass {

    private String name;
    private ArrayList<Variable> variables = new ArrayList<>();
    private ArrayList<Method> methods = new ArrayList<>();
    private ArrayList<String> code = new ArrayList<>();
    private ArrayList<ExtractedClass> classes = new ArrayList<>();

    public void setVariables(ArrayList<Variable> variables) {
        this.variables = variables;
    }

    public ArrayList<ExtractedClass> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<ExtractedClass> classes) {
        this.classes = classes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Method> getMethods() {
        return methods;
    }

    public void setMethods(ArrayList<Method> methods) {
        this.methods = methods;
    }

    public ArrayList<String> getCode() {
        return code;
    }

    public void setCode(ArrayList<String> code) {
        this.code = code;
    }

    public ExtractedClass(String name, ArrayList<Variable> variables, ArrayList<Method> methods, ArrayList<String> code) {
        this.name = name;
        this.variables = variables;
        this.methods = methods;
        this.code = code;
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

    public ArrayList<Variable> getVariables() {
        return variables;
    }
}
