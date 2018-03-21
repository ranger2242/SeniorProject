package com.ANZR.Ergo.plugin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


public class LoadingBarWindow {

    private JLabel label = new JLabel();
    private JProgressBar loadingBar = new JProgressBar();
    private JFrame frame;

    private boolean closed = false;


    LoadingBarWindow() {

        int width = 600;
        Dimension frameDimension = new Dimension(width, 125);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setSize(frameDimension);

        frame = new JFrame("Ergo is running...");
        frame.setLayout(new BorderLayout());
        frame.setContentPane(panel);
        frame.setSize(frameDimension);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(false);
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
//                System.out.println("test");
            }

            @Override
            public void windowClosing(WindowEvent e) {
                closed = true;
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

        label.setText("");
        loadingBar.setMaximum(100);
        loadingBar.setMinimum(0);
        Dimension loadingBarDimension = new Dimension(width - 50, 4);
        loadingBar.setSize(loadingBarDimension);
        loadingBar.setPreferredSize(loadingBarDimension);

        GridBagConstraints loadingBarConstraints = new GridBagConstraints();
        loadingBarConstraints.fill = GridBagConstraints.HORIZONTAL;
        loadingBarConstraints.gridwidth = 3;
        panel.add(loadingBar, loadingBarConstraints);

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        labelConstraints.gridx = 0;
        panel.add(label, labelConstraints);


    }

    public void show() {
        frame.setVisible(true);
    }
    public boolean getClosed(){
        return closed;
    }

    public boolean updateLoadingBar(String text, int progress) {
        label.setText(text);
        loadingBar.setValue(progress);
        return closed;
    }

    public void closeFrame() {
        frame.setVisible(false);
        frame.dispose();
    }
}
