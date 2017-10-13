import java.util.ArrayList;
import java.util.Arrays;

public class Method {

    String name;
    String type;
    private final ArrayList<Variable> params = new ArrayList<>();
    private final ArrayList<Variable> instanceVars = new ArrayList<>();



    public Method(String name, String type, Variable[] params, Variable[] instanceVars) {
        this.name = name;
        this.type = type;
        this.params.addAll(Arrays.asList(params));
        this.instanceVars.addAll(Arrays.asList(instanceVars));
    }

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


    public void print(){
        Main.out(type + " " + name + " ");
        Main.out("\tParameters:");
        params.forEach(x-> Main.out("\t\t" + x.toString()));
        Main.out("\tInstance:");
        instanceVars.forEach(x-> Main.out("\t\t" + x.toString()));
    }



}
