package org.recipe_system.views;

import org.recipe_system.Controller.Controller;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class AddIngredientView extends JDialog {
    public AddIngredientView(JFrame parent, Controller controller) {
        super(parent, "Adicionar Ingrediente", true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 16, 16));
        JLabel nameLabel = new JLabel("Nome:");
        JTextField nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(180, 32));
        nameField.setBorder(new LineBorder(new Color(180, 180, 180), 1, true));
        nameField.setMargin(new Insets(6, 10, 6, 10)); // Espaçamento interno

        JLabel qtdLabel = new JLabel("Quantidade em estoque:");
        JTextField qtdField = new JTextField();
        qtdField.setPreferredSize(new Dimension(180, 32));
        qtdField.setBorder(new LineBorder(new Color(180, 180, 180), 1, true));
        qtdField.setMargin(new Insets(6, 10, 6, 10)); // Espaçamento interno

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(qtdLabel);
        formPanel.add(qtdField);

        // Painel intermediário para espaçamento entre inputs e botões
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.setBorder(new EmptyBorder(0, 0, 20, 0)); // Espaço entre inputs e botões

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
                controller.registerIngredient(name, qtd);
                JOptionPane.showMessageDialog(this, "Ingrediente cadastrado com sucesso!");
                nameField.setText("");
                qtdField.setText("");
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