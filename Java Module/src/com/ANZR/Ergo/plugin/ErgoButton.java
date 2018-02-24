package com.ANZR.Ergo.plugin;

import com.ANZR.Ergo.Ergo;
import com.ANZR.Ergo.io.DataLoader;
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
    private Folder rootFolder = new Folder();
    private Project project;
    private VirtualFile[] files;

    @Override
    public void actionPerformed(AnActionEvent e) {

        loadingBarWindow.updateLoadingBar("Loading Project", 0);
        loadingBarWindow.show();

        project = e.getData(LangDataKeys.PROJECT);
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Ergo");
        files = ProjectRootManager.getInstance(project).getContentSourceRoots();


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
        timer.setRepeats(true);//repeating endlessly?
        timer.start();


    }

    private void createToolWindowAndHideLoadingBar(ToolWindow toolWindow, VirtualFile[] files, Project project){

        rootFolder = DataLoader.loadProjectFolder(project.getName(), files);

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

    public Folder getRootFolder() {
        return rootFolder;
    }

    public Project getProject() {
        return project;
    }

    public VirtualFile[] getFiles() {
        return files;
    }

    public void setRootFolder(Folder folder) {
        rootFolder = folder;
    }

    @Override
    public void update(AnActionEvent e) {
    }

}

