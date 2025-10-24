package org.recipe_system.views.RecipeViews;

import org.recipe_system.Controller.Controller;
import org.recipe_system.Model.Recipe;
import org.recipe_system.Utils.StringHandler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;

public class ListRecipesView extends JFrame {

    public ListRecipesView(Controller controller) {
        setTitle("Lista de Receitas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(new EmptyBorder(10, 10, 10, 10));

        ArrayList<Recipe> recipes = controller.getAllRecipes();

        String[] columnNames = {"Nome", "Refeições", "Editar", "Remover"};
        Object[][] data = new Object[recipes.size()][4];
        for (int i = 0; i < recipes.size(); i++) {
            Recipe recipe = recipes.get(i);
            data[i][0] = StringHandler.capitalize(recipe.getName());
            data[i][1] = recipe.getServings(); // Corrigido
            data[i][2] = "Editar";
            data[i][3] = "Remover";
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 3;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(30);

        table.getColumn("Editar").setCellRenderer(new JTableButtonRenderer());
        table.getColumn("Editar").setCellEditor(new JTableButtonEditor(new JCheckBox(), (row) -> {
            Recipe recipeToEdit = recipes.get(row);
            EditRecipeView editRecipeView = new EditRecipeView(this, controller, recipeToEdit);
            editRecipeView.setVisible(true);
            dispose();
            new ListRecipesView(controller).setVisible(true);

        }));

        table.getColumn("Remover").setCellRenderer(new JTableButtonRenderer());
        table.getColumn("Remover").setCellEditor(new JTableButtonEditor(new JCheckBox(), (row) -> {
            Recipe recipeToRemove = recipes.get(row);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Deseja remover a receita: " + StringHandler.capitalize(recipeToRemove.getName()) + "?",
                    "Confirmar remoção",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
//                Boolean success = controller.removeRecipe(recipeToRemove); // Usando ID
//                if (success) {
//                    JOptionPane.showMessageDialog(this, "Receita removida com sucesso!");
//                    dispose();
//                    new ListRecipesView(controller).setVisible(true);
//                } else {
//                    JOptionPane.showMessageDialog(this, "Erro ao remover a receita.", "Erro", JOptionPane.ERROR_MESSAGE);
//                }
            }
        }));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton addRecipeButton = new JButton("Adicionar Receita");
        addRecipeButton.addActionListener(e -> {
            AddRecipeView addRecipeView = new AddRecipeView(this, controller);
            addRecipeView.setVisible(true);
            dispose();
            new ListRecipesView(controller).setVisible(true);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(addRecipeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setSize(600, 400);
    }
    // Classe interna para renderizar o botão na célula da tabela
    static class JTableButtonRenderer extends JButton implements TableCellRenderer {
        public JTableButtonRenderer() {
            setOpaque(true);
            setFocusPainted(false);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            if ("Editar".equals(getText())) {
                setBackground(new Color(251, 205, 83));
                setForeground(Color.BLACK);
            } else if ("Remover".equals(getText())) {
                setBackground(new Color(220, 80, 80));
                setForeground(Color.WHITE);
            }
            return this;
        }
    }

    // Classe interna para lidar com o clique no botão da tabela
    static class JTableButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private int row;
        private final ButtonAction action;

        public interface ButtonAction {
            void onClick(int row);
        }

        public JTableButtonEditor(JCheckBox checkBox, ButtonAction action) {
            super(checkBox);
            this.action = action;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.row = row;
            button.setText((value == null) ? "" : value.toString());
            if ("Editar".equals(button.getText())) {
                button.setBackground(new Color(251, 205, 83));
                button.setForeground(Color.BLACK);
            } else if ("Remover".equals(button.getText())) {
                button.setBackground(new Color(220, 80, 80));
                button.setForeground(Color.WHITE);
            }
            return button;
        }

        public Object getCellEditorValue() {
            action.onClick(row);
            return button.getText();
        }
    }
}