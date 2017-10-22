import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

class ClassBuilder {

    ExtractedClass e = new ExtractedClass();
    private int depth = 0;

    public ClassBuilder(ArrayList<String> code, int depth) {
        CompilationUnit cu = JavaParser.parse(listToLine(code));
        NodeList<TypeDeclaration<?>> classes = cu.getTypes();
        this.depth = depth;
        for (TypeDeclaration<?> cl : classes) {
            construct(cl);
        }
    }

    public ClassBuilder(TypeDeclaration<?> cl, int depth) {
        this.depth = depth;
        construct(cl);
    }


    public ExtractedClass getExtracted() {
        return e;
    }

    public void construct(TypeDeclaration<?> cl) {

        ArrayList<ExtractedClass> classes = findNests((ClassOrInterfaceDeclaration) cl);
        cl.getChildNodes();
        ArrayList<Method> methodList = MethodBuilder.parseClass(cl);

        ArrayList<String> code = lineToList(cl.toString());
        ArrayList<Variable> var = findVariables(cl);
        String parent = "";
        if (cl.isNestedType()) {
            TypeDeclaration<?> a = (TypeDeclaration<?>) cl.getParentNode().get();
            parent = a.getName().asString();
        }

        ExtractedClass e = new ExtractedClass(cl.getNameAsString(), parent, var, methodList, classes, code, cl, depth);

        boolean b = false;
        Iterator<ExtractedClass> a = Main.parsedClasses.iterator();
        while (a.hasNext()) {
            if (a.next().equals(e)) {
                b = true;
            }
        }
        if (!b)
            Main.parsedClasses.add(e);

        ClassOrInterfaceDeclaration cid = cl.asClassOrInterfaceDeclaration();
        ArrayList<String> extend = new ArrayList<>();
        for (ClassOrInterfaceType ext : cid.getExtendedTypes()) {
            extend.add(ext.getName().asString());
        }
        e.setExtensions(extend);

        ArrayList<String> impl = new ArrayList<>();
        for (ClassOrInterfaceType ext : cid.getImplementedTypes()) {
            impl.add(ext.getName().asString());
        }
        e.setImplementations(impl);
        this.e = e;

    }

    public static ArrayList<Variable> findVariables(TypeDeclaration<?> cl) {
        ArrayList<Variable> variables = new ArrayList<>();
        for (FieldDeclaration f : cl.getFields()) {
            for (VariableDeclarator v : f.getVariables()) {
                variables.add(new Variable(v.getType().asString(), v.getName().asString(), f.getModifiers()));
            }
        }
        return variables;

    }


    public ArrayList<ExtractedClass> findNests(ClassOrInterfaceDeclaration classAsDeclaration) {
        List<ClassOrInterfaceDeclaration> nestedClasses = classAsDeclaration.getChildNodesByType(ClassOrInterfaceDeclaration.class);
        ArrayList<ExtractedClass> classes = new ArrayList<>();
        for (ClassOrInterfaceDeclaration nestedClass : nestedClasses) {
            TypeDeclaration<?> n = nestedClass;
            ClassBuilder cb = new ClassBuilder(n, depth + 1);
            classes.add(cb.getExtracted());
        }
        return classes;
    }


    private static String listToLine(ArrayList<String> code) {
        StringBuilder in = new StringBuilder();

        for (String line : code) {
            in.append(line).append("\r\n");
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
