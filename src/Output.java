import java.util.ArrayList;

/**
 * Created by Chris Cavazos on 10/18/2017.
 */
public class Output {
    ArrayList<ExtractedClass> classes = new ArrayList<>();

    public Output(ArrayList<ExtractedClass> classes) {
        checkInheritence(classes);
        checkUses(classes);
    }

    private void checkInheritence(ArrayList<ExtractedClass> classes) {
        for (ExtractedClass c1 : classes) {
            for (ExtractedClass c2 : classes) {
                if (c1.getExtensions().contains(c2.getName())) {
                    Main.out(c2.getName() + " <|-- " + c1.getName());
                }
            }
        }
    }

    private void checkUses(ArrayList<ExtractedClass> classes) {
        for (ExtractedClass c1 : classes) {
            ArrayList<String> known = new ArrayList<>();
            for (ExtractedClass c2 : classes) {
                for (Method m : c1.getMethods()) {
                    for(String s: m.getOperations()) {
                        if(s.contains(c2.getName()) && !known.contains(c2.getName()) && c1.getName() !=c2.getName()) {
                            Main.out(c1.getName() + " --> " + c2.getName());
                            known.add(c2.getName());
                        }
                    }
                }
            }
        }
    }

}
