package org.layout.Add;

import org.layout.APIHandleUtils.Manga;
import org.layout.db.SQLConnectionString;

import javax.swing.*;
import java.sql.*;
import java.util.Objects;
import java.util.UUID;

import static org.layout.APIHandleUtils.MangaDexApiHandling.mangaArray;

public class AddBookGUI {
    private JPanel mainLayout;
    private JPanel layout;
    private JPanel titleLayout;
    private JTextField titleInput;
    private JPanel authorLayout;
    private JTextField authorInput;
    private JPanel genreLayout;
    private JComboBox<String> genreCombobox;
    private JPanel statusLayout;
    private JComboBox<String> statusCombobox;
    private JPanel yearLayout;
    private JComboBox<String> yearCombobox;
    private JPanel descriptionLayout;
    private JTextArea descriptionTextArea;
    private JPanel chapterLayout;
    private JTextField chapterInput;
    private JPanel functionLayout;
    private JButton saveButton;
    private JButton cancelButton;
    private JLabel warningTitle;
    private JLabel warningAuthor;
    private JLabel warningGenre;
    private JLabel warningStatus;
    private JLabel warningYear;
    private JLabel warningDescription;
    private JLabel warningChapter;

    public AddBookGUI() {
        warningTitle.setVisible(false);
        warningAuthor.setVisible(false);
        warningGenre.setVisible(false);
        warningStatus.setVisible(false);
        warningYear.setVisible(false);
        warningDescription.setVisible(false);
        warningChapter.setVisible(false);

        String[] genre = {"manga", "science", "adventure", "slice of life"};
        String[] status = {"ongoing", "completed", "hiatus", "cancelled"};
        String[] year = new String[23];

        for (int i = 0; i < year.length; i++) {
            year[i] = 2000 + year.length - i - 1 + "";
        }

        DefaultComboBoxModel<String> genreComboboxModel = new DefaultComboBoxModel<>(genre);
        genreCombobox.setModel(genreComboboxModel);

        DefaultComboBoxModel<String> statusComboboxModel = new DefaultComboBoxModel<>(status);
        statusCombobox.setModel(statusComboboxModel);

        DefaultComboBoxModel<String> yearComboboxModel = new DefaultComboBoxModel<>(year);
        yearCombobox.setModel(yearComboboxModel);

        saveButton.addActionListener(e -> {
            warningTitle.setVisible(false);
            warningAuthor.setVisible(false);
            warningGenre.setVisible(false);
            warningStatus.setVisible(false);
            warningYear.setVisible(false);
            warningDescription.setVisible(false);
            warningChapter.setVisible(false);

            if (titleInput.getText().equals("")) {
                warningTitle.setVisible(true);
                warningTitle.setText("* Title field is required");
                return;
            } else if (titleInput.getText().length() > 256) {
                warningTitle.setVisible(true);
                warningTitle.setText("* Title max characters is 256");
                return;
            }

            for (Manga manga : mangaArray) {
                if (manga.getTitle().equals(titleInput.getText())) {
                    warningTitle.setVisible(true);
                    warningTitle.setText("* Book is already in the database");
                    return;
                }
            }

            if (authorInput.getText().equals("")) {
                warningAuthor.setVisible(true);
                warningAuthor.setText("* Author field is required");
                return;
            } else if (authorInput.getText().length() > 256) {
                warningAuthor.setVisible(true);
                warningAuthor.setText("* Max characters is 256");
                return;
            }

            int chapter;

            if (chapterInput.getText().equals("")) {
                warningChapter.setVisible(true);
                warningChapter.setText("* Chapter field is required");
                return;
            } else {
                try {
                    chapter = Integer.parseInt(chapterInput.getText());
                } catch (NumberFormatException ignored) {
                    warningChapter.setVisible(true);
                    warningChapter.setText("* Chapter field must be a number");
                    chapterInput.setText("");
                    return;
                }
            }

            mangaArray.add(new Manga(UUID.randomUUID().toString(), titleInput.getText(), authorInput.getText(),
                    Objects.requireNonNull(genreCombobox.getSelectedItem()).toString(),
                    Objects.requireNonNull(statusCombobox.getSelectedItem()).toString(),
                    Objects.requireNonNull(yearCombobox.getSelectedItem()).toString(),
                    descriptionTextArea.getText(), "Not available", chapter));

            try {
                PreparedStatement authorQuery = SQLConnectionString.getConnection().prepareStatement("Select * from Author where " + "Author.Name = ?");
                authorQuery.setString(1, authorInput.getText());
                ResultSet authorRes = authorQuery.executeQuery();
                String authorUUID = UUID.randomUUID().toString();

                if (authorRes.next()) {
                    authorUUID = authorRes.getString(1);
                } else {
                    PreparedStatement insertAuthorQuery = SQLConnectionString.getConnection().prepareStatement("Insert into Author (ID, Name, State) values (?, ?, '0')");
                    insertAuthorQuery.setString(1, authorUUID);
                    insertAuthorQuery.setString(2, authorInput.getText());
                    insertAuthorQuery.execute();
                }

                PreparedStatement insertBookQuery = SQLConnectionString.getConnection().prepareStatement(
                        "Insert into Book (ID, Title, Author, Type, Status, YearReleased, Description, Cover, Chapter, State) values " +
                                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                );

                insertBookQuery.setString(1, UUID.randomUUID().toString());
                insertBookQuery.setString(2, titleInput.getText());
                insertBookQuery.setString(3, authorUUID);
                insertBookQuery.setString(4, "1");
                insertBookQuery.setString(5, statusCombobox.getSelectedItem().toString());
                insertBookQuery.setString(6, yearCombobox.getSelectedItem().toString());
                insertBookQuery.setString(7, descriptionTextArea.getText());
                insertBookQuery.setString(8, "Not available");
                insertBookQuery.setString(9, chapter + "");
                insertBookQuery.setString(10, "0");
                insertBookQuery.executeUpdate();
            } catch (Exception ignored) {
            }

            titleInput.setText("");
            authorInput.setText("");
            genreCombobox.setSelectedIndex(0);
            statusCombobox.setSelectedIndex(0);
            yearCombobox.setSelectedIndex(0);
            descriptionTextArea.setText("");
            chapterInput.setText("");

            JOptionPane.showMessageDialog(mainLayout,
                    "Added Successfully", "Notify message",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        cancelButton.addActionListener(e -> {
            titleInput.setText("");
            authorInput.setText("");
            genreCombobox.setSelectedIndex(0);
            statusCombobox.setSelectedIndex(0);
            yearCombobox.setSelectedIndex(0);
            descriptionTextArea.setText("");
            chapterInput.setText("");
        });
    }

    public JPanel getAddBookGUI() {
        return mainLayout;
    }
}
