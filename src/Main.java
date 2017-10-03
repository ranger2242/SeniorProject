import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Created by Chris Cavazos on 10/1/2017.
 */
public class Main {
    static List<String> scanned = new ArrayList<>();
    static List<String> assignments = new ArrayList<>();
    static List<String> methods = new ArrayList<>();
    static List<Variable> variables = new ArrayList<>();
    static List<Function> functions = new ArrayList<>();

    static String div = "-----------------------------------------------";
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler();
        String output = "";
        try {
            output = fileHandler.load(new File("GraphPanel.java"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        output = output.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");
        scanned = Arrays.asList(output.split("\r\n"));
        for (int i = 0; i < scanned.size(); i++) {
            scanned.set(i, scanned.get(i).replaceAll("<=", "@1@"));
            scanned.set(i, scanned.get(i).replaceAll(">=", "@2@"));
            scanned.set(i, scanned.get(i).replaceAll("!=", "@3@"));
            scanned.set(i, scanned.get(i).replaceAll("==", "@4@"));
            scanned.set(i, scanned.get(i).replaceAll("(?<=if)(.*?)(?=\\()", ""));


            scanned.set(i, scanned.get(i).replaceAll("[(]", "( "));
            //scanned.set(i, scanned.get(i).replaceAll("[)]", " )"));
            scanned.set(i, scanned.get(i).replaceAll("=", " = "));
            String[] s = scanned.get(i).split("\\s+");
            List<String> list = Arrays.asList(s);
            list = removeWhiteSpace(list);
            String out = "";
            for (String l : list) {
                if (list.indexOf(l) < list.size() - 1) {
                    out += l + " ";
                } else
                    out += l;

            }

            scanned.set(i, out);
        }
        scanned = removeWhiteSpace(scanned);
        assignments = findAllAssignments(scanned);
        methods = findAllMethods(scanned);


        stripVariable();
        stripMethods();






        printStuff();
    }


    private static void printStuff() {
        scanned.forEach(System.out::println);
        System.out.println(div);
        System.out.println("Variables\n");
        //assignments.forEach(System.out::println);
        variables.forEach(x->System.out.println(x.toString()));
        System.out.println(div);
        System.out.println("Methods\n");
        functions.forEach(x->System.out.println(x.toString()));
        //methods.forEach(System.out::println);
        System.out.println(scanned.size() + "");
    }

    private static void stripVariable() {
        for(String s : assignments){
            String[] arr = s.split(" ");
            for(int i = 0; i < arr.length; i++){
                if ( arr[i].equals("=") ){
                    variables.add(new Variable(arr[i - 1], arr[i - 2]));
                }
            }
        }
    }

    private static void stripMethods() {
        for( String method : methods){
            String[] word = method.split(" ");
            for(int i = 0; i < word.length; i++){
                if (word[i].endsWith("(")){
                    String name = word[i].substring(0, word[i].length() - 1);
                    String type = word[i -1];
                    functions.add(new Function(name, type, null));
                }
            }
        }
    }

    public static List<String> findAllAssignments(List<String> strings) {
        List<String> temp = strings.stream().filter(x -> x.contains("=")).collect(Collectors.toCollection(ArrayList::new));
        List<String> filtered = new ArrayList<>();


        for (String s : temp) {
            String[] arr = s.split(" ");
            for (int i = 0; i < arr.length; i++) {
                if (arr[i].equals("=") && i >1) {
                    filtered.add(s);
                }
            }
        }
        return filtered;
    }

    public static List<String> removeWhiteSpace(List<String> strings) {
        return strings.stream().filter(x -> x.length() > 0).collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<String> findAllMethods(List<String> strings){
        List<String> temp = strings.stream().filter(x -> !x.contains("=") && !x.contains(";")).collect(Collectors.toCollection(ArrayList::new));

        List<String> filtered = new ArrayList<>();


        for (String s : temp) {
            String[] arr = s.split(" ");
            for (int i = 0; i < arr.length; i++) {
                if (arr[i].endsWith("(") && i >1) {
                    filtered.add(s);
                }
            }
        }
        return filtered;

    }
}
