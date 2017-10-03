public class Function {

    String name;
    String type;
    Variable[] params;


    public Function(String name, String type, Variable[] params) {
        this.name = name;
        this.type = type;
        this.params = params;
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

    public Variable[] getParams() {
        return params;
    }

    public void setParams(Variable[] params) {
        this.params = params;
    }


    public String toString(){
        return type + " " + name + " ";
    }



}
