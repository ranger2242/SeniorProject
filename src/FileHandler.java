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


    public String load(){//trying to upload new load function
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
            //  System.out.println("File content: " + sb);
            return sb.toString();
        } catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading file " + ioe);
        }
        return "error";
    }
    public ExtractedPackage load(String path){
       return load(path, new ExtractedPackage("root"));
    }

    public ExtractedPackage load(String path, ExtractedPackage e){
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() &&listOfFiles[i].getName().endsWith(".java") ) {
                System.out.println("File " + listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                e.addPackage(load(path+"\\"+listOfFiles[i].getName(), new ExtractedPackage(listOfFiles[i].getName())));
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
        ArrayList<String> paths = new ArrayList<>();
        ArrayList<File> f= Arrays.asList(listOfFiles).stream().filter(x-> x.getAbsolutePath().endsWith(".java")).collect(Collectors.toCollection(ArrayList::new));
        f.forEach(x->paths.add(x.getAbsolutePath()));
        e.setClasses(paths);
        return e;
    }


}

