package com.ANZR.Ergo.plugin;

import com.intellij.ui.JBColor;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Table extends JBTable {

    private ErgoToolWindow parent;
    private boolean wasDoubleClick;
    private int row;
    private static final String[] classTableHeader = {"Anti-Pattern", "Found"};
    private static final String[] folderTableHeader = {"Element", "Anti-Patterns Found"};
    DirectoryElement directoryElement;

    Table(ErgoToolWindow parent) {
        super();
        this.parent = parent;
        setFillsViewportHeight(true);
        setAutoCreateRowSorter(true);
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                click(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });


    }

    public void setTableModel(DirectoryElement directoryElement) {
        this.directoryElement = directoryElement;
        if (directoryElement.isClass())
            setClassModel(directoryElement);
        else
            setFolderModel(directoryElement);
    }

    private void setFolderModel(DirectoryElement directoryElement) {
        ArrayList<DirectoryElement> directoryElements = directoryElement.getChildElements();
        Object[][] temp = new Object[directoryElements.size()][folderTableHeader.length];
        for (int i = 0; i < directoryElements.size(); i++) {
            if (directoryElements.get(i).getName() != null)
                temp[i][0] = directoryElements.get(i).getName();
            else temp[i][0] = "null";
            temp[i][1] = directoryElements.get(i).getAntiPatterns().size() + DirectoryElement.getAntiPatternCount(directoryElements.get(i).getChildElements());
        }
        setModel(new DefaultTableModel(temp, folderTableHeader));
    }



    private void setClassModel(DirectoryElement directoryElement) {
        ArrayList<AntiPattern> antiPatterns = directoryElement.getAntiPatterns();
        Object[][] temp = new Object[antiPatterns.size()][classTableHeader.length];
        for (int i = 0; i < antiPatterns.size(); i++) {
            if (antiPatterns.get(i).getName() != null)
                temp[i][0] = antiPatterns.get(i).getName();
            else temp[i][0] = "null";
            temp[i][1] = antiPatterns.get(i).getMessage();
        }
        setModel(new DefaultTableModel(temp, classTableHeader));
    }

    @Override
    public boolean isCellEditable(int rows, int columns) {
        row = rows;
        return false;
    }

    private void click(MouseEvent e) {
        if (e.getClickCount() == 2) {
            wasDoubleClick = true;
        } else {
            Integer timerInterval = (Integer) Toolkit.getDefaultToolkit().getDesktopProperty(
                    "awt.multiClickInterval");
            Timer timer = new Timer(timerInterval, evt -> {
                if (isRowSelected(row) && wasDoubleClick) {
                    parent.createModel(row);
                    wasDoubleClick = false;
                }
            });
            timer.setRepeats(false);

            timer.start();
        }
    }

    public Component prepareRenderer(TableCellRenderer cellRenderer, int rows, int columns) {
        Component component = super.prepareRenderer(cellRenderer, rows, columns);

        if (directoryElement == null)
            return component;

        if (directoryElement.isDirectory()) {
            setColorsForFolder(component, rows);
        } else if (directoryElement.isClass()) {
            setColorsForClass(component, rows);
        }

        return component;
    }

    private void setColorsForClass(Component component, int rows) {


        if (!getValueAt(rows, 1).equals(0)) {

            ArrayList<AntiPattern> antiPatterns = directoryElement.getAntiPatterns();
            for (int i = 0; i < antiPatterns.size(); i++) {
                if (antiPatterns.get(i).getName().equals(getValueAt(rows, 0))) {
                    if (antiPatterns.get(i).getPriority() == AntiPattern.Priority.HIGH) {
                        component.setBackground(new JBColor(JBColor.RED, JBColor.RED));
                        component.setForeground(new JBColor(JBColor.BLACK, JBColor.BLACK));
                        return;
                    } else {
                        component.setBackground(new JBColor(JBColor.YELLOW, JBColor.YELLOW));
                        component.setForeground(new JBColor(JBColor.BLACK, JBColor.BLACK));
                        return;
                    }
                }

            }

        } else {
            component.setBackground(new JBColor(JBColor.DARK_GRAY, JBColor.WHITE));
            component.setForeground(new JBColor(JBColor.LIGHT_GRAY, JBColor.BLACK));
        }
    }

    private void setColorsForFolder(Component component, int rows) {

        if (!getValueAt(rows, 1).equals(0)) {

            if(checkFolderForPriority(directoryElement.getChildElements().get(rows))){
                component.setBackground(new JBColor(JBColor.RED, JBColor.RED));
                component.setForeground(new JBColor(JBColor.BLACK, JBColor.BLACK));
            } else {
                component.setBackground(new JBColor(JBColor.YELLOW, JBColor.YELLOW));
                component.setForeground(new JBColor(JBColor.BLACK, JBColor.BLACK));
            }

        } else {
            component.setBackground(new JBColor(JBColor.DARK_GRAY, JBColor.WHITE));
            component.setForeground(new JBColor(JBColor.LIGHT_GRAY, JBColor.BLACK));
        }

    }


    private boolean checkFolderForPriority(DirectoryElement directoryElement) {

        if(directoryElement.isClass()){
            for (AntiPattern a : directoryElement.getAntiPatterns()) {
                if (a.getPriority() == AntiPattern.Priority.HIGH) {
                    return true;
                }
            }
            return false;
        }

        for (DirectoryElement child : directoryElement.getChildElements()) {

            for (AntiPattern a : child.getAntiPatterns()) {
                if (a.getPriority() == AntiPattern.Priority.HIGH) {
                    return true;
                }
            }

            if (checkFolderForPriority(child))
                return true;
        }

        return false;
    }


    public int getRow() {
        return row;
    }

    public static String[] getClassTableHeader() {
        return classTableHeader;
    }
}
