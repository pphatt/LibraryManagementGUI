package org.layout;

import javax.swing.*;
import java.awt.*;

public class Transition extends JPanel {
    private JLayeredPane body;

    public Transition() {
        setOpaque(false);
        setLayout(new GridBagLayout());
        body = new JLayeredPane();
        body.setLayout(new CardLayout());
        add(body);
    }

    public void display(Component form) {
        body.add(form);
        body.repaint();
        body.revalidate();
        form.setVisible(true);
    }
}
