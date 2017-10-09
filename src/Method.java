import java.util.ArrayList;
import java.util.List;

public class Method {

    String name;
    String type;
    List<Usage> usages = new ArrayList<>();
    List<Variable> instanceVars = new ArrayList<>();



    public Method(String name, String type, List<Variable> params) {
        this.name = name;
        this.type = type;
        instanceVars.addAll(params);
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

    public List<Usage> getUsages() {
        return usages;
    }

    public void setUsages(List<Usage> usages) {
        this.usages = usages;
    }

    public List<Variable> getInstanceVars() {
        return instanceVars;
    }

    public void setInstanceVars(List<Variable> instanceVars) {
        this.instanceVars = instanceVars;
    }

    public String toString(){
        return type + " " + name + " ";
    }



}
