package org.layout;

import org.layout.db.SQLConnectionString;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import static org.layout.APIHandleUtils.MangaDexApiHandling.mangaArray;

public class DeleteBookDialog extends JDialog {
    private JPanel contentPane;
    private JScrollPane scrollViewDetail;
    private JPanel bookDetailsLayout;
    private JPanel titleLayout;
    private JLabel bookTitleDetail;
    private JPanel authorLayout;
    private JLabel bookAuthorDetail;
    private JPanel genreLayout;
    private JLabel bookGenreDetail;
    private JPanel statusLayout;
    private JLabel bookStatusDetail;
    private JPanel yearLayout;
    private JLabel bookYearDetail;
    private JPanel descriptionLayout;
    private JTextArea bookDescriptionDetail;
    private JPanel chapterLayout;
    private JLabel bookChapterDetail;
    private JButton deleteButton;
    private JButton cancelButton;

    public DeleteBookDialog(int index, String title, String author, String genre, String status, String year, String description, String chapter) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(cancelButton);
        setTitle("Delete Book: " + title);
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
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        deleteButton.addActionListener(e -> {
            String[] options = {"Yes", "No"};

            int o = JOptionPane.showOptionDialog(contentPane,
                    "Delete confirmation", "Notify message",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);

            if (o == JOptionPane.YES_OPTION) {
                try (Connection connection = DriverManager.getConnection(SQLConnectionString.getConnectionString())) {
                    PreparedStatement authorQuery = connection.prepareStatement("update book set book.State = 1 where ID = ?");
                    authorQuery.setString(1, mangaArray.get(index).getUuid());
                    authorQuery.executeUpdate();
                }
                catch (SQLException error) {
                    error.printStackTrace();
                }

                mangaArray.remove(index);
            }

            onCancel();
        });

        cancelButton.addActionListener(e -> onCancel());
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
