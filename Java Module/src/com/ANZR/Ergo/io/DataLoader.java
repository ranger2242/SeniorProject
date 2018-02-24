package com.ANZR.Ergo.io;

import com.ANZR.Ergo.plugin.AntiPattern;
import com.ANZR.Ergo.plugin.Folder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataLoader {

    private static final String[] antiPatternNames = {"God Object", "Long Method"};

    public static Folder loadProjectFolder(String folderName, VirtualFile[] sourceFolders) {
        Folder buildFolder = new Folder(folderName);

        for (VirtualFile file : sourceFolders) {

            if (file.getFileType().getName().equals("JAVA")) {
                buildFolder.addFolder(new Folder(file.getName(), file));
            } else if (file.isDirectory()) {
                Folder childFolder = loadProjectFolder(file.getName(), file.getChildren());
                buildFolder.addFolder(childFolder);
            } else {
                ///File was not a directory or Java file.
            }
        }

        return buildFolder;
    }

    public static Folder getAssociatedPatterns(Folder folders, JsonElement data) {
        //Cycle through classes
        for (Folder folder : folders.getFolders()) {

            if (folder.isDirectory()) {
                getAssociatedPatterns(folder, data);
                continue;
            }

            JsonArray dataArray = data.getAsJsonArray();

            //Cycle through anti-patterns
            for (int i = 0; i < dataArray.size(); i++) {
                switch (i){
                    case 0:
                        mapGodObjects(dataArray, folder);
                        break;
                    case 1:
                        mapLongMethods(dataArray, folder);
                        break;

                }
            }
        }

        return folders;
    }

    private static void mapGodObjects(JsonArray dataArray, Folder folder){
        JsonArray patterns = dataArray.get(0).getAsJsonArray();
        //Cycle through and match each class to each result
        for (JsonElement jsonElement : patterns) {
            JsonArray array = jsonElement.getAsJsonArray();
            int result = array.get(0).getAsInt();
            if(classesAreSame(array, folder) && result != 0)
                folder.addAntiPattern(new AntiPattern(antiPatternNames[0]));
        }

    }

    private static void mapLongMethods(JsonArray dataArray, Folder folder){
        JsonArray patterns = dataArray.get(1).getAsJsonArray();
        //Cycle through and match each class to each result
        for (JsonElement jsonElement : patterns) {
            JsonArray array = jsonElement.getAsJsonArray();
            int result = array.get(0).getAsInt();
            if(classesAreSame(array, folder) && result != 0)
                folder.addAntiPattern(new AntiPattern(antiPatternNames[1]));
        }

    }


    /** Checks if class of a folder and json array point to same class */
    private static boolean classesAreSame(JsonArray array, Folder folder){
        String jsonClassName = array.get(1).getAsString() + ".java";
        String jsonClassPath = array.get(2).getAsString();
        String className = folder.getVirtualFile().getName();
        String classPath = folder.getVirtualFile().getPath();

        try {
            boolean isSamePath = Files.isSameFile(Paths.get(jsonClassPath), Paths.get(classPath));
            return className.equals(jsonClassName) && isSamePath;
        } catch (IOException e) {
            return false;
        }
    }


}
