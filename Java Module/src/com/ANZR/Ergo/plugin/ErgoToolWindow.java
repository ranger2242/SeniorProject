package com.ANZR.Ergo.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.util.containers.Stack;

import javax.swing.*;
import java.awt.*;

public class ErgoToolWindow implements ToolWindowFactory {

    private ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
    private JPanel contentWindow = new JPanel(new BorderLayout());
    private SideBar sideBar = new SideBar(this);
    private Table table;
    private JLabel errorLabel = new JLabel();
    private DirectoryElement rootDirectoryElement;
    private DirectoryElement currentDirectoryElement;
    private Stack<DirectoryElement> previousDirectoryElement = new Stack<>();
    private Project project;


    public Project getProject() {
        return project;
    }



    public void populateToolWindow(ToolWindow toolWindow, DirectoryElement rootDirectoryElement, Project project) {
        this.rootDirectoryElement = rootDirectoryElement;
        this.currentDirectoryElement = rootDirectoryElement;
        this.project = project;

        //Setup Side Bar
        contentWindow.add(sideBar, BorderLayout.LINE_START);
        //Setup Table
        table = new Table(this);
        table.setTableModel(currentDirectoryElement);
        contentWindow.add(new JScrollPane(table));
        contentWindow.add(table.getTableHeader(), BorderLayout.EAST);

        Content content = contentFactory.createContent(contentWindow, "- " + rootDirectoryElement.getName(), false);
        toolWindow.getContentManager().removeAllContents(true);
        toolWindow.getContentManager().addContent(content);
        toolWindow.show(null);
    }

    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
    }

    public void showErrorLabel(String error) {
        //change color make more noticeable
        errorLabel.setText(error);
        errorLabel.setVisible(true);
        contentWindow.add(errorLabel, BorderLayout.CENTER);
    }

    public void createModel(int rows) {
        if (previousDirectoryElement.isEmpty() || !currentDirectoryElement.isClass()){
            previousDirectoryElement.push(currentDirectoryElement);
            currentDirectoryElement = currentDirectoryElement.getChildElements().get(rows);
            table.setTableModel(currentDirectoryElement);
        }
        sideBar.setIfButtonIsEnabled();
    }


    //Getters and Setters
    public Table getTable() {
        return table;
    }

    public DirectoryElement getCurrentDirectoryElement() {
        return currentDirectoryElement;
    }

    public Stack<DirectoryElement> getPreviousDirectoryElement() {
        return previousDirectoryElement;
    }

    public DirectoryElement getRootDirectoryElement() {
        return rootDirectoryElement;
    }

    public void setCurrentDirectoryElement(DirectoryElement currentDirectoryElement) {
        this.currentDirectoryElement = currentDirectoryElement;
    }

}