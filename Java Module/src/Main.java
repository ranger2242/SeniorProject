import com.google.gson.Gson;
import io.socket.client.Socket;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Chris Cavazos on 10/1/2017.
 */



@SuppressWarnings("ALL")
class Main {

    public final static String div = "-----------------------------------------------";
    public static Set<ExtractedClass> parsedClasses = new LinkedHashSet<>();
    public static Set<Enum> globalEnums = new LinkedHashSet<>();

    static String port = "2242";
    static String ip="10.133.228.186";
    static String ip3="139.94.249.166";

    static String ip2= "localhost";
    public static String dir = "http://" +ip2+ ":" + port;
    static Gson gson = new Gson();

    static Socket socket;
    private static String clientID;

    private static boolean trainingMode = true;

    public static void main(String[] args) {


        //String path = "ex\\ex5";
        String path = "C:\\Users\\Ross\\Desktop\\test_dataset";
        //String path = "C:\\Users\\Chris\\Google Drive\\JAVA\\untitled";

        FileHandler fileHandler = new FileHandler();
        Parser parser = new Parser(fileHandler.load(path));
        slog("Loaded dir: " + path);
        parsedClasses = new LinkedHashSet<>(parser.getExtractedClasses());
        globalEnums = new LinkedHashSet<>(parser.getGlobalEnums());
        slog("Classes Parsed");

        Transformer transformer = new Transformer(parsedClasses, globalEnums);
        double[][][] matrices = transformer.transform();
        slog("Data Transformed");

        if (trainingMode) {
            try{
                writeToCSV(matrices[0]);
                slog("Data writen to CSV file");
            }catch (IOException e){
                System.out.println(e.getLocalizedMessage());
            }

        }else{

            Gson gson = new Gson();
            String json = gson.toJson(matrices);

            Pipeline pipeline = new Pipeline();
            pipeline.sendToServer(json);

        }


    }

    public static void writeToCSV(double[][] matrix) throws IOException{
        String toWrite = "";
        String rootDirectory = getRootDirectory();
        String fileName = rootDirectory + "\\training_dataset.csv";

        for (int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[i].length; j++) {
                toWrite += matrix[i][j];
                if (j < matrix[i].length - 1){
                    toWrite += ",";
                }
            }
            toWrite += "\n";
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(toWrite);
        writer.close();


    }


    private static String getRootDirectory(){
        String rootDirectory = System.getProperty("user.dir");
        while (!rootDirectory.endsWith("\\")){
            rootDirectory = rootDirectory.substring(0, rootDirectory.length() - 1);
        }
        rootDirectory = rootDirectory.substring(0, rootDirectory.length() - 1);
        return rootDirectory;
    }

    private static void printAllClasses() {
        for (ExtractedClass extractedClass : parsedClasses) {
            if (extractedClass.getParent().equals("")) {
                extractedClass.printClass(0);
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
