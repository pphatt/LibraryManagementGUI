package org.layout;

import org.layout.APIHandleUtils.MangaDexApiHandling;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Form {
    private JPanel panel1;
    private JButton button1;
    private JButton button2;

    public Form() {
//        JPanel panel;
//        panel = new JPanel();
//        panel.setLayout(new GridBagLayout());
//        GridBagConstraints constraints = new GridBagConstraints();
//        constraints.gridx = 0;
//        constraints.gridy = 0;
//        panel.add(new JLabel("First page", SwingConstants.CENTER));
//        panel1.add(panel, constraints);
//
//        constraints.gridx = 1;
//        constraints.gridy = 1;
//        panel = new JPanel();
//        panel.setLayout(new GridBagLayout());
//        panel.add(new JLabel("Second page", SwingConstants.CENTER));
//        panel1.add(panel, constraints);

        MangaDexApiHandling mangaDexApiHandling = new MangaDexApiHandling(5);
        Transition transition = new Transition();

        transition.display(new ContentScroll().contentScroll);
        panel1.add(transition);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                transition.display(new JLabel("ok"));
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                transition.display(new JLabel("Yeah1"));
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Register");
//        frame.setResizable(false);
        frame.setContentPane(new Form().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
