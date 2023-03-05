package org.layout;

import org.layout.APIHandleUtils.Manga;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.util.ArrayList;

public class ContentScroll extends JPanel {
    private JScrollPane contentScroll;
    private JTable contentTable;

    public ContentScroll(ArrayList<Manga> mangaArray) {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        contentTable.setModel(model);

        model.addColumn("ID");
        model.addColumn("Title");
        model.addColumn("Author");
        model.addColumn("Genre");
        model.addColumn("Status");
        model.addColumn("Year Release");
        model.addColumn("Chapter");

        contentTable.getTableHeader().setReorderingAllowed(false);
        contentTable.removeColumn(contentTable.getColumnModel().getColumn(0));

        for (Manga manga : mangaArray) {
            model.addRow(new Object[]{manga.getUuid(), manga.getTitle(), manga.getAuthor(), manga.getGenre(), manga.getStatus(),
                    manga.getYearRelease(), manga.getChapters()});
        }

        TableColumnModel columns = contentTable.getColumnModel();
        DefaultTableCellRenderer centerColumn = new DefaultTableCellRenderer();
        centerColumn.setHorizontalAlignment(JLabel.CENTER);
        columns.getColumn(3).setCellRenderer(centerColumn);
        columns.getColumn(4).setCellRenderer(centerColumn);
        columns.getColumn(5).setCellRenderer(centerColumn);

        ListSelectionModel cellSelectionModel = contentTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel.addListSelectionListener(e -> {
            int selectedRow = contentTable.getSelectedRow();
            String title = (String) contentTable.getValueAt(selectedRow, 0);
            String author = (String) contentTable.getValueAt(selectedRow, 1);
            String genre = (String) contentTable.getValueAt(selectedRow, 2);
            String status = (String) contentTable.getValueAt(selectedRow, 3);
            String yearRelease = (String) contentTable.getValueAt(selectedRow, 4);
            String description = mangaArray.get(selectedRow).getDescription();
            String chapter = contentTable.getValueAt(selectedRow, 5).toString();

            ViewDetailsBook viewDetailsDialog = new ViewDetailsBook(title, author, genre, status, yearRelease, description, chapter);
            viewDetailsDialog.setLocationRelativeTo(contentScroll);
            viewDetailsDialog.setVisible(true);
        });
    }

    public JScrollPane getScrollPane() {
        return contentScroll;
    }

    public JTable getTable() {
        return contentTable;
    }

//    public static void main(String[] args) {
//        MangaDexApiHandling mangaDexApiHandling = new MangaDexApiHandling(10);
//        JFrame frame = new JFrame("Register");
////        frame.setResizable(false);
//        frame.setContentPane(new ContentScroll().contentScroll);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
//    }
}
