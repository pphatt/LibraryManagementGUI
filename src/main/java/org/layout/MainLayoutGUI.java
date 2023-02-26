package org.layout;

import javax.swing.*;
import java.awt.*;

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

    /*
     * Gridbaglayout Properties:
     * - [gridX, gridY] = [row, column]:
     *   - The gridX means that which position are the elements are placed in the gird layout
     *   - The gridY means that which column are the elements are placed in the gird layout
     *
     * */

    public MainLayoutGUI() {
        JPanel layout = new JPanel();
        layout.setBackground(Color.getColor("#2C3333"));
        layout.setForeground(Color.getColor("#2C3333"));
        layout.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        for (int column = 0; column < 100; column++) {
            JPanel test = new JPanel();
            test.setBackground(Color.getColor("#2C3333"));
            JButton button = new JButton("Click" + column);
            test.add(button);
            constraints.gridx = column > 10 ? column - (10 * (column / 10)) : column;
            constraints.gridy = column > 10 ? column / 10 : 0;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.anchor = GridBagConstraints.FIRST_LINE_START;
            layout.add(test, constraints);
        }

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        JScrollPane scrollLayout = new JScrollPane(layout);
//        scrollLayout.add(layout);
        scrollLayout.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        contentLayout.add(scrollLayout, constraints);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Register");
//        frame.setResizable(false);
        frame.setContentPane(new MainLayoutGUI().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
