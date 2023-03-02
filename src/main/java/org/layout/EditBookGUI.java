package org.layout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EditBookGUI extends JDialog {
    private JPanel contentPane;
    private JScrollPane mainLayout;
    private JPanel layout;
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
    private JTextArea editDescriptionField;
    private JPanel editChapterLayout;
    private JTextField editChapterField;
    private JPanel buttonLayout;
    private JButton saveButton;
    private JButton cancelButton;

    public EditBookGUI(String title, String author, String genre, String status, String year, String description, String chapter) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(cancelButton);

        setTitle("Edit book: " + title);
        setSize(new Dimension(800, 500));

        editTitleField.setText(title);
        editAuthorField.setText(author);
        editGenreField.setText(genre);
        editStatusField.setText(status);
        editYearField.setText(year);
        editDescriptionField.setText(description);
        editChapterField.setText(chapter);

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

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

//    public static void main(String[] args) {
//        EditBookGUI dialog = new EditBookGUI();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }
}
