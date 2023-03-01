package org.layout;

import org.layout.APIHandleUtils.Manga;
import org.layout.APIHandleUtils.MangaDexApiHandling;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.layout.APIHandleUtils.MangaDexApiHandling.mangaArray;

public class MainLayoutGUI {
    private JPanel MainPanel;
    private JPanel headLayout;
    private JTextField textField1;
    private JPanel mainLayout;
    private JPanel navLayout;
    private JPanel innerNavLayout;
    private JButton homeButton;
    private JButton addButton;
    private JButton quitButton;
    private JPanel contentLayout;
    private JTable contentTable;
    private JScrollPane contentScroll;

    /*
     * Gridbaglayout Properties:
     * - [gridX, gridY] = [row, column]:
     *   - The gridX means that which position are the elements are placed in the gird layout
     *   - The gridY means that which column are the elements are placed in the gird layout
     *
     * */

    public MainLayoutGUI() {
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

        JPanel addPanel = new JPanel();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        addPanel.setLayout(new GridBagLayout());
        JLabel l = new JLabel("Add");
        l.setForeground(Color.WHITE);
        addPanel.add(l, constraints);
        addPanel.setVisible(false);
        contentTable.add(addPanel);

        homeButton.addActionListener(e -> {
            contentScroll.setVisible(true);
            addPanel.setVisible(false);
        });

        addButton.addActionListener(e -> {
            contentScroll.setVisible(false);
            contentScroll.setOpaque(false);
            addPanel.setVisible(true);
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args) {
        MangaDexApiHandling mangaDexApiHandling = new MangaDexApiHandling(20);
        JFrame frame = new JFrame("Register");
//        frame.setResizable(false);
        frame.setContentPane(new MainLayoutGUI().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
