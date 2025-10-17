package org.recipe_system.views;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import org.recipe_system.Controller.Controller;
import org.recipe_system.Model.Ingredient;

public class ListAllIngredientsView extends JFrame {
    public ListAllIngredientsView(Controller controller) {
        setTitle("Lista de Ingredientes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        ArrayList<Ingredient> ingredients = controller.listIngredients();

        for (Ingredient ingredient : ingredients) {
            JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel nameLabel = new JLabel(ingredient.getName() + " - Estoque: " + ingredient.getQtd_in_stock());

            JButton editButton = new JButton("Editar");

            JButton removeButton = new JButton("Remover");
            removeButton.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Removido: " + ingredient.getName());
                dispose();
                new ListAllIngredientsView(controller).setVisible(true);
            });
            editButton.addActionListener(e -> {
                EditIngredientView editDialog = new EditIngredientView(this, controller, ingredient);
                editDialog.setVisible(true);
                // Após editar, atualize a listagem
                dispose();
                new ListAllIngredientsView(controller).setVisible(true);
            });

            itemPanel.add(nameLabel);
            itemPanel.add(editButton);
            itemPanel.add(removeButton);
            panel.add(itemPanel);
        }

        JButton addButton = new JButton("Adicionar Novo Ingrediente");
        addButton.addActionListener(e -> {
            // Chame a tela de cadastro aqui
            JOptionPane.showMessageDialog(this, "Adicionar novo ingrediente");
        });


        panel.add(Box.createVerticalStrut(20));
        panel.add(addButton);

        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane);
    }
}