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
        String output;

        parsePackage(fileHandler.load("ex\\ex2"));

        Output p = new Output(new ArrayList<>(parsedClasses));
        printAllClasses();
    }

    public static void parsePackage(ExtractedPackage e){
        FileHandler fileHandler = new FileHandler();
        String output;

        for ( String s : e.getClasses() ){
            output = fileHandler.load(new File(s));
            output = output.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");
            ArrayList<String> scanned = formatCode(output);
            ClassBuilder cb = new ClassBuilder(scanned, 1);
        }
        e.getPackages().forEach(x->parsePackage(x));
        String a = "";
    }

    private static ArrayList<String> formatCode(String output) {
        ArrayList<String> scanned = new ArrayList<>(Arrays.asList(output.split("\r\n")));
        for (int i = 0; i < scanned.size(); i++) {
            scanned.set(i, scanned.get(i).replaceAll("<=", "@1@"));
            scanned.set(i, scanned.get(i).replaceAll(">=", "@2@"));
            scanned.set(i, scanned.get(i).replaceAll("!=", "@3@"));
            scanned.set(i, scanned.get(i).replaceAll("==", "@4@"));
            scanned.set(i, scanned.get(i).replaceAll("(?<=if)(.*?)(?=\\()", ""));


            scanned.set(i, scanned.get(i).replaceAll("[(]", "( "));
            scanned.set(i, scanned.get(i).replaceAll("=", " = "));
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

    static void printAllClasses() {
        for (ExtractedClass e : parsedClasses) {
            if (e.getDepth() == 1) {
                e.print();
                printChildren(e);
            }
        }
    }

    static void printChildren(ExtractedClass e){
        for (ExtractedClass e1 : e.getClasses()) {
            ExtractedClass e2 = getClassByParent(e1.getName(), e.getName());
            if (e2 != null) {
                e2.print();
                printChildren(e2);
            }
        }
    }

    static ExtractedClass getClassByParent(String cl, String parent) {
        for (ExtractedClass c : parsedClasses) {
            if (c.getName().equals(cl) && c.getParent().equals(parent))
                return c;
        }
        return null;
    }

    private static ArrayList<String> removeWhiteSpace(ArrayList<String> strings) {
        return strings.stream().filter(x -> x.length() > 0).collect(Collectors.toCollection(ArrayList::new));
    }

    public static void out(String s) {
        System.out.println(s);
    }

    public static void outa(String s) {
        System.out.print(s);
    }
}
