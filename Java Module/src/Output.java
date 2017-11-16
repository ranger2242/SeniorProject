import java.util.ArrayList;

/**
 * Created by Chris Cavazos on 10/18/2017.
 */
class Output {
    ArrayList<ExtractedClass> classes = new ArrayList<>();

    public Output(ArrayList<ExtractedClass> classes) {
        checkInheritence(classes);
        checkUses(classes);
        Main.out("");
        this.classes=classes;
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

    public void printAll(){
        classes.forEach(x->printClass(x));
    }

    private void printClass(ExtractedClass e){
        Main.out("class "+e.getName()+"{");
        e.getVariables().forEach(x->x.printAlt());
        e.getMethods().forEach(x->x.printAlt());
        Main.out("}\n");
    }

    private void checkUses(ArrayList<ExtractedClass> classes) {
       /* for (ExtractedClass c1 : classes) {
            ArrayList<String> known = new ArrayList<>();
            for (ExtractedClass c2 : classes) {
                for (Method m : c1.getMethods()) {
                    for(Expression s: m.getOperations()) {
                        if(s.contains(c2.getName()) && !known.contains(c2.getName()) && !Objects.equals(c1.getName(), c2.getName())) {
                            Main.out(c1.getName() + " --> " + c2.getName());
                            known.add(c2.getName());
                        }
                    }
                }
            }
        }*/
    }

}
