package com.ANZR.Ergo.plugin;

import com.ANZR.Ergo.Ergo;
import com.intellij.notification.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.HttpURLConnection;
import java.net.URL;
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


        if (!loadingBarWindow.updateLoadingBar("Loading Project", 0)) {
            Project project = e.getData(LangDataKeys.PROJECT);
            Ergo ergo = new Ergo(project);

            VirtualFile[] files2 = ProjectRootManager.getInstance(project).getContentSourceRoots();

            //VirtualFileManager.getInstance().asyncRefresh(null);
            new OpenFileDescriptor(project, files2[0]).navigate(true);
            try {
                Robot r = new Robot();
                r.setAutoDelay(200);
                r.keyPress(KeyEvent.VK_CONTROL);
                r.keyPress(KeyEvent.VK_S);
                r.keyRelease(KeyEvent.VK_S);
                r.keyRelease(KeyEvent.VK_CONTROL);
            } catch (AWTException e1) {
                e1.printStackTrace();
            }
            FileDocumentManager.getInstance().saveAllDocuments();
            if (checkConnection()) {
                loadingBarWindow.show();
                new Thread(() -> {
                    VirtualFile[] files = ProjectRootManager.getInstance(project).getContentSourceRoots();
                    String[] sourceRoots = Arrays.copyOf(Arrays.stream(files).map(VirtualFile::getPath).toArray(), files.length, String[].class);

                    project.getBaseDir().refresh(false, true);
                    ergo.run(sourceRoots, loadingBarWindow);

                }).start();

            }
            timer = new Timer(100, e1 -> {

                DirectoryElement tableData = ergo.getTableData();
                if (tableData != null)
                    createToolWindowAndHideLoadingBar(project, tableData);
                if (loadingBarWindow.getClosed()) {
                    loadingBarWindow.closeFrame();
                    createToolWindowAndHideLoadingBar(project, null);
                }
            });
            timer.setRepeats(true);
            timer.start();
        }

    }

    public static void displayNotification(String msg, NotificationType type) {
        NotificationGroup GROUP_DISPLAY_ID_INFO =
                new NotificationGroup("ERGO notification group",
                        NotificationDisplayType.BALLOON, true);
        Notification notification = GROUP_DISPLAY_ID_INFO.createNotification(msg, type);
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        Notifications.Bus.notify(notification, projects[0]);
    }

    private boolean checkConnection() {
        try {
            try {
                URL url = new URL("http://www.google.com");
                System.out.println(url.getHost());
                HttpURLConnection con = (HttpURLConnection) url
                        .openConnection();
                con.connect();
                if (con.getResponseCode() == 200) {
                    System.out.println("Connection established!!");
                    return true;
                }
            } catch (Exception exception) {
                System.out.println("No Connection");
                displayNotification("Connection failure. Network connection not found.", NotificationType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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

    public static void halt() {

    }
}

