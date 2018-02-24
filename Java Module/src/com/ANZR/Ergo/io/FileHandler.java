package com.ANZR.Ergo.io;

import com.ANZR.Ergo.parser.ExtractedDirectory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by USER on 11/18/2016.
 */
public class FileHandler {

    // If batch size =< 0 system will not use batching
    private int batchSize = -1;
    private String path;
    private ArrayList<File> listOfFiles = new ArrayList<>();
    private int batchStartIndex;
    private int batchEndIndex;
    private boolean hasNext = true;
    int c = 0;


    public FileHandler(String[] paths) {
        this.batchSize = -1;
        this.path = "";
    }

    public FileHandler(int batchSize, String path) {
        this.batchSize = batchSize;
        this.path = path;

        File folder = new File(path);
        if (folder.listFiles() != null) {
            listOfFiles.addAll(Arrays.asList(folder.listFiles()));
            batchStartIndex = 0;
            if (batchSize < listOfFiles.size()) {
                batchEndIndex = batchSize - 1;
            } else {
                batchEndIndex = listOfFiles.size();
            }

        } else {
            System.out.println("ERROR: DirectoryElement ->" + path + " is empty.");
        }


    }

    public static String load(File file) {//trying to upload new load function
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


    private ArrayList<String> getPathsForBatch(String path) {

        ArrayList<String> paths = new ArrayList<>();
        ArrayList<String> sourceFolderPaths = new ArrayList<>();
        ArrayList<File> batchList;

        if (batchEndIndex + 1 <= listOfFiles.size()) {
            batchList = new ArrayList<>(listOfFiles.subList(batchStartIndex, batchEndIndex + 1));
        } else {
            batchList = new ArrayList<>(listOfFiles.subList(batchStartIndex, listOfFiles.size()));
        }
        paths.addAll(batchList.stream().map(x -> x.getAbsolutePath()).collect(Collectors.toCollection(ArrayList::new)));

        for (String p : paths) {
            sourceFolderPaths.addAll(findSourceFolderPaths(p));
        }
        return sourceFolderPaths;
    }

    public ArrayList<ExtractedDirectory> nextBatch() {
        ArrayList<String> sourceFolderPaths = getPathsForBatch(path);
        ArrayList<ExtractedDirectory> directories = new ArrayList<>();
        for (String sourcePath : sourceFolderPaths) {
            directories.add(load(sourcePath, new ExtractedDirectory(sourcePath)));
        }
        incrementBatchIndices();
        return directories;

    }

    public ExtractedDirectory[] getProjectDirectory(String[] sourceFolderPaths) {
        ExtractedDirectory[] dirs = new ExtractedDirectory[sourceFolderPaths.length];
        for (int i = 0; i < sourceFolderPaths.length; i++) {
            dirs[i] = load(sourceFolderPaths[i], new ExtractedDirectory(sourceFolderPaths[i]));
        }
        return dirs;
    }


    private void incrementBatchIndices() {
        if (batchStartIndex + batchSize < listOfFiles.size()) {
            batchStartIndex += batchSize;
            if (batchEndIndex + batchSize < listOfFiles.size()) {
                batchEndIndex += batchSize;
            } else {
                batchEndIndex = listOfFiles.size();
            }
            hasNext = true;
        } else {
            // Over Batched restarting indices and returning false
            batchStartIndex = 0;
            if (batchSize < listOfFiles.size()) {
                batchEndIndex = batchSize - 1;
            } else {
                batchEndIndex = listOfFiles.size();
            }
            hasNext = false;
        }
    }

    public int batchesLeft() {
        int batchesLeft = 0;
        int start = batchStartIndex;
        while (start < listOfFiles.size()) {
            start += batchSize;
            batchesLeft += 1;
        }
        return batchesLeft - 1;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public static void writeToCSV(BufferedWriter writer, ArrayList<Object[][][]> matrices, ArrayList<ExtractedDirectory> directories) throws IOException {
        String toWrite = "";

        for (int i = 0; i < matrices.size(); i++) {
            Object[][] matrix = matrices.get(i)[0];
            for (int j = 0; j < matrix.length; j++) {
                for (int k = 0; k < matrix[j].length; k++) {
                    if (matrix[j][k] instanceof Double)
                        toWrite += (double) matrix[j][k];
                    else if (matrix[j][k] instanceof Integer)
                        toWrite += (Integer) matrix[j][k];
                    else if (matrix[j][k] instanceof String)
                        toWrite += (String) matrix[j][k];
                    if (k < matrix[j].length - 1) {
                        toWrite += ",";
                    }
                }
                toWrite += directories.get(i).getName() + "\n";
            }
        }

        writer.append(toWrite);
    }

    private ArrayList<String> findSourceFolderPaths(String path) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> sourcePaths = new ArrayList<>();
        for (File f : listOfFiles != null ? listOfFiles : new File[0]) {
            if (f.isDirectory() && f.getName().equals("src")) {
                sourcePaths.add(f.getAbsolutePath());
                int i = countSrcFiles(f.getAbsolutePath(), 0);
                //Logger.slog("TOTAL FILES: "+i+" " + f.getAbsolutePath());
            }
        }
        for (File f : listOfFiles != null ? listOfFiles : new File[0]) {
            if (f.isDirectory()) {
                sourcePaths.addAll(findSourceFolderPaths(path + "\\" + f.getName()));
            }
        }
        return sourcePaths;
    }

    private int countSrcFiles(String absolutePath, int c) {
        File folder = new File(absolutePath);
        File[] listOfFiles = folder.listFiles();
        for (File f : listOfFiles != null ? listOfFiles : new File[0]) {
            if (f.isFile() && f.getAbsolutePath().endsWith(".java")) {
                c++;
            }
        }
        for (File f : listOfFiles != null ? listOfFiles : new File[0]) {
            if (f.isDirectory()) {
                c += countSrcFiles(absolutePath + "\\" + f.getName(), 0);
            }
        }


        return c;
    }

    private ExtractedDirectory load(String path, ExtractedDirectory e) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        for (File listOfFile : listOfFiles != null ? listOfFiles : new File[0]) {
            if (listOfFile.isFile() && listOfFile.getName().endsWith(".java")) {
                //System.out.println("File " + listOfFile.getName());
            } else if (listOfFile.isDirectory()) {
                e.addPackage(load(path + "\\" + listOfFile.getName(), new ExtractedDirectory(listOfFile.getName())));
                //System.out.println("Directory " + listOfFile.getName());
            }
        }
        ArrayList<String> paths = new ArrayList<>();
        ArrayList<File> f = Arrays.stream(listOfFiles).filter(x -> x.getAbsolutePath().endsWith(".java")).collect(Collectors.toCollection(ArrayList::new));
        f.forEach(x -> paths.add(x.getAbsolutePath()));
        e.setClassPaths(paths);
        return e;
    }

    public static void deleteFile(String fileName) {
        File file = new File(fileName);
        file.delete();
        file = null;
    }

    public static BufferedWriter setupFileWriter(String fileName) {
        try {
            return new BufferedWriter(new FileWriter(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getRootDirectory() {
        String rootDirectory = System.getProperty("user.dir");
        while (!rootDirectory.endsWith("\\")) {
            rootDirectory = rootDirectory.substring(0, rootDirectory.length() - 1);
        }
        rootDirectory = rootDirectory.substring(0, rootDirectory.length() - 1);
        return rootDirectory;
    }

}

