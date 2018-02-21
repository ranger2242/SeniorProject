package com.ANZR.Ergo.plugin;

import a.j.B;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;

import javax.swing.*;
import java.awt.*;

public class LoadingBarWindow {

    private JLabel label = new JLabel();
    private JProgressBar loadingBar = new JProgressBar();

    public LoadingBarWindow(Project project){

        JPanel panel = new JPanel(new GridBagLayout());
        Dimension dimension = new Dimension(175,35);

        label.setText("SOME TEXT");
        loadingBar.setMaximum(100);
        loadingBar.setMinimum(0);

        panel.setSize(dimension);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.gridx = 0;
        panel.add(loadingBar, constraints);
        panel.add(label, constraints);


        ComponentPopupBuilder builder = JBPopupFactory.getInstance().createComponentPopupBuilder(panel, null);
        builder.setMinSize(dimension);

        JBPopup popup = builder.createPopup();
        popup.showCenteredInCurrentWindow(project);

    }

    public void setText(String text){
        label.setText(text);
    }

    public void setProgress(int value){
        loadingBar.setValue(value);
    }


}
