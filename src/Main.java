import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by Chris Cavazos on 10/1/2017.
 */
class Main {

    public final static String div = "-----------------------------------------------";

    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler();
        String output;

        output = fileHandler.load(new File("A.java"));

        output = output.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");
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

        ClassBuilder cb = new ClassBuilder(scanned);

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
