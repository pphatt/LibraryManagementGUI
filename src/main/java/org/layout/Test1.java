package org.layout;

import org.layout.APIHandleUtils.MangaDexApiHandling;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class Test1 {
    private JPanel panel1;
    private JPanel layout;
    private JPanel titleField;
    private JTextField titleInput;
    private JPanel authorField;
    private JTextField authorInput;
    private JPanel genreField;
    private JComboBox authorCombobox;
    private JPanel statusField;
    private JComboBox statusCombobox;
    private JPanel yearField;
    private JComboBox yearCombobox;
    private JPanel descriptionLayout;
    private JTextArea descriptionTextArea;
    private JPanel chapterLayout;
    private JTextField chapterInput;
    private JPanel functionLayout;
    private JButton saveButton;
    private JButton cancelButton;
    private JPanel test;

    public Test1() {
        titleInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                test.setBackground(Color.BLACK);
                test.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                test.setBackground(null);
                test.setBorder(null);
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                T a = new T();
                a.setSize(200,200);
                a.setLocationRelativeTo(panel1);
//                a.setLocationRelativeTo(null);
                a.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Register");
//        frame.setResizable(false);
        frame.setContentPane(new Test1().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
