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
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        Transition transition = new Transition();
        ContentScroll contentTable = new ContentScroll();
        transition.display(contentTable.contentScroll);
        contentLayout.add(transition, constraints);

        homeButton.addActionListener(e -> {
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            panel.add(new JLabel("click"));

            transition.display(panel);
        });

        addButton.addActionListener(e -> {
//            contentScroll.setVisible(false);
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args) {
        MangaDexApiHandling mangaDexApiHandling = new MangaDexApiHandling(5);
        JFrame frame = new JFrame("Register");
//        frame.setResizable(false);
        frame.setContentPane(new MainLayoutGUI().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
