package org.recipe_system.views;

import org.recipe_system.Controller.Controller;
import org.recipe_system.Model.Ingredient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class EditIngredientView extends JDialog {
    public EditIngredientView(JFrame parent, Controller controller, Ingredient ingredient) {
        super(parent, "Editar Ingrediente", true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 16, 16));
        JLabel nameLabel = new JLabel("Nome:");
        JTextField nameField = new JTextField(ingredient.getName());
        nameField.setPreferredSize(new Dimension(180, 32));
        nameField.setBorder(new LineBorder(new Color(180, 180, 180), 1, true));
        nameField.setMargin(new Insets(6, 10, 6, 10));

        JLabel qtdLabel = new JLabel("Quantidade em estoque:");
        JTextField qtdField = new JTextField(String.valueOf(ingredient.getQtd_in_stock()));
        qtdField.setPreferredSize(new Dimension(180, 32));
        qtdField.setBorder(new LineBorder(new Color(180, 180, 180), 1, true));
        qtdField.setMargin(new Insets(6, 10, 6, 10));

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(qtdLabel);
        formPanel.add(qtdField);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        JButton cancelButton = new JButton("Cancelar");
        JButton saveButton = new JButton("Salvar");

        for (JButton btn : new JButton[]{cancelButton, saveButton}) {
            btn.setFocusPainted(false);
            btn.setPreferredSize(new Dimension(110, 36));
            btn.setBackground(new Color(240, 240, 240));
            btn.setBorder(new LineBorder(new Color(120, 120, 120), 1, true));
        }
        saveButton.setBackground(new Color(100, 180, 100));
        saveButton.setForeground(Color.WHITE);

        cancelButton.addActionListener(e -> dispose());

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String qtdText = qtdField.getText().trim();
            if (name.isEmpty() || qtdText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int qtd = Integer.parseInt(qtdText);
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

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }
}