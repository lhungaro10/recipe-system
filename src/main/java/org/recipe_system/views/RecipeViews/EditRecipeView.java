package org.recipe_system.views.RecipeViews;

import org.recipe_system.Controller.Controller;
import org.recipe_system.Model.Ingredient;
import org.recipe_system.Model.Recipe;
import org.recipe_system.Model.RecipeIngredient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EditRecipeView extends JDialog {
    private final Recipe recipeToEdit;
    private JTextField nameField;
    private JTextField servingsField;
    private JTable availableIngredientsTable;
    private JTable selectedIngredientsTable;
    private IngredientsTableModel selectedIngredientsModel;
    private DefaultTableModel availableIngredientsModel;
    private List<Ingredient> allIngredients;

    public EditRecipeView(JFrame parent, Controller controller, Recipe recipe) {
        super(parent,"Editar Receita", true);
        this.recipeToEdit = recipe;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(new EmptyBorder(20, 24, 20, 24));

        // --- PAINEL SUPERIOR (NOME E PORÇÕES) ---
        JPanel formPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        formPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel nameLabel = new JLabel("Nome da Receita:");
        nameField = new JTextField(recipeToEdit.getName()); // Preenche o nome
        styleTextField(nameField);

        nameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String newName = nameField.getText().trim().toLowerCase();
                // Verifica se o nome mudou e se o novo nome já existe
                if (!newName.isEmpty() && !newName.equals(recipeToEdit.getName()) && controller.verifyRecipeAlreadyExists(newName)) {
                    JOptionPane.showMessageDialog(
                            EditRecipeView.this,
                            "Já existe uma receita cadastrada com este nome.",
                            "Nome Duplicado",
                            JOptionPane.ERROR_MESSAGE
                    );
                    nameField.setText(recipeToEdit.getName()); // Reverte para o nome original
                }
            }
        });

        JLabel servingsLabel = new JLabel("Número de Refeições:");
        servingsField = new JTextField(String.valueOf(recipeToEdit.getNumberOfServings())); // Preenche as porções
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

        allIngredients = controller.getAllIngredients();
        List<String> recipeIngredientNames = recipeToEdit.getIngredients().stream()
                .map(RecipeIngredient::getIngredient_name)
                .collect(Collectors.toList());

        // Tabela de ingredientes disponíveis
        availableIngredientsModel = new DefaultTableModel(new Object[]{"Ingredientes Disponíveis"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        allIngredients.stream()
                .filter(ing -> !recipeIngredientNames.contains(ing.getName()))
                .forEach(ing -> availableIngredientsModel.addRow(new Object[]{ing.getName()}));
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
        recipeToEdit.getIngredients().forEach(ri -> selectedIngredientsModel.addRow(new Object[]{ri.getIngredient_name(), ri.getRequired_quantity()}));
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
        saveButton.addActionListener(e -> updateRecipe(controller));

        pack();
        setSize(800, 500);
    }

    private void transferIngredient(JTable sourceTable, DefaultTableModel sourceModel, DefaultTableModel destModel, boolean isAdding) {
        int selectedRow = sourceTable.getSelectedRow();
        if (selectedRow >= 0) {
            String ingredientName = sourceModel.getValueAt(selectedRow, 0).toString();
            sourceModel.removeRow(selectedRow);

            if (isAdding) {
                // Adiciona à tabela de selecionados com quantidade inicial 1
                ((IngredientsTableModel) destModel).addRow(new Object[]{ingredientName, 1});
            } else {
                // Adiciona de volta à tabela de disponíveis
                destModel.addRow(new Object[]{ingredientName});
            }
        }
    }

    private void updateRecipe(Controller controller) {
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

            ArrayList<Ingredient> recipeIngredients = new ArrayList<>();
            ArrayList<Integer> recipeIngredientsQtd = new ArrayList<>();
            for (int i = 0; i < selectedIngredientsModel.getRowCount(); i++) {
                String ingredientName = (String) selectedIngredientsModel.getValueAt(i, 0);
                int quantity = (Integer) selectedIngredientsModel.getValueAt(i, 1);

                Ingredient originalIngredient = allIngredients.stream()
                        .filter(ing -> ing.getName().equals(ingredientName))
                        .findFirst()
                        .orElse(null);

                if (originalIngredient == null) {
                    JOptionPane.showMessageDialog(this, "Ocorreu um erro durante a seleção de ingrediente.", "Ingrediente inválido", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                recipeIngredients.add(originalIngredient);
                recipeIngredientsQtd.add(quantity);
            }

            // Chama o método de atualização no controller
            Boolean success = controller.editRecipe(recipeToEdit, name, servings, recipeIngredients, recipeIngredientsQtd);
            if (!success) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar a receita. Verifique os dados e tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "Receita atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
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

    private class IngredientsTableModel extends DefaultTableModel {
        public IngredientsTableModel(Object[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return (columnIndex == 1) ? Integer.class : String.class;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 1;
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            if (column == 1) {
                try {
                    int value = Integer.parseInt(aValue.toString());
                    if (value > 0) {
                        super.setValueAt(value, row, column);
                    } else {
                        JOptionPane.showMessageDialog(null, "A quantidade deve ser um número inteiro positivo.", "Valor Inválido", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Por favor, insira um número válido para a quantidade.", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                super.setValueAt(aValue, row, column);
            }
        }
    }
}