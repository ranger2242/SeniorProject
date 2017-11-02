import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Chris Cavazos on 10/1/2017.
 */
@SuppressWarnings("ALL")
class Main {

    public final static String div = "-----------------------------------------------";
    private static Set<ExtractedClass> parsedClasses = new LinkedHashSet<>();
    private static Set<Enum> globalEnums = new LinkedHashSet<>();

    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler();
        Parser parser = new Parser(fileHandler.load("ex\\ex3"));
        parsedClasses = new LinkedHashSet<>(parser.getExtractedClasses());
        globalEnums = new LinkedHashSet<>(parser.getGlobalEnums());

        printAllClasses();

        System.out.println();
    }

    private static void printAllClasses() {
        for (ExtractedClass extractedClass : parsedClasses) {
            if (extractedClass.getParent().equals("")) {
               extractedClass.printClass(0);
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
