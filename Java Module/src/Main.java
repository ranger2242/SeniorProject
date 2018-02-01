import com.google.gson.Gson;
import io.socket.client.Socket;
import java.io.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

/**
 * Created by Chris Cavazos on 10/1/2017.
 */


@SuppressWarnings("ALL")
class Main {

    public final static String div = "-----------------------------------------------";
    public static ArrayList<Set<ExtractedClass>> parsedClasses = new ArrayList<>();
    public static ArrayList<Set<Enum>> globalEnums = new ArrayList<>();

    static String port = "2242";
    static String ip = "10.133.228.186";
    static String ip3 = "139.94.249.166";

    static String ip2 = "localhost";
    public static String dir = "http://" + ip2 + ":" + port;
    static Gson gson = new Gson();

    static Socket socket;
    private static String clientID;

    private static boolean trainingMode = true;



    public static void main(String[] args) {

        slog("Setting up file writer...");

        String rootDirectory = getRootDirectory();
        String fileName = rootDirectory + "\\training_dataset.csv";
        BufferedWriter writer = setupFileWriter(fileName);

        slog("Settting up filehandler...");

        String path = "C:\\Users\\Ross\\Desktop\\JAVA";

        int batchSize = 1;
        FileHandler fileHandler = new FileHandler(batchSize, path);

        int batchNumber = 1;
        while ( fileHandler.hasNext() ){

            System.out.println(div);
            slog("Batch: " + batchNumber + " - Batches Left: " + fileHandler.batchesLeft());
            ArrayList<ExtractedDir> directories = fileHandler.nextBatch();
            slog("Directories loaded. " + directories.size() + " directories founds.");

            slog("Parsing classes...");
            Tuple<ArrayList<Set<ExtractedClass>>, ArrayList<Set<Enum>>> tuple = Parser.parseDirectories(directories);
            parsedClasses = tuple.parsedClasses;
            globalEnums = tuple.globalEnums;
            slog("Classes Parsed");


            if (trainingMode) {

                ArrayList<double[][][]> matrices = transformTrainingData();

                try{
                    FileHandler.writeToCSV(writer, matrices);
                    slog("Data writen to CSV file");
                }catch (IOException e){e.printStackTrace();}

            } else {

                double[][][] matrices = transformData();

                Gson gson = new Gson();
                String json = gson.toJson(matrices);

                Pipeline pipeline = new Pipeline();
                pipeline.sendToServer(json);

            }
            slog("Batch Complete");
            batchNumber++;
        }
        try{
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }



    public static void clearFile(String fileName){
        File file = new File(fileName);
        file.delete();
        file = null;
    }

    public static ArrayList<double[][][]> transformTrainingData(){
        ArrayList<double[][][]> matrices = new ArrayList<>();
        for (Set<ExtractedClass> set : parsedClasses) {
            Transformer transformer = new Transformer(set, globalEnums.get(parsedClasses.indexOf(set)));
            matrices.add(transformer.transform());
        }
        slog("Data Transformed");
        return matrices;
    }

    public static double[][][] transformData(){
        Set<ExtractedClass> set = parsedClasses.get(0);
        Transformer transformer = new Transformer(set, globalEnums.get(parsedClasses.indexOf(set)));
        double[][][] matrices = transformer.transform();
        slog("Data Transformed");
        return matrices;
    }

    private static BufferedWriter setupFileWriter(String fileName){
        try{
            return new BufferedWriter(new FileWriter(fileName));
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private static String getRootDirectory() {
        String rootDirectory = System.getProperty("user.dir");
        while (!rootDirectory.endsWith("\\")) {
            rootDirectory = rootDirectory.substring(0, rootDirectory.length() - 1);
        }
        rootDirectory = rootDirectory.substring(0, rootDirectory.length() - 1);
        return rootDirectory;
    }

    private static void printAllClasses() {
        for (Set<ExtractedClass> set : parsedClasses) {
            for (ExtractedClass extractedClass : set) {
                if (extractedClass.getParent().equals("")) {
                    extractedClass.printClass(0);
                }
            }
        }
    }

    public static void out(String s) {
        System.out.println(s);
    }

    public static void outa(String s) {
        System.out.print(s);
    }

    public static void slog(String msg) {
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("MM/dd/yyyy::HH:mm:ss a");
        System.out.println(s.format(d) + ":\t" + msg);
    }

}
