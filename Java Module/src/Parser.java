import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

class Parser {

    private ArrayList<ExtractedClass> extractedClasses = new ArrayList<>();
    private final ArrayList<Enum> globalEnums = new ArrayList<>();

    public Parser(ExtractedDir dir) {

        parseDirectory(dir);
    }

    FileHandler fileHandler = new FileHandler();

    private void parseDirectory(ExtractedDir e) {

        for (String s : e.getClassPaths()) {
            ArrayList<ExtractedClass> newClasses = parseCode(s);
            if (newClasses != null) {
                extractedClasses.addAll(newClasses);
            }
        }
        e.getPackages().forEach(this::parseDirectory);
    }


    private ArrayList<ExtractedClass> parseCode(String dir) {
        String output;

        output = fileHandler.load(new File(dir));
        ArrayList<String> scanned = formatCode(output);
        String code = listToLine(scanned);
        code = code.replaceAll("\\(", "\\( ");
        ArrayList<ExtractedClass> listOfClasses = new ArrayList<>();
        try {
            System.out.println("PROCESSING: " + dir);
            CompilationUnit cu = JavaParser.parse(code);
            NodeList<TypeDeclaration<?>> classes = cu.getTypes();
            NodeList<ImportDeclaration> imports = cu.getImports();

            for (TypeDeclaration<?> cl : classes) {
                if (cl.isEnumDeclaration()) {
                    globalEnums.add(new Enum(cl.asEnumDeclaration()));
                } else {
                    //Assumed a class or interface
                    ExtractedClass newClass = new ExtractedClass(cl);
                    newClass.setImports(imports);
                    listOfClasses.addAll(createClassList(newClass));
                }
            }
            return listOfClasses;
        } catch (ParseProblemException e) {
            System.out.println("PARSE ERROR: " + dir);
            // e.printStackTrace();
            return null;
        }catch (StackOverflowError e) {
            System.out.println("STACK OVERFLOW ERROR: " + dir);
            // e.printStackTrace();
            return null;
        }
    }

    private ArrayList<ExtractedClass> createClassList(ExtractedClass extractedClass) {

        ArrayList<ExtractedClass> list = new ArrayList<>();
        list.add(extractedClass);

        for (ExtractedClass r : extractedClass.getClasses()) {
            list.addAll(createClassList(r));
        }
        return list;
    }

    private void printAllChildNodes(ClassOrInterfaceDeclaration cid) {
        cid.getChildNodes().forEach(x -> Main.out(x.getClass().toString()));
    }


    //Formatting Tools
    private static ArrayList<String> formatCode(String output) {
        ArrayList<String> scanned = new ArrayList<>(Arrays.asList(output.split("\r\n")));
        for (int i = 0; i < scanned.size(); i++) {
            scanned.set(i, scanned.get(i).replaceAll("(?<=if)(.*?)(?=\\()", ""));
            String[] s = scanned.get(i).split("\\s+");
            ArrayList<String> list = new ArrayList<>(Arrays.asList(s));
            list = removeWhiteSpace(list);
            StringBuilder out = new StringBuilder();
            for (String l : list) {
                if (list.indexOf(l) < list.size() - 1) {
                    out.append(l).append(" ");
                } else
                    out.append(l);
            }

            scanned.set(i, out.toString());
        }
        scanned = removeWhiteSpace(scanned);
        return scanned;
    }

    private static String listToLine(ArrayList<String> code) {
        StringBuilder in = new StringBuilder();

        for (String line : code) {
            in.append(line).append("\r\n");
        }
        return in.toString();
    }

    private static ArrayList<String> removeWhiteSpace(ArrayList<String> strings) {
        return strings.stream().filter(x -> x.length() > 0).collect(Collectors.toCollection(ArrayList::new));
    }


    //Setters And Getters
    public ArrayList<ExtractedClass> getExtractedClasses() {
        return extractedClasses;
    }

    public void setExtractedClasses(ArrayList<ExtractedClass> extractedClasses) {
        this.extractedClasses = extractedClasses;
    }

    public ArrayList<Enum> getGlobalEnums() {
        return globalEnums;
    }


}
