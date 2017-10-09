import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Chris Cavazos on 10/8/2017.
 */
public class ClassSepterator {
    public static ArrayList<ArrayList<String>> seperate(ArrayList<String> code) {

        ArrayList<ArrayList<String>> classes = new ArrayList<ArrayList<String>>();

        for (int index = 0; index < code.size(); index++) {
            int classIndex = findClass(index, code);
            if (classIndex != -1) {
                int numberOfBraces = 0;
                for (int j = index; j < code.size(); j++) {
                    numberOfBraces += checkLineForBraces(code.get(j));
                    if (numberOfBraces == 0) {
                        List<String> classString = code.subList(index, j + 1);
                        ArrayList<String> a = new ArrayList<>();
                        a.addAll(classString);
                        classes.add(a);
                        index = j - 1;
                        break;
                    }
                }
            }
        }

 /*       for (ArrayList<String> c : classes) {
            c.forEach(System.out::println);
            System.out.println(Main.div);
        }*/
        return classes;
    }

    private static int findClass(int index, ArrayList<String> code) {
        String string = code.get(index);
        String[] arr = string.split(" ");
        List<String> list = Arrays.asList(arr);
        if (list.contains("class")) {
            String a = list.get(list.indexOf("class"));
            if (a.length() == 5)
                return code.indexOf(string);
            else return -1;
        } else return -1;
    }


    private static int checkLineForBraces(String line) {
        int braces = 0;
        char[] arr = line.toCharArray();

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == '{') {
                braces += 1;
            } else if (arr[i] == '}') {
                braces -= 1;
            }
        }
        return braces;
    }
}
