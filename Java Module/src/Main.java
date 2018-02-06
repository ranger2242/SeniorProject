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
    private static boolean trainingMode = true;

    private static String path = "C:\\Users\\Ross\\Desktop\\stuf";


    public static void main(String[] args) {
        FileHandler fh = new FileHandler();
        GodObjectTransformer godObjectTransformer = new GodObjectTransformer();
        Logger.slog("Setting up file writer");

        String rootDirectory = fh.getRootDirectory();
        String fileName = rootDirectory + "\\training_dataset.csv";
        BufferedWriter writer = fh.setupFileWriter(fileName);


        Logger.slog("Loading path: "+ path);

        int batchSize = 2;
        FileHandler fileHandler = new FileHandler(batchSize, path);

        int batchNumber = 1;
        while ( fileHandler.hasNext() ){

            System.out.println(div);
            Logger.slog("Batch: " + batchNumber + " - Batches Left: " + fileHandler.batchesLeft());
            ArrayList<ExtractedDir> directories = fileHandler.nextBatch();
            Logger.slog("Directories loaded. " + directories.size() + " directories founds.");

            Logger.slog("Parsing classes...");
            ProjectData<ArrayList<Set<ExtractedClass>>, ArrayList<Set<Enum>>> projectClassData = Parser.parseDirectories(directories);
            Logger.slog("Classes Parsed");


            if (trainingMode) {
                ArrayList<Object[][][]> matrices = godObjectTransformer.transformTrainingData(projectClassData);

                try{
                    FileHandler.writeToCSV(writer, matrices, directories);
                    Logger.slog("Data writen to CSV file");
                }catch (IOException e){e.printStackTrace();}

            } else {

                Object[][][] matrices = godObjectTransformer.transformData(projectClassData);

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
        }catch (IOException e){e.printStackTrace();}

    }

}
