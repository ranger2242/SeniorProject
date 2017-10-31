import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;

import java.util.ArrayList;

public class Constructor {

    private ArrayList<Variable> params = new ArrayList<>();
    private ConstructorDeclaration constructorDeclaration;


    Constructor(ConstructorDeclaration constructorDeclaration) {
        this.constructorDeclaration = constructorDeclaration;

        for (Parameter p : constructorDeclaration.getParameters()) {
            VariableDeclarator v = new VariableDeclarator(p.getType(), p.getName());
            params.add(new Variable(v, p.getModifiers()));
        }

    }


    //Setters And Getters
    public ArrayList<Variable> getParams() {
        return params;
    }

    public void setParams(ArrayList<Variable> params) {
        this.params = params;
    }

    public ConstructorDeclaration getConstructorDeclaration() {
        return constructorDeclaration;
    }

    public void setConstructorDeclaration(ConstructorDeclaration constructorDeclaration) {
        this.constructorDeclaration = constructorDeclaration;
    }

    public String getDescription() {
        String a = constructorDeclaration.getName().asString() + "(";


        if ( params.size() > 0 ){
            a += params.get(0).type + " " + params.get(0).name;
            for (int i = 1; i < params.size(); i++) {
                a += ", " + params.get(i).type + " " + params.get(i).name;
            }
        }


        a += ")";
        return a;
    }

}
