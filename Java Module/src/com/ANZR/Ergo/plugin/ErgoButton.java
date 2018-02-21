package com.ANZR.Ergo.plugin;

import com.ANZR.Ergo.Ergo;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;

import java.util.Arrays;

public class ErgoButton extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        Project project = e.getData(LangDataKeys.PROJECT);

        //Loading bar wont popup unless server is runnning. idk...
        //It also doesnt show up until after parsing. idk...

        LoadingBarWindow loadingBarWindow = new LoadingBarWindow(project);
        loadingBarWindow.setText("This is a test");
        loadingBarWindow.setProgress(50);

        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Ergo");
        VirtualFile[] files = ProjectRootManager.getInstance(project).getContentSourceRoots();
        Folder rootFolder = getRootFolder(project.getName(), files);
        String[] sourceRoots = Arrays.copyOf(Arrays.stream(files).map(VirtualFile::getPath).toArray(),files.length,String[].class);
        Ergo.run(sourceRoots);

//        Logger.printProjectFiles(rootFolder);
        ErgoToolWindow tool = new ErgoToolWindow();
        tool.populateToolWindow(toolWindow, rootFolder, project);
    }

    @Override
    public void update(AnActionEvent e) {
    }

    private Folder getRootFolder(String folderName, VirtualFile[] sourceFolders) {
        Folder buildFolder = new Folder(folderName);

        for (VirtualFile file : sourceFolders) {

            if (file.getFileType().getName().equals("JAVA")) {
                buildFolder.addFolder(new Folder(file.getName(), true, file));
            } else if (file.isDirectory()) {
                Folder childFolder = getRootFolder(file.getName(), file.getChildren());
                buildFolder.addFolder(childFolder);
            } else {
                ///File was not a directory or Java file.
            }
        }

        return buildFolder;
    }


}

