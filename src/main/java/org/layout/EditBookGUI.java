package org.layout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

import static org.layout.APIHandleUtils.MangaDexApiHandling.mangaArray;

public class EditBookGUI extends JDialog {
    private JPanel contentPane;
    private JScrollPane mainLayout;
    private JPanel layout;
    private JPanel editTitleLayout;
    private JTextField editTitleField;
    private JPanel editAuthorLayout;
    private JTextField editAuthorField;
    private JPanel editGenreLayout;
    private JComboBox<String> editGenreCombobox;
    private JPanel editStatusLayout;
    private JComboBox<String> editStatusCombobox;
    private JPanel editYearLayout;
    private JComboBox<String> editYearCombobox;
    private JPanel editDescriptionLayout;
    private JTextArea editDescriptionField;
    private JPanel editChapterLayout;
    private JTextField editChapterField;
    private JPanel buttonLayout;
    private JButton saveButton;
    private JButton cancelButton;
    private JLabel warningTitleField;
    private JLabel warningAuthorField;
    private JLabel warningGenreField;
    private JLabel warningStatusField;
    private JLabel warningYearField;
    private JLabel warningDescriptionField;
    private JLabel warningChapterField;

    public EditBookGUI(int index, String title, String author, String genre, String status, String year, String description, String chapter) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(cancelButton);

        setTitle("Edit book: " + title);
        setSize(new Dimension(800, 500));

        String[] genreItem = {"manga", "science", "adventure", "slice of life"};
        String[] statusItem = {"ongoing", "completed", "hiatus", "cancelled"};
        String[] yearItem = new String[23];

        for (int i = 0; i < yearItem.length; i++) {
            yearItem[i] = 2000 + yearItem.length - i - 1 + "";
        }

        editTitleField.setText(title);
        editAuthorField.setText(author);

        DefaultComboBoxModel<String> genreComboboxModel = new DefaultComboBoxModel<>(genreItem);
        int getCurrentGenreIndex = 0;

        for (int i = 0; i < genreItem.length; i++) {
            if (genreItem[i].equals(genre)) {
                getCurrentGenreIndex = i;
                break;
            }
        }

        editGenreCombobox.setModel(genreComboboxModel);
        editGenreCombobox.setSelectedIndex(getCurrentGenreIndex);

        DefaultComboBoxModel<String> statusComboboxModel = new DefaultComboBoxModel<>(statusItem);
        int getCurrentStatusIndex = 0;

        for (int i = 0; i < statusItem.length; i++) {
            if (statusItem[i].equals(status)) {
                getCurrentStatusIndex = i;
                break;
            }
        }

        editStatusCombobox.setModel(statusComboboxModel);
        editStatusCombobox.setSelectedIndex(getCurrentStatusIndex);

        DefaultComboBoxModel<String> yearComboboxModel = new DefaultComboBoxModel<>(yearItem);
        int getCurrentYearIndex = 0;

        for (int i = 0; i < yearItem.length; i++) {
            if (yearItem[i].equals(year)) {
                getCurrentYearIndex = i;
                break;
            }
        }

        editYearCombobox.setModel(yearComboboxModel);
        editYearCombobox.setSelectedIndex(getCurrentYearIndex);

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
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        saveButton.addActionListener(e -> {
            warningTitleField.setVisible(false);
            warningAuthorField.setVisible(false);
            warningChapterField.setVisible(false);

            if (editTitleField.getText().equals("")) {
                warningTitleField.setVisible(true);
                warningTitleField.setText("* Title field must not be empty");
                return;
            } else if (editTitleField.getText().length() > 256) {
                warningTitleField.setVisible(true);
                warningTitleField.setText("* Title max characters is 256");
                return;
            }

            if (editAuthorField.getText().equals("")) {
                warningAuthorField.setVisible(true);
                warningAuthorField.setText("* Author field must not be empty");
                return;
            } else if (editAuthorField.getText().length() > 256) {
                warningAuthorField.setVisible(true);
                warningAuthorField.setText("* Author max characters is 256");
                return;
            }

            int newChapter;

            if (editChapterField.getText().equals("")) {
                warningChapterField.setVisible(true);
                warningChapterField.setText("* Chapter field must not be empty");
                return;
            } else {
                try {
                    newChapter = Integer.parseInt(editChapterField.getText());
                } catch (NumberFormatException ignored) {
                    warningChapterField.setVisible(true);
                    warningChapterField.setText("* Chapter field must be a number");
                    editChapterField.setText("");
                    return;
                }
            }

            mangaArray.get(index).setTitle(editTitleField.getText());
            mangaArray.get(index).setAuthor(editAuthorField.getText());
            mangaArray.get(index).setGenre(Objects.requireNonNull(editGenreCombobox.getSelectedItem()).toString());
            mangaArray.get(index).setStatus(Objects.requireNonNull(editStatusCombobox.getSelectedItem()).toString());
            mangaArray.get(index).setYearRelease(Objects.requireNonNull(editYearCombobox.getSelectedItem()).toString());
            mangaArray.get(index).setDescription(editDescriptionField.getText().trim().replaceAll("\n", " "));
            mangaArray.get(index).setChapters(newChapter);

            JOptionPane.showMessageDialog(mainLayout,
                    "Edited Successfully", "Notify message",
                    JOptionPane.INFORMATION_MESSAGE);

            onCancel();
        });

        cancelButton.addActionListener(e -> onCancel());
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
