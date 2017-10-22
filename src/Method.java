import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class Method {

    String name;
    String type;
    private final ArrayList<Variable> params = new ArrayList<>();
    private final ArrayList<Variable> instanceVars = new ArrayList<>();
    ArrayList<String> operations = new ArrayList<>();
    MethodDeclaration md= null;

    public Method(String name, String type,
                  Variable[] params, Variable[] instanceVars,
                  MethodDeclaration md) {
        this.name = name;
        this.type = type;
        this.params.addAll(Arrays.asList(params));
        this.instanceVars.addAll(Arrays.asList(instanceVars));
        this.md=md;
        parseOperations();
    }

    private void parseOperations() {
        String code= md.getChildNodesByType(BlockStmt.class).toString();
        ArrayList<String> spCode= new ArrayList<>(Arrays.asList(code.split("\r\n")));
        operations = spCode.stream().filter(x->x.contains(".")||x.contains(" new ") ).collect(Collectors.toCollection(ArrayList::new));
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


    public void print(int depth) {
        String s1 = "" + String.join("", Collections.nCopies(depth+1 , "\t"));
        String s2 = s1 + "\t";
        String s3 = String.join("", Collections.nCopies(depth  , "\t"));

        String tag = s3;
        for(Modifier m : md.getModifiers()){
            tag+=m.asString()+" ";
        }
        tag+= type + " " + name;
        Main.out(tag);
        if(params.size()>0) {
            Main.out(s1 + "Parameters:");
            params.forEach(x -> Main.out(s2 + x.toString()));
            Main.out("");
        }
        if(instanceVars.size()>0) {
            Main.out(s1 + "Instance:");
            instanceVars.forEach(x -> Main.out(s2 + x.toString()));
            Main.out("");
        }
        if(operations.size()>0) {
            Main.out(s1 + "Operations:");
            operations.forEach(x -> Main.out(s2 + x));
            Main.out("");

        }
        Main.out(s1+"--"+name+" END--\n");



    }


    public ArrayList<String> getOperations() {
        return operations;
    }
}
