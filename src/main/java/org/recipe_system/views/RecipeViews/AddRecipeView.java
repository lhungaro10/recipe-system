package org.recipe_system.views.RecipeViews;

import org.recipe_system.Controller.Controller;
import org.recipe_system.Model.Ingredient;
import org.recipe_system.Model.Recipe;
import org.recipe_system.Utils.StringHandler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class AddRecipeView extends JFrame {
    private JTextField nameField;
    private JTextField servingsField;
    private JTable availableIngredientsTable;
    private JTable selectedIngredientsTable;
    private IngredientsTableModel selectedIngredientsModel;
    private DefaultTableModel availableIngredientsModel;
    private List<Ingredient> allIngredients;

    public AddRecipeView(JFrame parent, Controller controller) {
        super("Adicionar Receita");
        super.setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getRootPane().setBorder(new EmptyBorder(20, 24, 20, 24));

        // --- PAINEL SUPERIOR (NOME E PORÇÕES) ---
        JPanel formPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        formPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel nameLabel = new JLabel("Nome da Receita:");
        nameField = new JTextField();
        styleTextField(nameField);

        JLabel servingsLabel = new JLabel("Número de Refeições:");
        servingsField = new JTextField();
        styleTextField(servingsField);

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(servingsLabel);
        formPanel.add(servingsField);

        // --- PAINEL CENTRAL (SELEÇÃO DE INGREDIENTES) ---
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Tabela de ingredientes disponíveis
        allIngredients = controller.listIngredients(); // Supondo que este método exista
        availableIngredientsModel = new DefaultTableModel(new Object[]{"Ingredientes Disponíveis"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Não editável
            }
        };
        allIngredients.forEach(ing -> availableIngredientsModel.addRow(new Object[]{ing.getName()}));
        availableIngredientsTable = new JTable(availableIngredientsModel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(new JScrollPane(availableIngredientsTable), gbc);

        // Botões de transferência
        JPanel transferButtonsPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        JButton addButton = new JButton(">");
        JButton removeButton = new JButton("<");
        transferButtonsPanel.add(addButton);
        transferButtonsPanel.add(removeButton);
        gbc.gridx = 1;
        gbc.weightx = 0.1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 10, 0, 10);
        centerPanel.add(transferButtonsPanel, gbc);

        // Tabela de ingredientes selecionados
        selectedIngredientsModel = new IngredientsTableModel(new Object[]{"Ingrediente", "Quantidade"}, 0);
        selectedIngredientsTable = new JTable(selectedIngredientsModel);
        gbc.gridx = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        centerPanel.add(new JScrollPane(selectedIngredientsTable), gbc);

        // --- PAINEL INFERIOR (BOTÕES DE AÇÃO) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        JButton cancelButton = new JButton("Cancelar");
        JButton saveButton = new JButton("Salvar");
        styleButton(cancelButton, new Color(240, 240, 240), Color.BLACK);
        styleButton(saveButton, new Color(100, 180, 100), Color.WHITE);
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        // --- ADICIONANDO PAINÉIS AO FRAME ---
        add(formPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- ACTION LISTENERS ---
        addButton.addActionListener(e -> transferIngredient(availableIngredientsTable, availableIngredientsModel, selectedIngredientsModel, true));
        removeButton.addActionListener(e -> transferIngredient(selectedIngredientsTable, selectedIngredientsModel, availableIngredientsModel, false));
        cancelButton.addActionListener(e -> dispose());
        saveButton.addActionListener(e -> saveRecipe(controller));

        pack();
        setSize(800, 500); // Definindo um tamanho maior para acomodar as tabelas
    }

    private void transferIngredient(JTable sourceTable, DefaultTableModel sourceModel, DefaultTableModel destModel, boolean isAdding) {
        int selectedRow = sourceTable.getSelectedRow();
        if (selectedRow >= 0) {
            String ingredientName = sourceModel.getValueAt(selectedRow, 0).toString();
            sourceModel.removeRow(selectedRow);

            if (isAdding) {
                ((IngredientsTableModel) destModel).addRow(new Object[]{ingredientName, 1});
            } else {
                destModel.addRow(new Object[]{ingredientName});
            }
        }
    }

    private void saveRecipe(Controller controller) {
        String name = nameField.getText().trim().toLowerCase();
        String servingsText = servingsField.getText();

        if (name.isEmpty() || servingsText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o nome e o número de refeições.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int servings = Integer.parseInt(servingsText);
            if (servings <= 0) {
                JOptionPane.showMessageDialog(this, "O número de refeições deve ser maior que zero.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Coletar ingredientes da tabela
            ArrayList<Ingredient> recipeIngredients = new ArrayList<>();
            ArrayList<Integer> recipeIngredientsQtd = new ArrayList<>();
            for (int i = 0; i < selectedIngredientsModel.getRowCount(); i++) {
                String ingredientName = (String) selectedIngredientsModel.getValueAt(i, 0);
                int quantity = (Integer) selectedIngredientsModel.getValueAt(i, 1);

                // Encontrar o objeto Ingredient original para manter o ID
                Ingredient originalIngredient = allIngredients.stream()
                        .filter(ing -> ing.getName().equals(ingredientName))
                        .findFirst()
                        .orElse(null); // Ou criar um novo se preferir

                if (originalIngredient == null) {
                    JOptionPane.showMessageDialog(this, "Ocorreu um erro durante a seleção de ingrediente.", "Ingrediente inválido", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                recipeIngredients.add(originalIngredient);
                recipeIngredientsQtd.add(quantity);
            }
            // newRecipe.addIngredient(originalIngredient, quantity); // Método hipotético

             controller.registerRecipe(name, servings, recipeIngredients, recipeIngredientsQtd);

            JOptionPane.showMessageDialog(this, "Receita salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O número de refeições deve ser um valor numérico.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleTextField(JTextField textField) {
        textField.setPreferredSize(new Dimension(180, 32));
        textField.setBorder(new LineBorder(new Color(180, 180, 180), 1, true));
        textField.setMargin(new Insets(6, 10, 6, 10));
    }

    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(110, 36));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setBorder(new LineBorder(new Color(120, 120, 120), 1, true));
    }

    // Classe interna para o modelo da tabela de ingredientes selecionados
    private class IngredientsTableModel extends DefaultTableModel {
        public IngredientsTableModel(Object[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 1) {
                return Integer.class; // Coluna de quantidade é numérica
            }
            return String.class;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 1; // Apenas a coluna "Quantidade" é editável
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            if (column == 1) {
                try {
                    int value = Integer.parseInt(aValue.toString());
                    if (value > 0) {
                        super.setValueAt(value, row, column);
                    } else {
                        // Informa o usuário que o valor deve ser positivo
                        JOptionPane.showMessageDialog(null, "A quantidade deve ser um número inteiro positivo.", "Valor Inválido", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    // Informa o usuário que o valor deve ser um número
                    JOptionPane.showMessageDialog(null, "Por favor, insira um número válido para a quantidade.", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                super.setValueAt(aValue, row, column);
            }
        }
    }
}