package com.ANZR.Ergo.plugin;

import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.stream.Collectors;

public class SideBar extends JPanel {

    private JButton rootButton = new JButton();
    private JButton previousButton = new JButton();
    private JButton nextButton = new JButton();
    private JButton sourceButton = new JButton();
    private ErgoToolWindow parent;

    SideBar(ErgoToolWindow parent) {
        super(new GridBagLayout());
        this.parent = parent;

        setButtons();

        int gridBagRow = 0;
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.gridx = 0;
        for (int x = 0; x < 4; x++) {
            constraints.gridy = gridBagRow;
            switch (x){
                case 0:
                    add(rootButton, constraints);
                    break;
                case 1:
                    add(previousButton, constraints);
                    break;
                case 2:
                    add(nextButton, constraints);
                    break;
                case 3:
                    constraints.weighty = 20;
                    add(sourceButton, constraints);
                    break;
            }
            gridBagRow++;
        }

    }

    public void setIfButtonIsEnabled(){
        if (parent.getPreviousDirectoryElement().isEmpty())
            previousButton.setEnabled(false);
        else
            previousButton.setEnabled(true);

        if(parent.getCurrentDirectoryElement().isClass())
            nextButton.setEnabled(false);
        else
            nextButton.setEnabled(true);

        for (DirectoryElement f :parent.getCurrentDirectoryElement().getChildElements()){
            sourceButton.setEnabled(false);
            if(f.isClass()){
                sourceButton.setEnabled(true);
                break;
            }
        }

    }

    private void setButtons() {
        rootButton = createButton("Go To Root DirectoryElement",
                IconLoader.getIcon("/icons/root.png"),
                e -> returnToRootFolder());

        previousButton = createButton("Previous DirectoryElement",
                IconLoader.getIcon("/icons/back.png"),
                e -> previousFolder());
        previousButton.setEnabled(false);

        nextButton = createButton("Enter Selected DirectoryElement",
                IconLoader.getIcon("/icons/into.png"),
                e -> nextFolder());

        sourceButton = createButton("Open Code In Project",
                IconLoader.getIcon("/icons/source.png"),
                e -> sourceButtonPressed());
        sourceButton.setEnabled(false);
    }

    private JButton createButton(String popupText, Icon icon, ActionListener action) {
        JButton button = new JButton();
        button.setIcon(icon);
        button.setSize(30, 30);
        button.setToolTipText(popupText);
        button.addActionListener(action);
        return button;
    }

    private void returnToRootFolder(){
        parent.getTable().setTableModel(parent.getRootDirectoryElement());
        parent.setCurrentDirectoryElement(parent.getRootDirectoryElement());
        parent.getPreviousDirectoryElement().clear();
        setIfButtonIsEnabled();
    }

    private void previousFolder(){
        if (!parent.getPreviousDirectoryElement().empty()){
            parent.setCurrentDirectoryElement(parent.getPreviousDirectoryElement().pop());
            parent.getTable().setTableModel(parent.getCurrentDirectoryElement());
        }
        setIfButtonIsEnabled();
    }

    private void nextFolder(){
        if (!parent.getCurrentDirectoryElement().isClass())
            parent.createModel(parent.getTable().getRow());
        setIfButtonIsEnabled();
    }

    private void sourceButtonPressed(){
        String headerID = Table.getClassTableHeader()[0];
        String currentHeader = parent.getTable().getModel().getColumnName(0);

        if (currentHeader.equals(headerID)){
            // We are inside the class.
            if (parent.getCurrentDirectoryElement().isClass()){
                VirtualFile virtualFile  = parent.getCurrentDirectoryElement().getVirtualFile();
                if (virtualFile.exists())
                    new OpenFileDescriptor(parent.getProject(), virtualFile).navigate(true);
                else {
                    StatusBar statusBar = WindowManager.getInstance().getStatusBar(parent.getProject());
                    JBPopupFactory.getInstance()
                            .createHtmlTextBalloonBuilder("Error Finding File: File may have been deleted.", MessageType.ERROR, null)
                            .setFadeoutTime(3500)
                            .createBalloon()
                            .show(RelativePoint.getSouthEastOf(statusBar.getComponent()), Balloon.Position.above);
                }
            }
        }else{
            int index = parent.getTable().getRow();
            String className = (String) parent.getTable().getModel().getValueAt(index, 0);
            DirectoryElement file =  parent.getCurrentDirectoryElement().getChildElements().stream().filter(x->x.getName().equals(className)).collect(Collectors.toList()).get(0);

            if (file.isClass()){
                VirtualFile virtualFile  = file.getVirtualFile();
                if (virtualFile.exists())
                    new OpenFileDescriptor(parent.getProject(), virtualFile).navigate(true);
                else {
                    StatusBar statusBar = WindowManager.getInstance().getStatusBar(parent.getProject());
                    JBPopupFactory.getInstance()
                            .createHtmlTextBalloonBuilder("Error Finding File: File may have been deleted.", MessageType.ERROR, null)
                            .setFadeoutTime(3500)
                            .createBalloon()
                            .show(RelativePoint.getSouthEastOf(statusBar.getComponent()), Balloon.Position.above);
                }
            }
        }
        setIfButtonIsEnabled();
    }
}
