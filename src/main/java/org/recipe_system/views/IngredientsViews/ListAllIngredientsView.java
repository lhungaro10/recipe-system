package org.recipe_system.views.IngredientsViews;

import org.recipe_system.Controller.Controller;
import org.recipe_system.Model.Ingredient;
import org.recipe_system.Utils.StringHandler;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;

public class ListAllIngredientsView extends JFrame {
    public ListAllIngredientsView(Controller controller) {
        setTitle("Lista de Ingredientes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        ArrayList<Ingredient> ingredients = controller.listIngredients();

        String[] columnNames = {"Estoque", "Nome", "Editar", "Remover"};
        Object[][] data = new Object[ingredients.size()][4];
        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ing = ingredients.get(i);
            data[i][0] = ing.getQtd_in_stock();
            data[i][1] = StringHandler.capitalize(ing.getName());
            data[i][2] = "Editar";
            data[i][3] = "Remover";
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            public boolean isCellEditable(int row, int col) {
                return col == 2 || col == 3;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(36);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // Aplica o alinhamento central em todas as colunas
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Renderizador de botão
        TableCellRenderer buttonRenderer = new JTableButtonRenderer();
        table.getColumn("Editar").setCellRenderer(buttonRenderer);
        table.getColumn("Remover").setCellRenderer(buttonRenderer);

        // Editor de botão
        table.getColumn("Editar").setCellEditor(new JTableButtonEditor(new JCheckBox(), (row) -> {
            Ingredient ing = ingredients.get(row);
            EditIngredientView editDialog = new EditIngredientView(this, controller, ing);
            editDialog.setVisible(true);
            dispose();
            new ListAllIngredientsView(controller).setVisible(true);
        }));

        table.getColumn("Remover").setCellEditor(new JTableButtonEditor(new JCheckBox(), (row) -> {
            Ingredient ing = ingredients.get(row);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Deseja remover o ingrediente: " + ing.getName() + "?",
                    "Confirmar remoção",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                Boolean success = controller.removeIngredient(ing); // Implemente este método no Controller
                if (!success) {
                    JOptionPane.showMessageDialog(this, "Erro ao remover o ingrediente: " + ing.getName(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                JOptionPane.showMessageDialog(this, "Removido: " + ing.getName());
                dispose();
                new ListAllIngredientsView(controller).setVisible(true);
            }
        }));

        JScrollPane scrollPane = new JScrollPane(table);

        JButton addButton = new JButton("Adicionar Novo Ingrediente");
        addButton.setPreferredSize(new Dimension(300, 36));
        addButton.addActionListener(e -> {
            AddIngredientView addIngredientDialog = new AddIngredientView(this, controller);
            addIngredientDialog.setVisible(true);
            dispose();
            new ListAllIngredientsView(controller).setVisible(true);
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(addButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    }

    static class JTableButtonRenderer extends JButton implements TableCellRenderer {
        public JTableButtonRenderer() {
            setOpaque(true);
            setFocusPainted(false);
            setPreferredSize(new Dimension(110, 36));
            setBackground(new Color(240, 240, 240));
            setBorder(new javax.swing.border.LineBorder(new Color(120, 120, 120), 1, true));
            setFont(new Font("SansSerif", Font.PLAIN, 14));
        }
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            if ("Editar".equals(getText())) {
                setBackground(new Color(251, 205, 83));

                setForeground(Color.WHITE);
            } else if ("Remover".equals(getText())) {
                setBackground(new Color(220, 80, 80));
                setForeground(Color.WHITE);
            } else {
                setBackground(new Color(240, 240, 240));
                setForeground(Color.BLACK);
            }
            return this;
        }
    }

    static class JTableButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int row;
        private boolean clicked;
        private ButtonAction action;

        public interface ButtonAction {
            void onClick(int row);
        }

        public JTableButtonEditor(JCheckBox checkBox, ButtonAction action) {
            super(checkBox);
            this.action = action;
            button = new JButton();
            button.setOpaque(true);
            button.setFocusPainted(false);
            button.setPreferredSize(new Dimension(110, 36));
            button.setBorder(new javax.swing.border.LineBorder(new Color(120, 120, 120), 1, true));
            button.setFont(new Font("SansSerif", Font.PLAIN, 14));
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.row = row;
            button.setText((value == null) ? "" : value.toString());
            if ("Editar".equals(button.getText())) {
                button.setBackground(new Color(255, 204, 0)); // Amarelo
                button.setForeground(Color.BLACK);
            } else if ("Remover".equals(button.getText())) {
                button.setBackground(new Color(220, 80, 80));
                button.setForeground(Color.WHITE);
            } else {
                button.setBackground(new Color(240, 240, 240));
                button.setForeground(Color.BLACK);
            }
            clicked = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (clicked) {
                action.onClick(row);
            }
            clicked = false;
            return button.getText();
        }

        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }
}