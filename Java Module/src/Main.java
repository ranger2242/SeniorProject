import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Chris Cavazos on 10/1/2017.
 */
@SuppressWarnings("ALL")
class Main {

    public final static String div = "-----------------------------------------------";
    public static Set<ExtractedClass> parsedClasses = new LinkedHashSet<>();
    public static Set<Enum> globalEnums = new LinkedHashSet<>();

    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler();
        Parser parser = new Parser(fileHandler.load("ex\\ex2"));
        parsedClasses = new LinkedHashSet<>(parser.getExtractedClasses());
        globalEnums = new LinkedHashSet<>(parser.getGlobalEnums());
        ArrayList<ExtractedClass> cl = new ArrayList<>(parsedClasses);
//        printAllClasses();
//        Output o = new Output(cl);
//        o.printAll();


        //Test list of matrices for pipeline
        Pipeline pipeline = new Pipeline();
        ArrayList<int[][]> list = new ArrayList<>();
        int[][] array = {{1,3,5}, {3,2,9}, {8,8,8}};
        list.add(array);

        //Create json file for Python project
        pipeline.createJSONFile(list);
        pipeline.launchPython();
        JSONObject json = pipeline.readJSONFile("python_output", ".json");
        System.out.println(json);

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
