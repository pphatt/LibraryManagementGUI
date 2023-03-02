package org.layout;

import org.layout.APIHandleUtils.MangaDexApiHandling;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JButton addDialogButton;

    /*
     * Gridbaglayout Properties:
     * - [gridX, gridY] = [row, column]:
     *   - The gridX means that which position are the elements are placed in the gird layout
     *   - The gridY means that which column are the elements are placed in the gird layout
     *
     * */

    /*
    * TODO:
    *  - Row select edit
    *  - Delete book
    *  - Add Try Catch
    *  - Search book
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
        transition.display(contentTable.getScrollPane());

        contentLayout.add(transition, constraints);

        homeButton.addActionListener(e -> transition.display(contentTable.getScrollPane()));

        addButton.addActionListener(e -> {
            AddBookGUI addBook = new AddBookGUI();
            transition.display(addBook.getAddBookGUI());
        });

        quitButton.addActionListener(e -> System.exit(0));

        addDialogButton.addActionListener(e -> {
            AddBookDialog dialog = new AddBookDialog();
            dialog.setLocationRelativeTo(MainPanel);
            dialog.setVisible(true);
        });
    }

    public static void main(String[] args) {
        MangaDexApiHandling mangaDexApiHandling = new MangaDexApiHandling(50);
        JFrame frame = new JFrame("Register");
//        frame.setResizable(false);
        frame.setContentPane(new MainLayoutGUI().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
