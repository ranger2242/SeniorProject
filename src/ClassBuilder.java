import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClassBuilder {
    ExtractedClass e=null;
    public ClassBuilder(ArrayList<String> code) {
        ArrayList<Variable> variables = stripVariable(code);
        ArrayList<Method> methods = stripMethods(code);
        String name= findClass(code);
        e = new ExtractedClass(name,variables,methods,code);


        System.out.println(name);
        System.out.println(Main.div);
        variables.forEach(System.out::println);
        System.out.println(Main.div);
        methods.forEach(System.out::println);
        System.out.println("\n");

    }
    public ExtractedClass generate(){
        return e;
    }
    private String findClass(ArrayList<String> s) {
        ArrayList<String> fuck = s.stream().filter(x -> x.contains("class") && !x.contains(";")&& !x.contains("\"")).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<String> classnames= new ArrayList<>();
        for (String string : fuck) {
            String[] arr = string.split(" ");
            int i = Arrays.asList(arr).indexOf("class");
            if(i+1 >= arr.length){
               classnames.add(s.get(s.indexOf(string)+1).split(" ")[0]);
            }else if(i!=-1 && arr[i].length()==5)
            classnames.add(arr[i + 1]);
        }
        return classnames.get(0);
    }

    private static ArrayList<Variable> stripVariable(ArrayList<String> assignments) {
        ArrayList<Variable> variables = new ArrayList<>();
        for (String s : assignments) {
            String[] arr = s.split(" ");
            for (int i = 0; i < arr.length; i++) {
                if (arr[i].equals("=") && i > 1) {
                    variables.add(new Variable(arr[i - 1], arr[i - 2]));
                }
            }
        }
        return variables;

    }

    private static ArrayList<Method> stripMethods(ArrayList<String> methods) {
        List<String> temp = methods.stream().filter(x -> !x.contains("=") && !x.contains(";")).collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Method> functions = new ArrayList<>();
        for (String method : temp) {
            String[] word = method.split(" ");
            for (int i = 0; i < word.length; i++) {
                if (word[i].endsWith("(") && i > 1) {
                    String name = word[i].substring(0, word[i].length() - 1);
                    String type = word[i - 1];
                    functions.add(new Method(name, type, new ArrayList<>()));
                }
            }
        }
        return functions;
    }
}
