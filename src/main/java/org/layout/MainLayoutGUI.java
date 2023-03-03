package org.layout;

import org.layout.APIHandleUtils.Manga;
import org.layout.APIHandleUtils.MangaDexApiHandling;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static org.layout.APIHandleUtils.MangaDexApiHandling.mangaArray;

public class MainLayoutGUI {
    private JPanel MainPanel;
    private JPanel headLayout;
    private JTextField searchField;
    private JPanel mainLayout;
    private JPanel navLayout;
    private JPanel innerNavLayout;
    private JButton homeButton;
    private JButton addButton;
    private JButton quitButton;
    private JPanel contentLayout;
    private JButton addDialogButton;
    private JButton editButton;
    private JButton deleteButton;
    private ContentScroll contentTable;

    /*
     * Gridbaglayout Properties:
     * - [gridX, gridY] = [row, column]:
     *   - The gridX means that which position are the elements are placed in the gird layout
     *   - The gridY means that which column are the elements are placed in the gird layout
     *
     * */

    /*
     * TODO:
     *  - Row select edit (CHECKED)
     *  - Delete book
     *  - Add Try Catch
     *  - Advance Search if the book is not found in database, it will search on manga-dex
     *  - Next to search bar add a search option for title or author or type
     *  - Fix edit warning messages and change status, genre, year field to combo box. (CHECKED)
     *  -
     *  -
     *  -
     *  - You need more sleep... Brother....
     * */

    public MainLayoutGUI() {
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;

        Transition transition = new Transition();
        contentTable = new ContentScroll(mangaArray);
        transition.display(contentTable.getScrollPane());

        contentLayout.add(transition, constraints);

        homeButton.addActionListener(e -> {
            contentTable = new ContentScroll(mangaArray);
            transition.display(contentTable.getScrollPane());
        });

        addButton.addActionListener(e -> {
            AddBookGUI addBook = new AddBookGUI();
            transition.display(addBook.getAddBookGUI());
        });

        quitButton.addActionListener(e -> System.exit(0));

        addDialogButton.addActionListener(e -> {
            AddBookDialog dialog = new AddBookDialog();
            dialog.setLocationRelativeTo(MainPanel);
            dialog.setVisible(true);

            contentTable = new ContentScroll(mangaArray);
            transition.display(contentTable.getScrollPane());
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                renderTableOnSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                renderTableOnSearch();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                renderTableOnSearch();
            }

            public void renderTableOnSearch() {
                if (searchField.getText().equals("")) {
                    contentTable = new ContentScroll(mangaArray);
                    transition.display(contentTable.getScrollPane());
                } else {
                    ArrayList<Manga> newMangaArray = new ArrayList<>();

                    for (Manga manga : mangaArray) {
                        if (manga.getTitle().toLowerCase().contains(searchField.getText().toLowerCase())) {
                            newMangaArray.add(manga);
                        }
                    }

                    contentTable = new ContentScroll(newMangaArray);
                    transition.display(contentTable.getScrollPane());
                }
            }
        });

        editButton.addActionListener(e -> {
            int index = contentTable.getTable().getSelectedRow();

            String title = contentTable.getTable().getValueAt(index, 0).toString();
            String author = contentTable.getTable().getValueAt(index, 1).toString();
            String genre = contentTable.getTable().getValueAt(index, 2).toString();
            String status = contentTable.getTable().getValueAt(index, 3).toString();
            String yearRelease = contentTable.getTable().getValueAt(index, 4).toString();
            String description = mangaArray.get(index).getDescription();
            String chapter = contentTable.getTable().getValueAt(index, 5).toString();

            EditBookGUI editBookGUI = new EditBookGUI(index, title, author, genre, status, yearRelease, description, chapter);
            editBookGUI.setLocationRelativeTo(MainPanel);
            editBookGUI.setVisible(true);

            contentTable = new ContentScroll(mangaArray);
            transition.display(contentTable.getScrollPane());
        });

        deleteButton.addActionListener(e -> {
            int index = contentTable.getTable().getSelectedRow();

            String title = contentTable.getTable().getValueAt(index, 0).toString();
            String author = contentTable.getTable().getValueAt(index, 1).toString();
            String genre = contentTable.getTable().getValueAt(index, 2).toString();
            String status = contentTable.getTable().getValueAt(index, 3).toString();
            String yearRelease = contentTable.getTable().getValueAt(index, 4).toString();
            String description = mangaArray.get(index).getDescription();
            String chapter = contentTable.getTable().getValueAt(index, 5).toString();

            DeleteBookDialog deleteBookDialog = new DeleteBookDialog(index, title, author, genre, status, yearRelease, description, chapter);
            deleteBookDialog.setLocationRelativeTo(MainPanel);
            deleteBookDialog.setVisible(true);

            contentTable = new ContentScroll(mangaArray);
            transition.display(contentTable.getScrollPane());
        });
    }

    public static void main(String[] args) {
        MangaDexApiHandling mangaDexApiHandling = new MangaDexApiHandling(20);
        JFrame frame = new JFrame("Library Management");
//        frame.setResizable(false);
        frame.setContentPane(new MainLayoutGUI().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
