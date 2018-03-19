package com.ANZR.Ergo.io;

import com.ANZR.Ergo.plugin.AntiPattern;
import com.ANZR.Ergo.plugin.DirectoryElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataLoader {


    private static final String[] antiPatternNames = {"God Object", "Long Method", "Constant Interface"};

    /**
     * Loads a Project data structure to be used to file the Ergo table
     *
     * @param project An intellij SDK data structure used to load the project
     * @return Project data structure to be used to file the Ergo table
     */
    public static DirectoryElement loadProjectFolder(Project project) {
        VirtualFile[] files = ProjectRootManager.getInstance(project).getContentSourceRoots();
        return loadProjectFolder(project.getName(), files);
    }

    public static DirectoryElement loadProjectFolder(String folderName, VirtualFile[] sourceFolders) {
        DirectoryElement buildDirectoryElement = new DirectoryElement(folderName);

        for (VirtualFile file : sourceFolders) {

            if (file.getFileType().getName().equals("JAVA")) {
                buildDirectoryElement.addFolder(new DirectoryElement(file.getName(), file));
            } else if (file.isDirectory()) {
                DirectoryElement childDirectoryElement = loadProjectFolder(file.getName(), file.getChildren());
                buildDirectoryElement.addFolder(childDirectoryElement);
            } else {
                ///File was not a directory or Java file.
            }
        }

        return buildDirectoryElement;
    }

    public static DirectoryElement getAssociatedPatterns(Project project, JsonElement data) {
        DirectoryElement folders = loadProjectFolder(project);
        return getAssociatedPatterns(folders, data);
    }

    public static DirectoryElement getAssociatedPatterns(DirectoryElement folders, JsonElement data) {
        //Cycle through classes
        for (DirectoryElement directoryElement : folders.getChildElements()) {

            if (directoryElement.isDirectory()) {
                getAssociatedPatterns(directoryElement, data);
                continue;
            }

            JsonArray dataArray = data.getAsJsonArray();

            //Cycle through anti-patterns
            for (int i = 0; i < dataArray.size(); i++) {
                JsonArray patterns = dataArray.get(i).getAsJsonArray();
                //Cycle through and match each class to each result
                for (JsonElement jsonElement : patterns) {
                    JsonArray array = jsonElement.getAsJsonArray();
                    int result = array.get(0).getAsInt();
                    if (classesAreSame(array, directoryElement) && result != 0)
                        directoryElement.addAntiPattern(new AntiPattern(antiPatternNames[i], result));
                }
            }
        }

        return folders;
    }


    /**
     * Checks if class of a directoryElement and json array point to same class
     *
     * @param array            A Json array retrived from the Ergo Server
     * @param directoryElement A Java file
     * @return A boolean that indicated if the array and Java file are a match
     */
    private static boolean classesAreSame(JsonArray array, DirectoryElement directoryElement) {
        String jsonClassName = array.get(1).getAsString() + ".java";
        String jsonClassPath = array.get(2).getAsString();
        String className = directoryElement.getVirtualFile().getName();
        String classPath = directoryElement.getVirtualFile().getPath();

        try {
            boolean isSamePath = Files.isSameFile(Paths.get(jsonClassPath), Paths.get(classPath));
            return className.equals(jsonClassName) && isSamePath;
        } catch (IOException e) {
            return false;
        }
    }


}
