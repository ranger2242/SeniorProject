import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 *   To Use call: MethodSeparator.separate(code)
 */

public class MethodSeparator {

    private MethodSeparator() {
    }

    public static ArrayList<ArrayList<String>> separate(ArrayList<String> code) {

        String in = "";

        for (String line : code) {
            in += line + "\n";
        }

        in = in.replaceAll("@1@", "<=");
        in = in.replaceAll("@2@", ">=");
        in = in.replaceAll("@3@", "!=");
        in = in.replaceAll("@4@", "==");

        CompilationUnit cu = JavaParser.parse(in);

        List<MethodDeclaration> methodDeclarations = cu.getType(0).getMethods();
        ArrayList<ArrayList<String>> methods = new ArrayList<ArrayList<String>>();


        for (MethodDeclaration method : methodDeclarations) {
            String decLine = method.getDeclarationAsString();
            String body = method.getBody().toString();
            body = body.substring(9, body.length() - 1);
            decLine += body;

            ArrayList<String> meth = new ArrayList<String>(Arrays.asList(decLine.split("\n")));
            methods.add(meth);
        }

        return methods;
    }


}
