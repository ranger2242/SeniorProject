import com.github.javaparser.ast.Modifier;

import java.util.EnumSet;

public class Variable {

    String name;
    String type;
    EnumSet<Modifier> modifiers =null;

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

    public Variable(String type, String name,EnumSet<Modifier> mod) {
        this.name = name;
        this.type = type;
        this.modifiers=mod;
    }

    public String toString() {
        return type + " " + name;
    }


    public void print(String s2) {
        String mod="";
        for(Modifier m : modifiers){
            mod+=m.asString()+" ";
        }
        Main.out(s2 +mod+ toString());

    }
}
