package org.layout.Edit;

import org.layout.db.SQLConnectionString;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Objects;

public class ViewAuthor {
    private JPanel panel1;
    private JPanel mainLayout;
    private JComboBox<String> authorCombobox;
    private JButton addButton;
    private String item;

    public ViewAuthor(String exception) {
        ArrayList<String> authorList = new ArrayList<>();

        try {
            PreparedStatement retrieveAuthor = SQLConnectionString.getConnection().prepareStatement(
                    "Select * from Author");
            ResultSet retrieveAuthorRes = retrieveAuthor.executeQuery();

            while (retrieveAuthorRes.next()) {
                if (retrieveAuthorRes.getString(2).equals(exception)) {
                    continue;
                }

                authorList.add(retrieveAuthorRes.getString(2));
            }
        } catch (Exception ignored) {
        }

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(authorList.toArray(new String[0]));
        authorCombobox.setModel(model);

        addButton.addActionListener(e -> {
            item = Objects.requireNonNull(authorCombobox.getSelectedItem()).toString();
        });
    }

    public JPanel getMainLayout() {
        return mainLayout;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JComboBox<String> getAuthorCombobox() {
        return authorCombobox;
    }

    public String getItem() {
        return item;
    }
}
