import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by USER on 11/18/2016.
 */
class FileHandler {

    // If batch size =< 0 system will not use batching
    private int batchSize = -1;
    private int currentBatch = 0;
    private String path;


    public FileHandler(int batchSize, String path){
        this.batchSize = batchSize;
        this.path = path;
    }

    public FileHandler(){
    }

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

    int c = 0;
    private ArrayList<String> paths = new ArrayList<>();
    private ArrayList<String> foundSrc = new ArrayList<>();

    private void initPaths(String path) {
        File folder = new File(path);

        if (folder.listFiles() != null){
            int startIndex = 0;
            int endIndex = folder.listFiles().length;
            if (batchSize > 0){
                startIndex = currentBatch * batchSize;
                endIndex = startIndex + batchSize;
                if (endIndex > folder.listFiles().length){
                    endIndex = folder.listFiles().length;
                }
            }

            File[] listOfFiles = Arrays.copyOfRange(folder.listFiles(), startIndex, endIndex);
            paths.clear();
            foundSrc.clear();
            paths.addAll(Arrays.stream(listOfFiles).map(x -> x.getAbsolutePath()).collect(Collectors.toCollection(ArrayList::new)));
            for (String singlePath : paths) {
                findSrc(singlePath);
            }
        }

    }

    public ArrayList<ExtractedDir> nextBatch(){
        currentBatch += 1;
        initPaths(path);
        ArrayList<ExtractedDir> dirs = new ArrayList<>();
        for(String sourcePath: foundSrc){
            dirs.add(load(sourcePath, new ExtractedDir(sourcePath)));
        }
        return dirs;//load(path, new ExtractedDir("root"));

    }

    public int batchesLeft(){
        File folder = new File(path);
        if (folder.listFiles() != null && batchSize > 0){
            int startIndex = (currentBatch + 2) * batchSize;
            int endIndex = folder.listFiles().length;

            int batchesLeft = 0;
            while ( startIndex < endIndex){
                batchesLeft += 1;
                startIndex += batchSize;
            }
            return batchesLeft;
        }
        return 0;
    }

    public boolean hasNext(){
        File folder = new File(path);
        if (folder.listFiles() != null){
            int endIndex = folder.listFiles().length;
            if (batchSize > 0){
                int startIndex = (currentBatch + 1) * batchSize;
                if(startIndex < endIndex){
                    return true;
                }
            }
        }
        return false;
    }

    public static void writeToCSV(BufferedWriter writer, ArrayList<double[][][]>  matrices) throws IOException {
        String toWrite = "";

        for(int i = 0; i < matrices.size() - 1; i++){
            double[][] matrix = matrices.get(i)[0];
            for (int j = 0; j < matrix.length; j++) {
                for (int k = 0; k < matrix[j].length; k++) {
                    toWrite += matrix[j][k];
                    if (k < matrix[j].length - 1) {
                        toWrite += ",";
                    }
                }
                toWrite += "\n";
            }
        }

        writer.append(toWrite);
    }

    private void findSrc(String path) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        for (File f : listOfFiles != null ? listOfFiles : new File[0]) {
            if (f.isDirectory() && f.getName().equals("src")) {
                foundSrc.add(f.getAbsolutePath());
                //System.out.println("SRC FILE: " + f.getAbsolutePath());
            }
        }
        for (File f : listOfFiles != null ? listOfFiles : new File[0]) {
            if (f.isDirectory()) {
                findSrc(path + "\\" + f.getName());
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

