import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Chris Cavazos on 10/1/2017.
 */


@SuppressWarnings("ALL")
class Main {

    public final static String div = "-----------------------------------------------";
    public static ArrayList<Set<ExtractedClass>> parsedClasses = new ArrayList<>();
    public static ArrayList<Set<Enum>> globalEnums = new ArrayList<>();
    private static boolean trainingMode = true;



    public static void main(String[] args) {
        FileHandler fh = new FileHandler();
        GodObjectTransformer got = new GodObjectTransformer();
        Logger.slog("Setting up file writer");

        String rootDirectory = fh.getRootDirectory();
        String fileName = rootDirectory + "\\training_dataset.csv";
        BufferedWriter writer = fh.setupFileWriter(fileName);

        String path = "C:\\Users\\Chris\\Desktop\\TypeCalc";
        Logger.slog("Loading path: "+path);

        int batchSize = 2;
        FileHandler fileHandler = new FileHandler(batchSize, path);

        int batchNumber = 1;
        while ( fileHandler.hasNext() ){

            System.out.println(div);
            Logger.slog("Batch: " + batchNumber + " - Batches Left: " + fileHandler.batchesLeft());
            ArrayList<ExtractedDir> directories = fileHandler.nextBatch();
            Logger.slog("Directories loaded. " + directories.size() + " directories founds.");

            Logger.slog("Parsing classes...");
            Tuple<ArrayList<Set<ExtractedClass>>, ArrayList<Set<Enum>>> tuple = Parser.parseDirectories(directories);
            parsedClasses = tuple.parsedClasses;
            globalEnums = tuple.globalEnums;
            Logger.slog("Classes Parsed");


            if (trainingMode) {
                ArrayList<Object[][][]> matrices = got.transformTrainingData(tuple);

                try{
                    FileHandler.writeToCSV(writer, matrices, directories);
                    Logger.slog("Data writen to CSV file");
                }catch (IOException e){e.printStackTrace();}

            } else {

                Object[][][] matrices = got.transformData(tuple);

                Gson gson = new Gson();
                String json = gson.toJson(matrices);

                Pipeline pipeline = new Pipeline();
                pipeline.sendToServer(json);

            }
            Logger.slog("Batch Complete");
            batchNumber++;
        }
        try{
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

}
