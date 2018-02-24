package com.ANZR.Ergo.io;

import com.ANZR.Ergo.plugin.AntiPattern;
import com.ANZR.Ergo.plugin.Folder;
import com.google.gson.JsonElement;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.webcore.util.JsonUtil;

public class DataLoader {

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
    public static Folder getAssociatedPatterns(Folder folders, JsonElement data){
        for (Folder folder : folders.getFolders()) {
            //finds a class, folder's virtual files are all null
            if(folder.getVirtualFile() != null){
                //find all patterns that are part of that class
//                for(JsonElement d : data.getAsJsonArray()){
//                    d.getAsJsonObject();
//                    if(folder.getVirtualFile().getName() == d[1] && folder.getVirtualFile().getPath() == d[2])
//                        folder.addAntiPattern(new AntiPattern("God Object", d[0]));
//                }
            }
        }

        return folders;
    }
}
