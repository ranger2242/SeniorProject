import java.util.ArrayList;
import java.util.List;

public class ClassExtractor {

    /* Search code each line and find the keyword "class".
     * Then keep track of curly braces to find the end of the class.
     */
    public static List<Class> extractClasses(List<String> code){

        List<Class> classes = new ArrayList<Class>();

        for (int index = 0; index < code.size(); index++ ){
            if ( code.get(index).contains("class") ){                   //Case where class is a string in program will break code

                String name = getClassName(code.get(index));

                //New class at this index
                //Search from current line to find the end of the class
                int numberOfBraces = 0;
                for ( int j = index ; j < code.size() - index; j++){
                    //Search for end of class here
                    numberOfBraces += checkLineForBraces(code.get(j));

                    if ( numberOfBraces == 0){
                        // Index is the start of the class and j is the end of class

                        List<String> classString = code.subList(index, j);
                        classString.add("}");

                        Class aNewClass = new Class(name, getVariables(classString), getMethods(classString));
                        classes.add(aNewClass);

                        index = j; //Avoid searching the rest of the class in main loop
                        break;
                    }
                }

            }
        }

        return classes;

    }

    private static List<Variable> getVariables(List<String> code) {

        List<Variable> variables = new ArrayList<>();

        for(String s : code){
            String[] arr = s.split(" ");
            for(int i = 0; i < arr.length; i++){
                if ( arr[i].equals("=") ){
                    variables.add(new Variable(arr[i - 1], arr[i - 2]));
                }
            }
        }
        return variables;
    }


    //--FIX--
    //Broken for some reason??
    private static List<Function> getMethods(List<String> code) {

        List<Function> functions = new ArrayList<>();

        for( String method : code){
            String[] word = method.split(" ");
            for(int i = 0; i < word.length; i++){
                if (word[i].endsWith("(")){
                    if (i > 0){
                        if ( !word[i - 1].equals("new") && !word[i].contains(".") ){
                            String name = word[i].substring(0, word[i].length() - 1);
                            String type = word[i -1];
                            functions.add(new Function(name, type, null));
                        }
                    }
                }
            }
        }
        return functions;
    }

    private static String getClassName(String line){
        String[] arr = line.split(" ");

        for( int i =0; i < arr.length; i++ ){
            if ( arr[i].equals("class") ){
                if (arr[i + 1].endsWith("{")){
                    //Drop last char '{'
                    return arr[i + 1].substring(0, arr[i + 1].length() - 1);
                }else{
                    return arr[i + 1];
                }
            }
        }
        return "ERROR: No Name detected";
    }

    private static int checkLineForBraces(String line ){
        int braces = 0;
        char[] arr = line.toCharArray();

        for ( int i = 0; i < arr.length; i++ ){
            if ( arr[i] == '{' ){
                braces += 1;
            }else if ( arr[i] == '}' ){
                braces -= 1;
            }
        }
        return braces;
    }


}
