package com.ANZR.Ergo.io;

import com.ANZR.Ergo.plugin.AntiPattern;
import com.ANZR.Ergo.plugin.Folder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.webcore.util.JsonUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataLoader {

    private static final String[] antiPatternNames =  {"God Object", "Long Method"};

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
            for (int i = 0; i<dataArray.size();i++) {
                JsonArray patterns = dataArray.get(i).getAsJsonArray();
                //Cycle through and match each class to each result
                for (JsonElement jsonElement : patterns) {
                    JsonArray array = jsonElement.getAsJsonArray();
                    int result = array.get(0).getAsInt();
                    String jsonClassName = array.get(1).getAsString() + ".java";
                    String jsonClassPath = array.get(2).getAsString();
                    String className = folder.getVirtualFile().getName();
                    String classPath = folder.getVirtualFile().getPath();

                    try {
                        boolean isSamePath = Files.isSameFile(Paths.get(jsonClassPath), Paths.get(classPath));
                        if (className.equals(jsonClassName) && isSamePath && result != 0)
                            folder.addAntiPattern(new AntiPattern(antiPatternNames[i]));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return folders;
    }



}
