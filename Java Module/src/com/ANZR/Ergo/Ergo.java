package com.ANZR.Ergo;

import com.ANZR.Ergo.io.DataInterpreter;
import com.ANZR.Ergo.io.FileHandler;
import com.ANZR.Ergo.io.Logger;
import com.ANZR.Ergo.io.Pipeline;
import com.ANZR.Ergo.parser.*;
import com.ANZR.Ergo.plugin.ErgoButton;
import com.ANZR.Ergo.plugin.LoadingBarWindow;
import com.ANZR.Ergo.transformer.Transformer;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Chris Cavazos on 10/1/2017.
 */


@SuppressWarnings("ALL")
public class Ergo {

    public final static String div = "-----------------------------------------------";
    private boolean trainingMode = false;
    private LoadingBarWindow progressBar;
    private ErgoButton buttonClass;


    public Ergo(ErgoButton button){
        this.buttonClass = button;
    }

    public void run(String[] sourceFolderPaths, LoadingBarWindow loadingBarWindow){

        if(trainingMode){
            runErgoTrainingMode();
        }else{
            progressBar = loadingBarWindow;
            runErgoClient(sourceFolderPaths);
        }
    }

    private void runErgoTrainingMode(){

        String path = "";

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
            ProjectData<ArrayList<Set<ExtractedClass>>, ArrayList<Set<com.ANZR.Ergo.parser.Enum>>> projectClassData = Parser.parseDirectories(directories);
            Logger.slog("Classes Parsed");


            ArrayList<Object[][][]> matrices = new ArrayList<>();
            for(int i = 0; i < projectClassData.parsedClasses.size();i++ ){
                Transformer transformer = new Transformer(projectClassData.parsedClasses.get(i), projectClassData.globalEnums.get(i));
                matrices.add(transformer.transform());
            }
            try{
                FileHandler.writeToCSV(writer, matrices, directories);
                Logger.slog("Data writen to CSV file");
            }catch (IOException e){e.printStackTrace();}

            Logger.slog("Batch Complete");
            batchNumber++;
        }

        try{
            writer.close();
        }catch (IOException e){e.printStackTrace();}

    }

    private void runErgoClient(String[] sourceFolderPaths){

        FileHandler fileHandler = new FileHandler(sourceFolderPaths);
        ExtractedDir[] directories = fileHandler.getProjectDirectory(sourceFolderPaths);

        progressBar.updateLoadingBar("Parsing classes...", 20);
        Logger.slog("Parsing classes...");
        ProjectData<Set<ExtractedClass>, Set<com.ANZR.Ergo.parser.Enum>> projectClassData = Parser.parseProject(directories);
        Logger.slog("Classes Parsed");

        progressBar.updateLoadingBar("Processing Data...", 50);
        Transformer transformer = new Transformer(projectClassData.parsedClasses, projectClassData.globalEnums);
        Object[][][] matrices = transformer.transform();

        Gson gson = new Gson();
        String json = gson.toJson(matrices);

        progressBar.updateLoadingBar("Anaylzing...", 60);
        Pipeline pipeline = new Pipeline(this);
        pipeline.sendToServer(json);

    }

    public void interpretData(JsonElement data){

        System.out.println(data.toString());

        progressBar.updateLoadingBar("Generating results...", 90);

//        Results Something = DataInterpreter.interpret(data);
//
//        buttonClass.setResults(some);




    }


}
