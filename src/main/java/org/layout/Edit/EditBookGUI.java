package org.layout.Edit;

import org.layout.db.SQLConnectionString;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import static org.layout.APIHandleUtils.MangaDexApiHandling.mangaArray;

public class EditBookGUI extends JDialog {
    private JPanel contentPane;
    private JScrollPane mainLayout;
    private JPanel layout;
    private JPanel editTitleLayout;
    private JTextField editTitleField;
    private JPanel editAuthorLayout;
    private JTextField editAuthorField;
    private JPanel editTypeLayout;
    private JComboBox<String> editTypeCombobox;
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
    private JLabel warningTypeField;
    private JLabel warningStatusField;
    private JLabel warningYearField;
    private JLabel warningDescriptionField;
    private JLabel warningChapterField;
    private JScrollPane genreDetail;
    private JPanel editGenreLayout;
    private JPanel functionLayout;
    private JPanel genreInnerLayout;
    private JComboBox<String> genreCombobox;
    private JButton addButton;
    private JPanel editAuthorOption;
    private JButton viewAuthorButton;
    private JButton createNewAuthorButton;
    private JButton cancelButton1;
    private JPanel externalField;
    ArrayList<String> exceptions = new ArrayList<>();
    private ArrayList<String> newGenres = new ArrayList<>();
    public int count = 0;

    public EditBookGUI(int index, String uuid, String title, String author, String type, String status, String year, String description, String chapter) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(cancelButton);

        setTitle("Edit book: " + title);
        setSize(new Dimension(800, 500));

        String[] typeItem = {"manga"};
        String[] statusItem = {"ongoing", "completed", "hiatus", "cancelled"};
        String[] yearItem = new String[23];

        for (int i = 0; i < yearItem.length; i++) {
            yearItem[i] = 2000 + yearItem.length - i - 1 + "";
        }

        editTitleField.setText(title);
        editAuthorField.setText(author);

        DefaultComboBoxModel<String> genreComboboxModel = new DefaultComboBoxModel<>(typeItem);
        int getCurrentGenreIndex = 0;

        for (int i = 0; i < typeItem.length; i++) {
            if (typeItem[i].equals(type)) {
                getCurrentGenreIndex = i;
                break;
            }
        }

        editTypeCombobox.setModel(genreComboboxModel);
        editTypeCombobox.setSelectedIndex(getCurrentGenreIndex);

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

        try {
            PreparedStatement bookGenreQuery = SQLConnectionString.getConnection().prepareStatement("Select * from BookGenre where BookID = ? and State = '0'");
            bookGenreQuery.setString(1, uuid);
            ResultSet bookGenreQueryRes = bookGenreQuery.executeQuery();

            while (bookGenreQueryRes.next()) {
                PreparedStatement genre = SQLConnectionString.getConnection().prepareStatement("Select * from Genre where ID = ?");
                genre.setString(1, bookGenreQueryRes.getString(2));
                ResultSet genreRes = genre.executeQuery();

                while (genreRes.next()) {
                    genreBox(genreRes.getString(2), Color.BLACK);
                    exceptions.add(genreRes.getString(2));
                }
            }

            fillGenreCombobox(exceptions, newGenres);
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
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        saveButton.addActionListener(e -> {
            warningTitleField.setVisible(false);
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

            String newAuthor;

            if (editAuthorField.getText().trim().equals(author)) {
                newAuthor = author;
            } else {
                newAuthor = editAuthorField.getText().split(" → ")[1].trim();
            }

            mangaArray.get(index).setTitle(editTitleField.getText());
            mangaArray.get(index).setAuthor(newAuthor);
            mangaArray.get(index).setGenre(Objects.requireNonNull(editTypeCombobox.getSelectedItem()).toString());
            mangaArray.get(index).setStatus(Objects.requireNonNull(editStatusCombobox.getSelectedItem()).toString());
            mangaArray.get(index).setYearRelease(Objects.requireNonNull(editYearCombobox.getSelectedItem()).toString());
            mangaArray.get(index).setDescription(editDescriptionField.getText().trim().replaceAll("\n", " "));
            mangaArray.get(index).setChapters(newChapter);

            checkAuthor(newAuthor, uuid, newChapter);
            checkGenre(uuid);

            JOptionPane.showMessageDialog(mainLayout,
                    "Edited Successfully", "Notify message",
                    JOptionPane.INFORMATION_MESSAGE);

            onCancel();
        });

        viewAuthorButton.addActionListener(e -> {
            externalField.removeAll();
            ViewAuthor view = new ViewAuthor(author);

            GridBagConstraints constraints = new GridBagConstraints();

            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.anchor = GridBagConstraints.FIRST_LINE_START;

            externalField.add(view.getMainLayout(), constraints);
            externalField.repaint();
            externalField.revalidate();
            view.getMainLayout().setVisible(true);

            view.getAddButton().addActionListener(event -> {
                editAuthorField.setText(author + " → " + Objects.requireNonNull(view.getAuthorCombobox().getSelectedItem()));
            });
        });

        createNewAuthorButton.addActionListener(e -> {
            externalField.removeAll();

            AddNewAuthor aa = new AddNewAuthor();

            GridBagConstraints constraints = new GridBagConstraints();

            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.anchor = GridBagConstraints.FIRST_LINE_START;

            externalField.add(aa.getMainLayout(), constraints);
            externalField.repaint();
            externalField.revalidate();
            aa.getMainLayout().setVisible(true);

            aa.getAuthorName().getDocument().addDocumentListener(new DocumentListener() {
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
                    editAuthorField.setText(author + " → " + aa.getAuthorName().getText());
                }
            });
        });

        cancelButton1.addActionListener(e -> {
            externalField.removeAll();
            externalField.repaint();
            externalField.revalidate();
            editAuthorField.setText(author);
        });

        cancelButton.addActionListener(e -> onCancel());

        addButton.addActionListener(e -> {
            newGenres.add(Objects.requireNonNull(genreCombobox.getSelectedItem()).toString());

            genreBox(genreCombobox.getSelectedItem().toString(), Color.BLUE);

            fillGenreCombobox(exceptions, newGenres);
        });
    }

    public void fillGenreCombobox(ArrayList<String> exceptions, ArrayList<String> newGenres) {
        try {
            PreparedStatement genreQuery = SQLConnectionString.getConnection().prepareStatement("Select * from Genre");
            ResultSet genreQueryRes = genreQuery.executeQuery();

            ArrayList<String> genre = new ArrayList<>();

            while (genreQueryRes.next()) {
                genre.add(genreQueryRes.getString(2));
            }

            genre.removeAll(exceptions);
            genre.removeAll(newGenres);

            DefaultComboBoxModel<String> genreModel = new DefaultComboBoxModel<>(genre.toArray(new String[0]));
            genreCombobox.setModel(genreModel);
        } catch (SQLException ignored) {
        }
    }

    public void genreBox(String genre, Color color) {
        JLabel label = new JLabel(genre);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        Border border = BorderFactory.createLineBorder(color);
        Border margin = new EmptyBorder(5, 5, 5, 5);
        panel.setBorder(BorderFactory.createCompoundBorder(border, margin));

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = count;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.ipadx = 4;
        constraints.ipady = 4;
        constraints.insets = new Insets(0, 0, 0, 10);
        constraints.fill = GridBagConstraints.BOTH;

        panel.add(label);
        genreInnerLayout.add(panel, constraints);
        count++;

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String[] options = {"Yes", "No"};

                int o = JOptionPane.showOptionDialog(contentPane,
                        "Delete " + label.getText() + " Genre", "Notify message",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);

                if (o == JOptionPane.YES_OPTION) {
                    genreInnerLayout.removeAll();
                    ArrayList<String> newExc = new ArrayList<>();
                    ArrayList<String> newGen = new ArrayList<>();

                    for (String exception : exceptions) {
                        if (!exception.equals(label.getText())) {
                            genreBox(exception, Color.BLACK);
                            newExc.add(exception);
                        }
                    }

                    for (String newGenre : newGenres) {
                        if (!newGenre.equals(label.getText())) {
                            genreBox(newGenre, Color.BLUE);
                            newGen.add(newGenre);
                        }
                    }

                    exceptions = new ArrayList<>(newExc);
                    newGenres = new ArrayList<>(newGen);

                    genreInnerLayout.revalidate();
                    genreInnerLayout.repaint();
                    fillGenreCombobox(exceptions, newGenres);
                }
            }
        });
    }

    public void checkAuthor(String author, String uuid, Integer newChapter) {
        try {
            PreparedStatement authorQuery = SQLConnectionString.getConnection().prepareStatement("Select * from Author where Name = ?");
            authorQuery.setString(1, author);
            ResultSet authorQueryRes = authorQuery.executeQuery();

            if (authorQueryRes.next()) {
                updateEditBook(authorQueryRes.getString(1), uuid, newChapter);
            } else {
                String _uuid = UUID.randomUUID().toString();

                PreparedStatement insertAuthor = SQLConnectionString.getConnection().prepareStatement(
                        "Insert into Author (ID, Name, State) values (?, ?, '0')"
                );

                insertAuthor.setString(1, _uuid);
                insertAuthor.setString(2, author);
                insertAuthor.executeUpdate();

                updateEditBook(_uuid, uuid, newChapter);
            }
        } catch (SQLException e) {
        }
    }

    public void checkGenre(String uuid) {
        try {
            PreparedStatement bookGenreQuery = SQLConnectionString.getConnection().prepareStatement("Select GenreID, Genre.Name from Genre, BookGenre where BookID = ? and Genre.ID = BookGenre.GenreID");
            bookGenreQuery.setString(1, uuid);
            ResultSet bookGenreQueryRes = bookGenreQuery.executeQuery();

            while (bookGenreQueryRes.next()) {
                if (!exceptions.contains(bookGenreQueryRes.getString(2))) {
                    PreparedStatement updateBookGenre = SQLConnectionString.getConnection().prepareStatement("Update BookGenre set State = '1' where BookID = ? and GenreID = ?");
                    updateBookGenre.setString(1, uuid);
                    updateBookGenre.setString(2, bookGenreQueryRes.getString(1));
                    updateBookGenre.executeUpdate();
                }
            }

            for (String newGenre : newGenres) {
                PreparedStatement genreQuery = SQLConnectionString.getConnection().prepareStatement("Select * from Genre where Name = ?");
                genreQuery.setString(1, newGenre);
                ResultSet genreQueryRes = genreQuery.executeQuery();

                if (genreQueryRes.next()) {
                    PreparedStatement checkBookGenre = SQLConnectionString.getConnection().prepareStatement("Select * from BookGenre where BookID = ? and GenreID = ?");
                    checkBookGenre.setString(1, uuid);
                    checkBookGenre.setString(2, genreQueryRes.getString(1));
                    ResultSet checkBookGenreRes = checkBookGenre.executeQuery();

                    if (checkBookGenreRes.next()) {
                        PreparedStatement updateBookGenre = SQLConnectionString.getConnection().prepareStatement("Update BookGenre set State = '0' where BookID = ? and GenreID = ?");
                        updateBookGenre.setString(1, uuid);
                        updateBookGenre.setString(2, genreQueryRes.getString(1));
                        updateBookGenre.executeUpdate();
                        continue;
                    }

                    PreparedStatement updateBookGenre = SQLConnectionString.getConnection().prepareStatement("Insert into BookGenre (BookID, GenreID, State) Values (?, ?, '0')");
                    updateBookGenre.setString(1, uuid);
                    updateBookGenre.setString(2, genreQueryRes.getString(1));
                    updateBookGenre.executeUpdate();
                }
            }
        } catch (SQLException ignored) {
        }
    }

    public void updateEditBook(String authorID, String uuid, Integer newChapter) {
        try {
            PreparedStatement updateBookQuery = SQLConnectionString.getConnection().prepareStatement(
                    "Update Book set Title = ?, Author = ?, Type = ?, Status = ?, YearReleased = ?, Description = ?, Chapter = ? where ID = ?"
            );

            updateBookQuery.setString(1, editTitleField.getText());
            updateBookQuery.setString(2, authorID);
            updateBookQuery.setString(3, "1");
            updateBookQuery.setString(4, editStatusCombobox.getSelectedItem().toString());
            updateBookQuery.setString(5, editYearCombobox.getSelectedItem().toString());
            updateBookQuery.setString(6, editDescriptionField.getText().trim().replaceAll("\n", " "));
            updateBookQuery.setString(7, newChapter + "");
            updateBookQuery.setString(8, uuid);
            updateBookQuery.executeUpdate();
        } catch (SQLException ignored) {
        }
    }

    private void onCancel() {
        dispose();
    }
}
