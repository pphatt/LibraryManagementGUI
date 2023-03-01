package org.layout;

import org.layout.APIHandleUtils.Manga;
import org.layout.APIHandleUtils.MangaDexApiHandling;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import static org.layout.APIHandleUtils.MangaDexApiHandling.mangaArray;

public class ContentScroll extends JPanel {
    public JScrollPane contentScroll;
    private JTable contentTable;

    public ContentScroll() {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        contentTable.setModel(model);

        model.addColumn("Title");
        model.addColumn("Author");
        model.addColumn("Genre");
        model.addColumn("Status");
        model.addColumn("Year Release");
        model.addColumn("Chapter");

        contentTable.setModel(model);
        contentTable.getTableHeader().setReorderingAllowed(false);

        for (Manga manga : mangaArray) {
            model.addRow(new Object[]{manga.getTitle(), manga.getAuthor(), manga.getGenre(), manga.getStatus(),
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
            String selectedData = (String) contentTable.getValueAt(selectedRow, 0);
        });
    }

    public static void main(String[] args) {
        MangaDexApiHandling mangaDexApiHandling = new MangaDexApiHandling(10);
        JFrame frame = new JFrame("Register");
//        frame.setResizable(false);
        frame.setContentPane(new ContentScroll().contentScroll);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
