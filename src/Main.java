import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Chris Cavazos on 10/1/2017.
 */
class Main {

    public final static String div = "-----------------------------------------------";
    public static Set<ExtractedClass> parsedClasses = new LinkedHashSet<>();

    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler();
        Parser parser = new Parser(fileHandler.load("ex\\ex3"));
        parsedClasses = new LinkedHashSet<>(parser.getExtractedClasses()) ;

        printAllClasses();

        System.out.println();
    }

    private static void printAllClasses(){
        for (ExtractedClass extractedClass :  parsedClasses){
            printSingleClass(extractedClass, 0);
        }

    }

    private static void printSingleClass(ExtractedClass extractedClass, int depth){
        String indent = "";
        for (int i =0; i < depth; i++){
            indent += "    ";
        }
        String indent2 = indent + "    ";
        String indent3 = indent2 + "    ";

        System.out.println(indent + "Class: " + extractedClass.getName());

        System.out.println(indent2 + "Vars: ");
        extractedClass.getVariables().forEach(x-> System.out.println(indent3 + x.toString()) );

        System.out.println(indent2 + "Methods: ");
        extractedClass.getMethods().forEach(x-> System.out.println(indent3 + x.getName() ));

        System.out.println(indent2 + "Constructors: ");
        extractedClass.getConstructors().forEach(x-> System.out.println(indent3 + x.getDescription() ));

        if (extractedClass.getClasses().size() > 0) {
            System.out.println(indent2 + "Nested Classes: ");
            for (ExtractedClass e : extractedClass.getClasses()){
                System.out.println(indent2 + "-----------");
                printSingleClass(e, depth + 1);
                System.out.println(indent2 + "-----------");
            }
        }
    }

    public static void out(String s) {
        System.out.println(s);
    }

    public static void outa(String s) {
        System.out.print(s);
    }
}
