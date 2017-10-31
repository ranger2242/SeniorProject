import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.VariableDeclarator;

import java.util.EnumSet;

public class Variable {

    String name;
    String type;
    EnumSet<Modifier> modifiers = null;

    public Variable(VariableDeclarator variableDeclarator, EnumSet<Modifier> modifiers) {
        this.name = variableDeclarator.getNameAsString();
        this.type = variableDeclarator.getType().asString();
        this.modifiers = modifiers;
    }

    public void print(String s2) {
        String mod = "";
        for (Modifier m : modifiers) {
            mod += m.asString() + " ";
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
