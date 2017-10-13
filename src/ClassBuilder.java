import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.TypeDeclaration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ClassBuilder {


    public ClassBuilder(ArrayList<String> code) {
        CompilationUnit cu = JavaParser.parse(listToLine(code));
        NodeList<TypeDeclaration<?>> classes = cu.getTypes();


        for (TypeDeclaration<?> cl : classes) {
            Main.out(cl.getName().asString());
            Main.out("Methods:");
            ArrayList<Method> methodList = MethodBuilder.parseClass(cl);
            methodList.forEach(Method::print);
            System.out.println("---------------");

        }
    }


    public static ArrayList<Variable> findVariables(ArrayList<String> assignments) {
        ArrayList<Variable> variables = new ArrayList<>();
        for (String s : assignments) {
            String[] lines = s.split("\r\n");
            for(String s1:lines) {

                List<String> arr =new ArrayList<>(Arrays.asList(s1.split(" ")));
                while(arr.contains(""))
                    arr.remove("");
                for (int i = 0; i < arr.size(); i++) {
                    if (arr.get(i).equals("=") && i > 1) {
                        variables.add(new Variable( arr.get(i - 2),arr.get(i - 1)));
                    }
                }
                if (arr.size() == 2) {
                    variables.add(new Variable(arr.get(0), arr.get(1)));
                }
            }
        }
        return variables;

    }

    private static String listToLine(ArrayList<String> code) {
        StringBuilder in = new StringBuilder();

        for (String line : code) {
            in.append(line).append("\n");
        }

        in = new StringBuilder(in.toString().replaceAll("@1@", "<="));
        in = new StringBuilder(in.toString().replaceAll("@2@", ">="));
        in = new StringBuilder(in.toString().replaceAll("@3@", "!="));
        in = new StringBuilder(in.toString().replaceAll("@4@", "=="));
        return in.toString();
    }

    public static ArrayList<String> lineToList(String code) {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(code.split("\r\n")));
        return list;
    }

}
