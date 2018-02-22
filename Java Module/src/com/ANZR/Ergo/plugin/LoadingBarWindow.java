package com.ANZR.Ergo.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.ActionCallback;
import com.intellij.openapi.wm.*;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;

public class LoadingBarWindow {

    private JLabel label = new JLabel();
    private JProgressBar loadingBar = new JProgressBar();
    private JFrame frame;
//    private Thread updateThread = new Thread(() -> {
//        updateLoadingBar()
//    });



    public LoadingBarWindow(){

        int width = 600;

        frame = new JFrame("Ergo is running...");
        JPanel panel = new JPanel(new GridBagLayout());
        Dimension dimension = new Dimension(width,125);

        label.setText("SOME TEXT");
        loadingBar.setMaximum(100);
        loadingBar.setMinimum(0);
        Dimension dim = new Dimension(width - 50, 4);
        loadingBar.setSize(dim);
        loadingBar.setPreferredSize(dim);

        panel.setSize(dimension);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.gridx = 0;

        GridBagConstraints l = new GridBagConstraints();
        l.fill = GridBagConstraints.HORIZONTAL;
        l.gridwidth = 3;
        panel.add(loadingBar, l);
        panel.add(label, constraints);

        frame.setLayout(new BorderLayout());
        frame.setContentPane(panel);
        frame.setSize(dimension);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);


    }

    public void setText(String text){
        label.setText(text);
    }

    public void setProgress(int value){
        loadingBar.setValue(value);
    }

    public void updateLoadingBar(String text, int progress){
        setText(text);
        setProgress(progress);
    }


    public void closeFrame(){
        frame.setVisible(false);
        frame.dispose();
    }
}
