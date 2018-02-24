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

    private ToolWindow toolWindow;

    @Override
    public void actionPerformed(AnActionEvent e) {

        loadingBarWindow.updateLoadingBar("Loading Project", 0);
        loadingBarWindow.show();

        project = e.getData(LangDataKeys.PROJECT);
        toolWindow= ToolWindowManager.getInstance(project).getToolWindow("Ergo");
        files = ProjectRootManager.getInstance(project).getContentSourceRoots();
        rootFolder = DataLoader.loadProjectFolder(project.getName(), files);
        Ergo ergo = new Ergo(this);

        new Thread(() -> {
            String[] sourceRoots = Arrays.copyOf(Arrays.stream(files).map(VirtualFile::getPath).toArray(), files.length, String[].class);
            ergo.run(sourceRoots, loadingBarWindow);
        }).start();

        timer = new Timer(100, e1 -> {
            if(ergo.getTableData() != null)
                createToolWindowAndHideLoadingBar(toolWindow, project);
        });
        timer.setRepeats(true);
        timer.start();


    }

    private void createToolWindowAndHideLoadingBar(ToolWindow toolWindow, Project project){
        ErgoToolWindow tool = new ErgoToolWindow();
        tool.populateToolWindow(toolWindow, rootFolder, project);
        loadingBarWindow.updateLoadingBar("Complete...", 100);
        Timer t = new Timer(100, e2 -> loadingBarWindow.closeFrame());
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

