import java.util.ArrayList;

/**
 * Created by Chris Cavazos on 10/17/2017.
 */
public class ExtractedPackage {

    String name;
    ArrayList<String> classPaths = new ArrayList<>();
    ArrayList<ExtractedPackage> packages = new ArrayList<>();

    public ExtractedPackage() {

    }

    public ExtractedPackage(String name) {
        this.name=name;
    }

    public ArrayList<ExtractedPackage> getPackages() {
        return packages;
    }

    public void setPackages(ArrayList<ExtractedPackage> packages) {
        this.packages = packages;
    }

    public ExtractedPackage(String name, ArrayList<String> classes) {
        this.name = name;
        this.classPaths = classes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getClasses() {
        return classPaths;
    }

    public void setClasses(ArrayList<String> classes) {
        this.classPaths = classes;
    }

    public void addPackage(ExtractedPackage load) {
        packages.add(load);
    }
}
