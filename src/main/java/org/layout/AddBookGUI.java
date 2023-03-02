package org.layout;

import org.layout.APIHandleUtils.Manga;

import javax.swing.*;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import static org.layout.APIHandleUtils.MangaDexApiHandling.mangaArray;

public class AddBookGUI {
    private JPanel mainLayout;
    private JPanel layout;
    private JPanel titleLayout;
    private JTextField titleInput;
    private JPanel authorLayout;
    private JTextField authorInput;
    private JPanel genreLayout;
    private JComboBox authorCombobox;
    private JPanel statusLayout;
    private JComboBox statusCombobox;
    private JPanel yearLayout;
    private JComboBox yearCombobox;
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

        saveButton.addActionListener(e -> {
            if (titleInput.getText().equals("")) {
                warningTitle.setVisible(true);
                warningTitle.setText("* Title field is required");
                return;
            } else if (titleInput.getText().length() > 256) {
                warningTitle.setVisible(true);
                warningTitle.setText("* Title's max characters is 256");
                return;
            }

            for (Manga manga : mangaArray) {
                if (manga.getTitle().contains(titleInput.getText())) {
                    warningTitle.setVisible(true);
                    warningTitle.setText("* Book is already in the database");
                    return;
                }
            }

            if (authorInput.getText().equals("")) {
                warningAuthor.setText("* Author field is required");
            } else if (authorInput.getText().length() > 256) {
                warningAuthor.setText("* Max characters is 256");
            }
        });

        cancelButton.addActionListener(e -> {

        });
    }

    public JPanel getAddBookGUI() {
        return mainLayout;
    }
}
