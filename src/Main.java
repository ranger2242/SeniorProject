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
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler();
        String output = "";
        try {
            output = fileHandler.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        output= output.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)","");
        scanned = Arrays.asList(output.split("\r\n"));
        for (int i = 0; i < scanned.size(); i++) {
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
        scanned.forEach(System.out::println);
        System.out.println(scanned.size() + "");
    }

    public static List<String> removeWhiteSpace(List<String> strings) {
        return strings.stream().filter(x -> x.length() > 0).collect(Collectors.toCollection(ArrayList::new));
    }


}
