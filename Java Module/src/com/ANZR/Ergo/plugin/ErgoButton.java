package com.ANZR.Ergo.plugin;

import com.ANZR.Ergo.Ergo;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;

import javax.swing.*;
import java.util.Arrays;

public class ErgoButton extends AnAction {


    private LoadingBarWindow loadingBarWindow = new LoadingBarWindow();
    private Timer timer;

    public void setResults(Object results) {
        this.results = results;
    }

    private Object results;


    @Override
    public void actionPerformed(AnActionEvent e) {

        loadingBarWindow.updateLoadingBar("Loading Project", 0);
        loadingBarWindow.show();

        Project project = e.getData(LangDataKeys.PROJECT);
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Ergo");
        VirtualFile[] files = ProjectRootManager.getInstance(project).getContentSourceRoots();


        Thread thread = new Thread(() -> {
            String[] sourceRoots = Arrays.copyOf(Arrays.stream(files).map(VirtualFile::getPath).toArray(), files.length, String[].class);
            Ergo ergo = new Ergo(this);
            ergo.run(sourceRoots, loadingBarWindow);


        });
        thread.start();

        timer = new Timer(100, e1 -> {
            if(!thread.isAlive())
                createToolWindowAndHideLoadingBar(toolWindow, files, project);
        });
        timer.setRepeats(true);
        timer.start();


    }

    private void createToolWindowAndHideLoadingBar(ToolWindow toolWindow, VirtualFile[] files, Project project){

        Folder rootFolder = getRootFolder(project.getName(), files);

        ErgoToolWindow tool = new ErgoToolWindow();
        tool.populateToolWindow(toolWindow, rootFolder, project);
        loadingBarWindow.updateLoadingBar("Complete...", 100);

        Timer t = new Timer(100, e1 -> {
            loadingBarWindow.closeFrame();
        });
        t.setRepeats(false);
        t.start();
        timer.stop();
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

