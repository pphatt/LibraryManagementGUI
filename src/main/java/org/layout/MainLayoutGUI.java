package org.layout;

import org.layout.APIHandleUtils.Manga;
import org.layout.APIHandleUtils.MangaDexApiHandling;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

import static org.layout.APIHandleUtils.MangaDexApiHandling.*;

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
    private JComboBox<String> optionSearchCombobox;
    private JPanel filterPanel;
    private JPanel filterGenrePanel;
    private JComboBox<String> filterGenreCombobox;
    private JPanel filterStatusPanel;
    private JComboBox<String> filterStatusCombobox;
    private JPanel filterYearPanel;
    private JComboBox<String> filterYearCombobox;
    private JButton clearButton;
    public static ContentScroll contentTable = new ContentScroll(mangaArray);

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
     *  - Next to search bar add a search option for title or author or type (CHECKED)
     *  - Fix edit warning messages and change status, genre, year field to combo box. (CHECKED)
     *  -
     *  -
     *  -
     *  - You need more sleep... Brother....
     * */

    public MainLayoutGUI() {
        Transition transition = new Transition();
        MangaDexApiHandling mangaDexApiHandling = new MangaDexApiHandling(10);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;

        transition.display(contentTable.getScrollPane());
        contentLayout.add(transition, constraints);

        String[] options = {"Title", "Author", "Genre", "Status"};
        DefaultComboBoxModel<String> optionModel = new DefaultComboBoxModel<>(options);
        optionSearchCombobox.setModel(optionModel);

        String[] genreOption = {"No filter", "manga", "science", "adventure", "slice of life"};
        String[] statusOption = {"No filter", "ongoing", "completed", "hiatus", "cancelled"};
        String[] yearOption = new String[24];

        yearOption[0] = "No filter";

        for (int i = 1; i < yearOption.length; i++) {
            yearOption[i] = 2000 + yearOption.length - i + "";
        }

        DefaultComboBoxModel<String> genreComboboxModel = new DefaultComboBoxModel<>(genreOption);
        filterGenreCombobox.setModel(genreComboboxModel);

        DefaultComboBoxModel<String> statusComboboxModel = new DefaultComboBoxModel<>(statusOption);
        filterStatusCombobox.setModel(statusComboboxModel);

        DefaultComboBoxModel<String> yearComboboxModel = new DefaultComboBoxModel<>(yearOption);
        filterYearCombobox.setModel(yearComboboxModel);

        homeButton.addActionListener(e -> {
            SearchAndFilterFunc(transition);
        });

        addButton.addActionListener(e -> {
            if (!state) {
                JOptionPane.showMessageDialog(mainLayout,
                        "System is fetching data. Please wait until it is fulfill", "Notify message",
                        JOptionPane.INFORMATION_MESSAGE);

                return;
            }

            AddBookGUI addBook = new AddBookGUI();
            transition.display(addBook.getAddBookGUI());
        });

        addDialogButton.addActionListener(e -> {
            if (!state) {
                JOptionPane.showMessageDialog(mainLayout,
                        "System is fetching data. Please wait until it is fulfill", "Notify message",
                        JOptionPane.INFORMATION_MESSAGE);

                return;
            }

            AddBookDialog dialog = new AddBookDialog();
            dialog.setLocationRelativeTo(MainPanel);
            dialog.setVisible(true);

            SearchAndFilterFunc(transition);
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
                SearchAndFilterFunc(transition);
            }
        });

        editButton.addActionListener(e -> {
            if (!state) {
                JOptionPane.showMessageDialog(mainLayout,
                        "System is fetching data. Please wait until it is fulfill", "Notify message",
                        JOptionPane.INFORMATION_MESSAGE);

                return;
            }

            int index = contentTable.getTable().getSelectedRow();

            if (index == -1) {
                JOptionPane.showMessageDialog(mainLayout,
                        "Invalid Book. You have to select a book to edit", "Notify message",
                        JOptionPane.INFORMATION_MESSAGE);

                return;
            }

            String uuid = contentTable.getTable().getModel().getValueAt(index, 0).toString();
            String title = contentTable.getTable().getValueAt(index, 0).toString();
            String author = contentTable.getTable().getValueAt(index, 1).toString();
            String genre = contentTable.getTable().getValueAt(index, 2).toString();
            String status = contentTable.getTable().getValueAt(index, 3).toString();
            String yearRelease = contentTable.getTable().getValueAt(index, 4).toString();
            String chapter = contentTable.getTable().getValueAt(index, 5).toString();

            int indx = 0;

            for (int i = 0; i < mangaArray.size(); i++) {
                if (mangaArray.get(i).getUuid().equals(uuid)) {
                    indx = i;
                    break;
                }
            }

            EditBookGUI editBookGUI = new EditBookGUI(indx, title, author, genre, status, yearRelease, mangaArray.get(indx).getDescription(), chapter);
            editBookGUI.setLocationRelativeTo(MainPanel);
            editBookGUI.setVisible(true);

            reWriteEntireData(mangaArray);

            SearchAndFilterFunc(transition);
        });

        deleteButton.addActionListener(e -> {
            if (!state) {
                JOptionPane.showMessageDialog(mainLayout,
                        "System is fetching data. Please wait until it is fulfill", "Notify message",
                        JOptionPane.INFORMATION_MESSAGE);

                return;
            }

            int index = contentTable.getTable().getSelectedRow();

            if (index == -1) {
                JOptionPane.showMessageDialog(mainLayout,
                        "Invalid Book. You have to select a book to delete", "Notify message",
                        JOptionPane.INFORMATION_MESSAGE);

                return;
            }

            String uuid = contentTable.getTable().getModel().getValueAt(index, 0).toString();
            String title = contentTable.getTable().getValueAt(index, 0).toString();
            String author = contentTable.getTable().getValueAt(index, 1).toString();
            String genre = contentTable.getTable().getValueAt(index, 2).toString();
            String status = contentTable.getTable().getValueAt(index, 3).toString();
            String yearRelease = contentTable.getTable().getValueAt(index, 4).toString();
            String chapter = contentTable.getTable().getValueAt(index, 5).toString();

            int indx = 0;

            for (int i = 0; i < mangaArray.size(); i++) {
                if (mangaArray.get(i).getUuid().equals(uuid)) {
                    indx = i;
                    break;
                }
            }

            DeleteBookDialog deleteBookDialog = new DeleteBookDialog(indx, title, author, genre, status, yearRelease, mangaArray.get(indx).getDescription(), chapter);
            deleteBookDialog.setLocationRelativeTo(MainPanel);
            deleteBookDialog.setVisible(true);

            reWriteEntireData(mangaArray);

            SearchAndFilterFunc(transition);
        });

        quitButton.addActionListener(e -> System.exit(0));

        filterGenreCombobox.addActionListener(e -> {
            SearchAndFilterFunc(transition);
        });

        filterStatusCombobox.addActionListener(e -> {
            SearchAndFilterFunc(transition);
        });

        filterYearCombobox.addActionListener(e -> {
            SearchAndFilterFunc(transition);
        });

        clearButton.addActionListener(e -> {
            if (!state) {
                return;
            }

            filterGenreCombobox.setSelectedIndex(0);
            filterStatusCombobox.setSelectedIndex(0);
            filterYearCombobox.setSelectedIndex(0);

            SearchAndFilterFunc(transition);
        });
    }

    public static void reWriteEntireData(ArrayList<Manga> array) {
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

    public static ArrayList<Manga> readData() {
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

        return null;
    }

    private void HandleFilter(ArrayList<Manga> newMangaArray, Manga manga) {
        if (!Objects.equals(filterGenreCombobox.getSelectedItem(), "No filter")) {
            if (!Objects.equals(filterGenreCombobox.getSelectedItem(), manga.getGenre())) {
                return;
            }
        }

        if (!Objects.equals(filterStatusCombobox.getSelectedItem(), "No filter")) {
            if (!Objects.equals(filterStatusCombobox.getSelectedItem(), manga.getStatus())) {
                return;
            }
        }

        if (!Objects.equals(filterYearCombobox.getSelectedItem(), "No filter")) {
            if (!Objects.equals(filterYearCombobox.getSelectedItem(), manga.getYearRelease())) {
                return;
            }
        }

        newMangaArray.add(manga);
    }

    public void SearchAndFilterFunc(Transition transition) {
        if (!state) {
            JOptionPane.showMessageDialog(mainLayout,
                    "System is fetching data. Please wait until it is fulfill", "Notify message",
                    JOptionPane.INFORMATION_MESSAGE);

            return;
        }

        ArrayList<Manga> newMangaArray;

        String option = (String) optionSearchCombobox.getSelectedItem();

        assert option != null;
        switch (option) {
            case "Title" -> {
                if (searchField.getText().equals("")) {
                    newMangaArray = new ArrayList<>();

                    for (Manga manga : mangaArray) {
                        HandleFilter(newMangaArray, manga);
                    }

                    contentTable = new ContentScroll(newMangaArray);
                    transition.display(contentTable.getScrollPane());
                } else {
                    newMangaArray = new ArrayList<>();

                    for (Manga manga : mangaArray) {
                        if (manga.getTitle().toLowerCase().contains(searchField.getText().toLowerCase())) {
                            HandleFilter(newMangaArray, manga);
                        }
                    }

                    contentTable = new ContentScroll(newMangaArray);
                    transition.display(contentTable.getScrollPane());
                }
            }
            case "Author" -> {
                if (searchField.getText().equals("")) {
                    newMangaArray = new ArrayList<>();

                    for (Manga manga : mangaArray) {
                        HandleFilter(newMangaArray, manga);
                    }

                    contentTable = new ContentScroll(newMangaArray);
                    transition.display(contentTable.getScrollPane());
                } else {
                    newMangaArray = new ArrayList<>();

                    for (Manga manga : mangaArray) {
                        if (manga.getAuthor().toLowerCase().contains(searchField.getText().toLowerCase())) {
                            HandleFilter(newMangaArray, manga);
                        }
                    }

                    contentTable = new ContentScroll(newMangaArray);
                    transition.display(contentTable.getScrollPane());
                }
            }
            case "Genre" -> {
                if (searchField.getText().equals("")) {
                    newMangaArray = new ArrayList<>();

                    for (Manga manga : mangaArray) {
                        HandleFilter(newMangaArray, manga);
                    }

                    contentTable = new ContentScroll(newMangaArray);
                    transition.display(contentTable.getScrollPane());
                } else {
                    newMangaArray = new ArrayList<>();

                    for (Manga manga : mangaArray) {
                        if (manga.getGenre().toLowerCase().contains(searchField.getText().toLowerCase())) {
                            HandleFilter(newMangaArray, manga);
                        }
                    }

                    contentTable = new ContentScroll(newMangaArray);
                    transition.display(contentTable.getScrollPane());
                }
            }
            default -> {
                if (searchField.getText().equals("")) {
                    newMangaArray = new ArrayList<>();

                    for (Manga manga : mangaArray) {
                        HandleFilter(newMangaArray, manga);
                    }

                    contentTable = new ContentScroll(newMangaArray);
                    transition.display(contentTable.getScrollPane());
                } else {
                    newMangaArray = new ArrayList<>();

                    for (Manga manga : mangaArray) {
                        if (manga.getStatus().toLowerCase().contains(searchField.getText().toLowerCase())) {
                            HandleFilter(newMangaArray, manga);
                        }
                    }

                    contentTable = new ContentScroll(newMangaArray);
                    transition.display(contentTable.getScrollPane());
                }
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Library Management");
//        frame.setResizable(false);
        frame.setContentPane(new MainLayoutGUI().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
