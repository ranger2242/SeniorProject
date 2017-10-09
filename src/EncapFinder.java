import java.util.ArrayList;

public class EncapFinder {

    //  --Unimplemented methods--

    public static void check(ArrayList<ExtractedClass> classes) {
        ArrayList<Variable> variableList = new ArrayList<>();
        for (ExtractedClass c : classes) {
            variableList.addAll(c.getVariables());
        }
    }

    public static boolean checkClass() {

        return true;
    }


}
