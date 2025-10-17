package org.recipe_system.views;

import org.recipe_system.Controller.Controller;
import org.recipe_system.Model.Ingredient;

import javax.swing.*;
import java.awt.*;

public class EditIngredientView extends JDialog {
    public EditIngredientView(JFrame parent, Controller controller, Ingredient ingredient) {
        super(parent, "Editar Ingrediente", true);
        setSize(350, 200);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JLabel nameLabel = new JLabel("Nome:");
        JTextField nameField = new JTextField(ingredient.getName());
        JLabel qtdLabel = new JLabel("Quantidade em estoque:");
        JTextField qtdField = new JTextField(String.valueOf(ingredient.getQtd_in_stock()));

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(qtdLabel);
        formPanel.add(qtdField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Cancelar");
        JButton saveButton = new JButton("Salvar");

        cancelButton.addActionListener(e -> dispose());

        saveButton.addActionListener(e -> {
            ingredient.setName(nameField.getText());
            try {
                String name = nameField.getText();
                Integer qtd = Integer.parseInt(qtdField.getText());

                Boolean success = controller.editIngredient(ingredient, name, qtd);
                if (!success) {
                    JOptionPane.showMessageDialog(this, "Erro ao editar ingrediente!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                JOptionPane.showMessageDialog(this, "Ingrediente editado com sucesso!");
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantidade inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}