package org.layout;

import org.layout.APIHandleUtils.Manga;
import org.layout.APIHandleUtils.MangaDexApiHandling;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
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
     *  - Delete book (CHECKED)
     *  - Add Try Catch (almost)
     *  - Advance Search if the book is not found in database, it will search on manga-dex
     *  - Next to search bar add a search option for title or author or type
     *  - Fix edit warning messages and change status, genre, year field to combo box. (CHECKED)
     *  -
     *  -
     *  -
     *  - You need more sleep... Brother....
     * */

    public MainLayoutGUI() {
        ArrayList<Manga> a = readData();

        for (int i = 0; i < a.size(); i++) {
            boolean c = true;

            for (int j = 0; j < mangaArray.size(); j++) {
                if (mangaArray.get(j).getUuid().equals(a.get(i).getUuid())) {
                    c = false;
                    break;
                }
            }

            if (c) {
                mangaArray.add(a.get(i));
            }
        }

        reWriteEntireData(mangaArray);

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

            reWriteEntireData(mangaArray);

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

            reWriteEntireData(mangaArray);

            contentTable = new ContentScroll(mangaArray);
            transition.display(contentTable.getScrollPane());
        });

        quitButton.addActionListener(e -> System.exit(0));
    }

    public void reWriteEntireData(ArrayList<Manga> array) {
        try (FileWriter fstream = new FileWriter("book-data.txt")) {
            BufferedWriter data = new BufferedWriter(fstream);

            for (int i = 0; i < array.size(); i++) {
                data.write(array.get(i).getUuid() + ";");
                data.write(array.get(i).getTitle() + ";");
                data.write(array.get(i).getAuthor() + ";");
                data.write(array.get(i).getGenre() + ";");
                data.write(array.get(i).getStatus() + ";");
                data.write(array.get(i).getYearRelease() + ";");
                data.write(array.get(i).getDescription() + ";");
                data.write(array.get(i).getCover() + ";");
                data.write(array.get(i).getChapters() + System.lineSeparator());
            }

            data.close();
        } catch (IOException ignored) {
        }
    }

    public ArrayList<Manga> readData() {
        try (BufferedReader br = new BufferedReader(new FileReader("book-data.txt"))) {
            ArrayList<Manga> arr = new ArrayList<>();
            String line = br.readLine();

            while (line != null) {
                String[] sb = line.split(";");
                arr.add(new Manga(sb[0], sb[1], sb[2], sb[3], sb[4], sb[5], sb[6], sb[7], Integer.parseInt(sb[8])));

                line = br.readLine();
            }

            return arr;
        } catch (IOException ignored) {
        }

        return new ArrayList<>();
    }

    public static void main(String[] args) {
        MangaDexApiHandling mangaDexApiHandling = new MangaDexApiHandling(60);
        JFrame frame = new JFrame("Library Management");
//        frame.setResizable(false);
        frame.setContentPane(new MainLayoutGUI().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
