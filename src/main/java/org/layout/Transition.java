package org.layout;

import javax.swing.*;
import java.awt.*;

public class Transition extends JPanel {
    private JLayeredPane body;

    public Transition() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        setOpaque(false);
        setLayout(new GridBagLayout());
        body = new JLayeredPane();
        body.setLayout(new CardLayout());
        add(body, constraints);
    }

    public void display(Component form) {
        body.removeAll();
        body.add(form);
        body.repaint();
        body.revalidate();
        form.setVisible(true);
    }
}
