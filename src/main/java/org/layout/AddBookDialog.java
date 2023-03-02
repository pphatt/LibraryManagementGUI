package org.layout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddBookDialog extends JDialog {
    private JPanel contentPane;
    private JButton cancelButton;

    public AddBookDialog() {
        setContentPane(new AddBookGUI().getAddBookGUI());
        setModal(true);
        setTitle("Add Book");
        setSize(new Dimension(500, 700));

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
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        AddBookDialog dialog = new AddBookDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
