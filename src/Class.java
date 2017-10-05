import java.util.ArrayList;
import java.util.List;

public class Class {

    String name;
    List<Variable> variables = new ArrayList<>();
    List<Function> functions = new ArrayList<>();

    public Class(String name, List<Variable> variables, List<Function> functions) {
        this.name = name;
        this.variables = variables;
        this.functions = functions;
    }

    public String toString(){

        String str = name + "\n";

        if(variables != null){
            str +=  "Variables\n";
            for( Variable var : variables){
                str += "-" + var.type + " " +var.name + "\n";
            }
        }

        if (functions != null){
            str +=  "Functions\n";
            for( Function func : functions){
                str += "-" + func.type + " " +func.name + "\n";
            }
        }

        return str;
    }


}
