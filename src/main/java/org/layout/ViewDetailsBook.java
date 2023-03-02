package org.layout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ViewDetailsBook extends JDialog {
    private JPanel contentPane;
    private JPanel bookImageLayout;
    private JLabel bookTitleDetail;
    private JPanel titleLayout;
    private JLabel bookAuthorDetail;
    private JPanel authorLayout;
    private JLabel bookGenreDetail;
    private JPanel genreLayout;
    private JLabel bookStatusDetail;
    private JPanel statusLayout;
    private JLabel bookYearDetail;
    private JPanel yearLayout;
    private JTextArea bookDescriptionDetail;
    private JPanel descriptionLayout;
    private JLabel bookChapterDetail;
    private JPanel chapterLayout;
    private JScrollPane scrollViewDetail;
    private JPanel bookDetailsLayout;

    public ViewDetailsBook(String title, String author, String genre, String status, String year, String description, String chapter) {
        setContentPane(contentPane);
        setModal(true);
        setTitle("View Book Details: " + title);
        setSize(new Dimension(800, 500));

        bookTitleDetail.setText(title);
        bookAuthorDetail.setText(author);
        bookGenreDetail.setText(genre);
        bookStatusDetail.setText(status);
        bookYearDetail.setText(year);

        bookDescriptionDetail.setText(description);
        bookDescriptionDetail.setEditable(false);

        bookChapterDetail.setText(chapter);

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
//        ViewDetailsBook dialog = new ViewDetailsBook();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }
}
