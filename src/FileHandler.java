import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by USER on 11/18/2016.
 */
public class FileHandler {


    public String load() throws IOException {//trying to upload new load function
        JFrame parentFrame = new JFrame();

        JFileChooser fileChooser = new JFileChooser();
        int userSelection = fileChooser.showSaveDialog(parentFrame);
        File f = null;
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            f = fileChooser.getSelectedFile();
        }
        return load(f);
    }

    public String load(File file) throws IOException {//trying to upload new load function
        FileInputStream fin = null;

        int ch;
        StringBuffer sb = new StringBuffer();
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

}

