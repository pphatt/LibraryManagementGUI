package org.layout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Test extends JPanel implements ActionListener {
    private final JPanel pages = new JPanel(null);
    private final JButton leftButton = new JButton("<<");
    private final JButton rightButton = new JButton(">>");
    private final Timer animation = new Timer(0, this);
    private boolean direction;

    Test() {
        super(new BorderLayout());
        add(leftButton, BorderLayout.WEST);
        add(pages, BorderLayout.CENTER);
        add(rightButton, BorderLayout.EAST);
        leftButton.addActionListener(this);
        rightButton.addActionListener(this);
        animation.setInitialDelay(10);
        animation.setRepeats(false);
    }

    public void addPage(JComponent page) {
        page.setLocation(0, 0);
        pages.add(page);
        pages.setComponentZOrder(page, pages.getComponentCount() - 1);
        SwingUtilities.invokeLater(this::doLayout);
    }

    @Override
    public void doLayout() {
        Dimension size = getParent().getSize();
        size.width -= leftButton.getWidth() + rightButton.getWidth();
        pages.setSize(size);
        for (Component page : pages.getComponents()) {
            page.setSize(size);
        }
        super.doLayout();
    }

    private void scrollLeft() {
        Component next = pages.getComponents()[pages.getComponentCount() - 1];
        pages.setComponentZOrder(next, 1);
        animation.start();
    }

    private void scrollRight() {
        animation.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == leftButton) {
            scrollLeft();
        } else if (src == rightButton) {
            scrollRight();
        } else if (src == animation) {
            Component onTop = pages.getComponents()[0];

            onTop.setLocation(onTop.getX() + (direction ? +4 : -4), 0);

            if (direction) {
                pages.setComponentZOrder(onTop, 1);
            } else {
                pages.setComponentZOrder(onTop, pages.getComponentCount() - 1);
            }

            onTop.setLocation(0, 0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Multi pages demo App");

                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                Test multiPage = new Test();
                multiPage.setPreferredSize(new Dimension(250, 100));

                JPanel page;
                page = new JPanel();
                page.setBackground(Color.CYAN);
                page.setLayout(new BorderLayout());
                page.add(new JLabel("First page", SwingConstants.CENTER), BorderLayout.CENTER);
                multiPage.addPage(page);

                page = new JPanel();
                page.setBackground(Color.YELLOW);
                page.setLayout(new BorderLayout());
                page.add(new JLabel("Second page", SwingConstants.CENTER), BorderLayout.CENTER);
                multiPage.addPage(page);

                frame.add(multiPage, BorderLayout.CENTER);

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}