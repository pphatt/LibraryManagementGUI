package org.layout;

import org.layout.db.SQLConnectionString;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewDetailsBook extends JDialog {
    private JPanel contentPane;
    private JPanel bookImageLayout;
    private JLabel bookTitleDetail;
    private JPanel titleLayout;
    private JLabel bookAuthorDetail;
    private JPanel authorLayout;
    private JLabel bookTypeDetail;
    private JPanel typeLayout;
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
    private JScrollPane genreLayout;
    private JPanel genreInnerLayout;

    public ViewDetailsBook(String uuid, String title, String author, String type, String status, String year, String description, String chapter) {
        setContentPane(contentPane);
        setModal(true);
        setTitle("View Book Details: " + title);
        setSize(new Dimension(800, 500));

        bookTitleDetail.setText(title);
        bookAuthorDetail.setText(author);
        bookTypeDetail.setText(type);
        bookStatusDetail.setText(status);
        bookYearDetail.setText(year);

        bookDescriptionDetail.setText(description);
        bookDescriptionDetail.setEditable(false);

        bookChapterDetail.setText(chapter);

        try {
            PreparedStatement bookGenreQuery = SQLConnectionString.getConnection().prepareStatement("Select * from BookGenre where BookID = ?");
            bookGenreQuery.setString(1, uuid);
            ResultSet bookGenreQueryRes = bookGenreQuery.executeQuery();

            while (bookGenreQueryRes.next()) {
                PreparedStatement genre = SQLConnectionString.getConnection().prepareStatement("Select * from Genre where ID = ?");
                genre.setString(1, bookGenreQueryRes.getString(2));
                ResultSet genreRes = genre.executeQuery();

                int count = 0;

                while (genreRes.next()) {
                    JLabel label = new JLabel(genreRes.getString(2));

                    JPanel panel = new JPanel();
                    panel.setLayout(new GridBagLayout());
                    Border border = BorderFactory.createLineBorder(Color.black);
                    Border margin = new EmptyBorder(5, 5, 5, 5);
                    panel.setBorder(BorderFactory.createCompoundBorder(border, margin));

                    GridBagConstraints constraints = new GridBagConstraints();

                    constraints.gridy = count;
                    constraints.weightx = 1;
                    constraints.weighty = 0;
                    constraints.ipadx = 4;
                    constraints.ipady = 4;
                    constraints.insets = new Insets(0, 0, 0, 10);
                    constraints.fill = GridBagConstraints.BOTH;

                    panel.add(label);
                    genreInnerLayout.add(panel, constraints);
                    count++;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

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
