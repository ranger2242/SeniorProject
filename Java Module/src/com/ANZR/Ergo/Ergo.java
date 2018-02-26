package com.ANZR.Ergo;

import com.ANZR.Ergo.io.DataLoader;
import com.ANZR.Ergo.io.FileHandler;
import com.ANZR.Ergo.io.Logger;
import com.ANZR.Ergo.io.Pipeline;
import com.ANZR.Ergo.parser.*;
import com.ANZR.Ergo.plugin.DirectoryElement;
import com.ANZR.Ergo.plugin.LoadingBarWindow;
import com.ANZR.Ergo.transformer.Transformer;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.intellij.openapi.project.Project;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;


@SuppressWarnings("ALL")
public class Ergo {

    public int test = 0;
    public final static String div = "-----------------------------------------------";
    private boolean trainingMode = false;
    private LoadingBarWindow progressBar;
    private Project project;
    DirectoryElement tableData = null;

    /**
     * Create an instance of Ergo
     *
     * @param project This refernce is used to create the data stucture for the table
     */
    public Ergo(Project project) {
        this.project = project;
    }

    /**
     * Begin Ergo execution
     *
     * @param sourceFolderPaths The folder paths that contain the java files that Ergo will anaylze
     * @param loadingBarWindow  A loading bar to give the user a visual on the process of execution
     */
    public void run(String[] sourceFolderPaths, LoadingBarWindow loadingBarWindow) {

        if (trainingMode) {
            runErgoTrainingMode();
        } else {
            progressBar = loadingBarWindow;
            runErgoClient(sourceFolderPaths);
        }
    }

    private void runErgoTrainingMode() {

        String path = "C:\\Users\\Chris\\Desktop\\JAVA";

        Logger.slog("Setting up file writer");

        String rootDirectory = FileHandler.getRootDirectory();
        String fileName = rootDirectory + "\\training_dataset.csv";
        BufferedWriter writer = FileHandler.setupFileWriter(fileName);


        Logger.slog("Loading path: " + path);

        int batchSize = 5;
        FileHandler fileHandler = new FileHandler(batchSize, path);

        int batchNumber = 1;
        while (fileHandler.hasNext()) {

            System.out.println(div);
            Logger.slog("Batch: " + batchNumber + " - Batches Left: " + fileHandler.batchesLeft());
            ArrayList<ExtractedDirectory> directories = fileHandler.nextBatch();
            if (directories.size() == 0) {
                Logger.slog("No directories found... Loading next batch");
                batchNumber++;
                continue;
            }

            Logger.slog("Directories loaded. " + directories.size() + " directories founds.");

            Logger.slog("Parsing classes...");
            ProjectData<ArrayList<Set<ExtractedClass>>, ArrayList<Set<com.ANZR.Ergo.parser.Enum>>> projectClassData = Parser.parseDirectories(directories);
            Logger.slog("Classes Parsed");


            ArrayList<Object[][][]> matrices = new ArrayList<>();
            for (int i = 0; i < projectClassData.parsedClasses.size(); i++) {
                Transformer transformer = new Transformer(projectClassData.parsedClasses.get(i), projectClassData.globalEnums.get(i));
                matrices.add(transformer.transform());
            }
            try {
                FileHandler.writeToCSV(writer, matrices, directories);
                Logger.slog("Data writen to CSV file");
            } catch (IOException e) {
                e.printStackTrace();
            }

            Logger.slog("Batch Complete");
            batchNumber++;
        }

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void runErgoClient(String[] sourceFolderPaths) {

        FileHandler fileHandler = new FileHandler(sourceFolderPaths);
        ExtractedDirectory[] directories = fileHandler.getProjectDirectory(sourceFolderPaths);

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

    public void interpretData(JsonElement data) {
        progressBar.updateLoadingBar("Generating results...", 90);
        tableData = DataLoader.getAssociatedPatterns(project, data);
    }

    public DirectoryElement getTableData() {
        return tableData;
    }

}
