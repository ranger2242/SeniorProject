import org.json.JSONObject;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
//import Sock

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





        System.out.println();
    }

    public static JSONObject initPipeline(ArrayList<int[][]> list){
        //Create json file for Python project
        Pipeline pipeline = new Pipeline();
        pipeline.createJSONFile(list);
        pipeline.launchPython();
        JSONObject json = pipeline.readJSONFile("python_output", ".json");
        return json;
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
