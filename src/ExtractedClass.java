import java.util.ArrayList;

public class ExtractedClass {

    String name;
    ArrayList<Variable> variables = new ArrayList<>();
    ArrayList<Method> methods = new ArrayList<>();
    ArrayList<String> code = new ArrayList<>();

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
        this.code= code;
    }

    public String toString(){

        String str = name + "\n";

        if(variables != null){
            str +=  "Variables\n";
            for( Variable var : variables){
                str += "-" + var.type + " " +var.name + "\n";
            }
        }

        if (methods != null){
            str +=  "Functions\n";
            for( Method func : methods){
                str += "-" + func.type + " " +func.name + "\n";
            }
        }

        return str;
    }


    public ArrayList<Variable> getVariables() {
        return variables;
    }
}
