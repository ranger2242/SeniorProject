import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
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

            //BEGIN find nested classes
            ClassOrInterfaceDeclaration classAsDeclaration = (ClassOrInterfaceDeclaration) cl;
            List<ClassOrInterfaceDeclaration> nestedClasses = classAsDeclaration.getChildNodesByType(ClassOrInterfaceDeclaration.class);

            for (ClassOrInterfaceDeclaration nestedClass : nestedClasses) {
                System.out.println("Class: " + nestedClass.getNameAsString() + " -> Nested: " + nestedClass.isNestedType());
            }
            //END find nested classes


            Main.out("Methods:");
            ArrayList<Method> methodList = MethodBuilder.parseClass(cl);
            methodList.forEach(Method::print);
            System.out.println("---------------");
        }
    }


    public static ArrayList<Variable> findVariables(ArrayList<String> assignments) {
        ArrayList<Variable> variables = new ArrayList<>();
        for (String assignment : assignments) {
            String[] lines = assignment.split("\r\n");
            for (String line : lines) {
                List<String> words = new ArrayList<>(Arrays.asList(line.split(" ")));
                while (words.contains("")) {
                    words.remove("");
                }
                for (int i = 0; i < words.size(); i++) {
                    if (words.get(i).equals("=") && i > 1) {
                        variables.add(new Variable(words.get(i - 2), words.get(i - 1)));
                    }
                }
                if (words.size() == 2) {
                    variables.add(new Variable(words.get(0), words.get(1)));
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
