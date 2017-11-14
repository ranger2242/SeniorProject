import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.VariableDeclarator;

import java.util.EnumSet;

public class Variable {

    String name;
    String type;
    private EnumSet<Modifier> modifiers = null;

    public Variable(VariableDeclarator variableDeclarator, EnumSet<Modifier> modifiers) {
        this.name = variableDeclarator.getNameAsString();
        this.type = variableDeclarator.getType().asString();
        this.modifiers = modifiers;
    }

    public void print(String s2) {
        StringBuilder mod = new StringBuilder();
        for (Modifier m : modifiers) {
            mod.append(m.asString()).append(" ");
        }
        Main.out(s2 + mod + toString());

    }

    public String toString() {
        return type + " " + name;
    }

    //Setters And Getters
    public String getName() {
        return name;
    }

    public void printAlt(){
        String s="";
        if (modifiers.contains(Modifier.PRIVATE) ) {
            s += "-";
        }
        if( modifiers.contains(Modifier.PROTECTED)){
            s += "#";
        }
        if( modifiers.contains(Modifier.PUBLIC)){
            s += "+";
        }
        s+=name+":"+type;
        Main.out(s);
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

}
