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
    private static String path = "C:\\Users\\Ross\\Desktop\\GOs";


    public static void main(String[] args) {

        Logger.slog("Setting up file writer");

        String rootDirectory = FileHandler.getRootDirectory();
        String fileName = rootDirectory + "\\training_dataset.csv";
        BufferedWriter writer = FileHandler.setupFileWriter(fileName);


        Logger.slog("Loading path: "+ path);

        int batchSize = 5;
        FileHandler fileHandler = new FileHandler(batchSize, path);

        int batchNumber = 1;
        while ( fileHandler.hasNext() ){

            System.out.println(div);
            Logger.slog("Batch: " + batchNumber + " - Batches Left: " + fileHandler.batchesLeft());
            ArrayList<ExtractedDir> directories = fileHandler.nextBatch();
            if(directories.size() == 0){
                Logger.slog("No directories found... Loading next batch");
                batchNumber++;
                continue;
            }

            Logger.slog("Directories loaded. " + directories.size() + " directories founds.");

            Logger.slog("Parsing classes...");
            ProjectData<ArrayList<Set<ExtractedClass>>, ArrayList<Set<Enum>>> projectClassData = Parser.parseDirectories(directories);
            Logger.slog("Classes Parsed");


            if (trainingMode) {
                ArrayList<Object[][][]> matrices = new ArrayList<>();
                for(int i = 0; i < projectClassData.parsedClasses.size();i++ ){
                    Transformer transformer = new Transformer(projectClassData.parsedClasses.get(i), projectClassData.globalEnums.get(i));
                     matrices.add(transformer.transform());
                }
                try{
                    FileHandler.writeToCSV(writer, matrices, directories);
                    Logger.slog("Data writen to CSV file");
                }catch (IOException e){e.printStackTrace();}

            } else {
                Transformer transformer = new Transformer(projectClassData.parsedClasses.get(0), projectClassData.globalEnums.get(0));
                Object[][][] matrices = transformer.transform();

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
