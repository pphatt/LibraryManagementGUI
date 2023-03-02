package org.layout;

import javax.swing.*;
import java.awt.event.*;

public class T extends JDialog {
    private JPanel contentPane;
    private JPanel editTitleLayout;
    private JTextField editTitleField;
    private JPanel editAuthorLayout;
    private JTextField editAuthorField;
    private JPanel editGenreLayout;
    private JTextField editGenreField;
    private JPanel editStatusLayout;
    private JTextField editStatusField;
    private JPanel editYearLayout;
    private JTextField editYearField;
    private JPanel editDescriptionLayout;
    private JTextField editDescriptionField;
    private JPanel editChapterLayout;
    private JPanel editChapterField;
    private JPanel buttonLayout;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton buttonOK;
    private JButton buttonCancel;

    public T() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

//    public static void main(String[] args) {
//        T dialog = new T();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }
}
