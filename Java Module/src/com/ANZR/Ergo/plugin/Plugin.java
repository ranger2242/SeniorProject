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

public class Plugin extends AnAction {

    private LoadingBarWindow loadingBarWindow;
    private Timer timer;

    /**
     * The Ergo Button was pressed. The plugin will begin to execute
     *
     * @param e This will provide the project data need to begin processing
     */
    @Override
    public void actionPerformed(AnActionEvent e) {
        loadingBarWindow = new LoadingBarWindow();

        if (!loadingBarWindow.updateLoadingBar("Loading Project", 0)){

            loadingBarWindow.show();

            Project project = e.getData(LangDataKeys.PROJECT);
            Ergo ergo = new Ergo(project);

            new Thread(() -> {
                VirtualFile[] files = ProjectRootManager.getInstance(project).getContentSourceRoots();
                String[] sourceRoots = Arrays.copyOf(Arrays.stream(files).map(VirtualFile::getPath).toArray(), files.length, String[].class);
                ergo.run(sourceRoots, loadingBarWindow);
            }).start();

            timer = new Timer(100, e1 -> {

                DirectoryElement tableData = ergo.getTableData();
                if (tableData != null)
                    createToolWindowAndHideLoadingBar(project, tableData);
                if(loadingBarWindow.getClosed()) {
                    loadingBarWindow.closeFrame();
                    createToolWindowAndHideLoadingBar(project, null);
                }
            });
            timer.setRepeats(true);
            timer.start();
        }

    }

    /**
     * Once processing is done, this will create the Ergo side window to display anti-patterns
     *
     * @param project The intellij project data structure
     */
    private void createToolWindowAndHideLoadingBar(Project project, DirectoryElement rootDirectoryElement) {
        if (rootDirectoryElement != null) {
            ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Ergo");
            ErgoToolWindow tool = new ErgoToolWindow();
            tool.populateToolWindow(toolWindow, rootDirectoryElement, project);
            loadingBarWindow.updateLoadingBar("Complete...", 100);


        }
        Timer t = new Timer(100, e2 -> loadingBarWindow.closeFrame());

        t.setRepeats(false);
        t.start();
        timer.stop();
    }

    @Override
    public void update(AnActionEvent e) {
    }

}

