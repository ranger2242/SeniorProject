import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by USER on 11/18/2016.
 */
class FileHandler {


    public String load() {//trying to upload new load function
        JFrame parentFrame = new JFrame();

        JFileChooser fileChooser = new JFileChooser();
        int userSelection = fileChooser.showSaveDialog(parentFrame);
        File f = null;
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            f = fileChooser.getSelectedFile();
        }
        return load(f);
    }


    public String load(File file) {//trying to upload new load function
        FileInputStream fin;

        int ch;
        StringBuilder sb = new StringBuilder();
        try {
            fin = new FileInputStream(file);
            while ((ch = fin.read()) != -1) {
                sb.append((char) ch);
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading file " + ioe);
        }
        return "error";
    }

    public ArrayList<ExtractedDir> load(String path) {
        initPaths(path);
        ArrayList<ExtractedDir> dirs = new ArrayList<>();
        for(String s: foundSrc){
            dirs.add(load(s, new ExtractedDir(s)));
        }
        return dirs;//load(path, new ExtractedDir("root"));
    }

    int c = 0;
    ArrayList<String> paths = new ArrayList<>();
    ArrayList<String> foundSrc = new ArrayList<>();

    void initPaths(String path) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        paths.addAll(Arrays.stream(listOfFiles).map(x -> x.getAbsolutePath()).collect(Collectors.toCollection(ArrayList::new)));
        for (String s : paths) {
            findSrc(s,0);
        }
    }

    void findSrc(String path, int d) {
        //System.out.println("");
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        for (File f : listOfFiles != null ? listOfFiles : new File[0]) {
            if (f.isDirectory() && f.getName().equals("src")) {
                foundSrc.add(f.getAbsolutePath());
                System.out.println("SRC FILE: " + f.getAbsolutePath());
            }
        }
        for (File f : listOfFiles != null ? listOfFiles : new File[0]) {
            if (f.isDirectory()) {
                findSrc(path + "\\" + f.getName(), d++);
            }
        }
    }

    private ExtractedDir load(String path, ExtractedDir e) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        for (File listOfFile : listOfFiles != null ? listOfFiles : new File[0]) {
            if (listOfFile.isFile() && listOfFile.getName().endsWith(".java")) {
                //System.out.println("File " + listOfFile.getName());
            } else if (listOfFile.isDirectory()) {
                e.addPackage(load(path + "\\" + listOfFile.getName(), new ExtractedDir(listOfFile.getName())));
                //System.out.println("Directory " + listOfFile.getName());
            }
        }
        ArrayList<String> paths = new ArrayList<>();
        ArrayList<File> f = Arrays.stream(listOfFiles).filter(x -> x.getAbsolutePath().endsWith(".java")).collect(Collectors.toCollection(ArrayList::new));
        f.forEach(x -> paths.add(x.getAbsolutePath()));
        e.setClasses(paths);
        return e;
    }


}

