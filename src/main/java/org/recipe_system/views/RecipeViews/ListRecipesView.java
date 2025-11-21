// Java
// File: `src/main/java/org/recipe_system/views/RecipeViews/ListRecipesView.java`

package org.recipe_system.views.RecipeViews;

import org.recipe_system.Controller.Controller;
import org.recipe_system.Model.Recipe;
import org.recipe_system.Model.RecipeIngredient;
import org.recipe_system.Utils.StringHandler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class ListRecipesView extends JFrame {

    public ListRecipesView(Controller controller) {
        setTitle("Lista de Receitas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(new EmptyBorder(14, 14, 14, 14));

        ArrayList<Recipe> recipes = controller.getAllRecipes();

        // Painel de listagem com “cards” e scroll
        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        Color cardBg = new Color(248, 250, 252);
        Color cardBorder = new Color(230, 235, 241);
        Color titleColor = new Color(33, 37, 41);
        Color subtitleColor = new Color(108, 117, 125);

        for (Recipe recipe : recipes) {
            RoundedPanel card = new RoundedPanel(14);
            card.setLayout(new BorderLayout(12, 8));
            card.setBackground(cardBg);
            card.setBorder(new EmptyBorder(14, 16, 14, 16));

            // Cabeçalho: Nome e refeições
            JPanel header = new JPanel(new BorderLayout(8, 8));
            header.setOpaque(false);

            JLabel nameLabel = new JLabel(StringHandler.capitalize(recipe.getName()));
            nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 16f));
            nameLabel.setForeground(titleColor);

            JLabel servingsLabel = new JLabel(recipe.getNumberOfServings() + " refeições");
            servingsLabel.setFont(servingsLabel.getFont().deriveFont(Font.PLAIN, 13f));
            servingsLabel.setForeground(subtitleColor);

            header.add(nameLabel, BorderLayout.WEST);
            header.add(servingsLabel, BorderLayout.EAST);

            // Corpo: Ingredientes
            JPanel body = new JPanel(new BorderLayout());
            body.setOpaque(false);

            JLabel ingTitle = new JLabel("Ingredientes");
            ingTitle.setFont(ingTitle.getFont().deriveFont(Font.BOLD, 13f));
            ingTitle.setForeground(titleColor);

            JTextArea ingredientsArea = new JTextArea();
            ingredientsArea.setEditable(false);
            ingredientsArea.setOpaque(false);
            ingredientsArea.setLineWrap(true);
            ingredientsArea.setWrapStyleWord(true);
            ingredientsArea.setFont(ingredientsArea.getFont().deriveFont(Font.PLAIN, 13f));
            ingredientsArea.setForeground(subtitleColor);

            String ingredientsText = buildIngredientsText(recipe);
            ingredientsArea.setText(ingredientsText.isEmpty() ? "Nenhum ingrediente informado." : ingredientsText);

            JPanel ingPanel = new JPanel();
            ingPanel.setOpaque(false);
            ingPanel.setLayout(new BorderLayout(0, 6));
            ingPanel.add(ingTitle, BorderLayout.NORTH);
            ingPanel.add(ingredientsArea, BorderLayout.CENTER);

            body.add(ingPanel, BorderLayout.CENTER);

            // Rodapé: Botões de ação (Visualizar/Editar/Remover)
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            actions.setOpaque(false);

            // Botão Visualizar
            JButton viewButton = createActionButton("Visualizar", new Color(76, 132, 255), Color.WHITE);
            viewButton.addActionListener(e -> {
                // Abre a tela de detalhes sem fechar a lista atual
                showRecipeDetails(recipe);
            });

            JButton editButton = createActionButton("Editar", new Color(251, 205, 83), Color.BLACK);
            editButton.addActionListener(e -> {
                EditRecipeView editRecipeView = new EditRecipeView(this, controller, recipe);
                editRecipeView.setVisible(true);
                dispose();
                new ListRecipesView(controller).setVisible(true);
            });

            JButton removeButton = createActionButton("Remover", new Color(220, 80, 80), Color.WHITE);
            removeButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Deseja remover a receita: " + StringHandler.capitalize(recipe.getName()) + "?",
                        "Confirmar remoção",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    // Mantém o mesmo fluxo de remoção utilizado anteriormente
                    Boolean success = controller.delete_recipe(recipe);
                    if (Boolean.TRUE.equals(success)) {
                        JOptionPane.showMessageDialog(this, "Receita removida com sucesso!");
                        dispose();
                        new ListRecipesView(controller).setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(this, "Erro ao remover a receita.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            actions.add(viewButton);
            actions.add(editButton);
            actions.add(removeButton);

            card.add(header, BorderLayout.NORTH);
            card.add(body, BorderLayout.CENTER);
            card.add(actions, BorderLayout.SOUTH);
            card.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(cardBorder, 1, true),
                    new EmptyBorder(10, 12, 12, 12)
            ));

            listPanel.add(card);
            listPanel.add(Box.createVerticalStrut(10));
        }

        JScrollPane scrollPane = new JScrollPane(listPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // Botão Adicionar
        JButton addRecipeButton = new JButton("Adicionar Receita");
        stylePrimaryButton(addRecipeButton);
        addRecipeButton.addActionListener(e -> {
            AddRecipeView addRecipeView = new AddRecipeView(this, controller);
            addRecipeView.setVisible(true);
            dispose();
            new ListRecipesView(controller).setVisible(true);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(addRecipeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setSize(720, 520);
        setLocationRelativeTo(null);
    }

    public static void showRecipeDetails(Recipe recipe) {
        if (recipe == null) return;

        String recipeName = recipe.getName();
        Integer numberOfServings = recipe.getNumberOfServings();
        ArrayList<RecipeIngredient> ingredients = recipe.getIngredients();

        StringBuilder ingredientsText = new StringBuilder();

        for( RecipeIngredient ri : ingredients ){
            Integer requiredQuantity = ri.getRequired_quantity();
            String ingredientName = ri.getIngredient_name();
            ingredientsText.append(requiredQuantity).append(" - ").append(ingredientName).append("\n");
        }

        // only visualization configuration and methods
        JDialog dialog = new JDialog((Frame) null, "Detalhes da Receita", true);
        dialog.setLayout(new BorderLayout(12, 12));
        dialog.getRootPane().setBorder(new EmptyBorder(14, 14, 14, 14));

        Color cardBg = new Color(248, 250, 252);
        Color cardBorder = new Color(230, 235, 241);
        Color titleColor = new Color(33, 37, 41);
        Color subtitleColor = new Color(108, 117, 125);

        RoundedPanel card = new RoundedPanel(12);
        card.setLayout(new BorderLayout(12, 12));
        card.setBackground(cardBg);
        card.setBorder(new EmptyBorder(14, 16, 14, 16));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel nameLabel = renderRecipeNameLabel(recipeName);
        JLabel servingsLabel = renderNumberOfServings(numberOfServings);

        header.add(nameLabel, BorderLayout.WEST);
        header.add(servingsLabel, BorderLayout.EAST);


        JPanel body = new JPanel(new BorderLayout(8, 8));
        body.setOpaque(false);

        JLabel ingTitle = new JLabel("Ingredientes");
        ingTitle.setFont(ingTitle.getFont().deriveFont(Font.BOLD, 13f));
        ingTitle.setForeground(titleColor);




        JTextArea ingredientsArea = renderIngredientsArea(ingredientsText.toString());

        JScrollPane ingScroll = new JScrollPane(ingredientsArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        ingScroll.setOpaque(false);
        ingScroll.getViewport().setOpaque(false);
        ingScroll.setBorder(null);
        ingScroll.getVerticalScrollBar().setUnitIncrement(16);

        body.add(ingTitle, BorderLayout.NORTH);
        body.add(ingScroll, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);

        JButton closeButton = createActionButton("Fechar", new Color(240, 240, 240), Color.BLACK);
        closeButton.addActionListener(e -> dialog.dispose());

        actions.add(closeButton);

        card.add(header, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);
        card.add(actions, BorderLayout.SOUTH);

        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(cardBorder, 1, true),
                new EmptyBorder(10, 12, 12, 12)
        ));

        dialog.add(card, BorderLayout.CENTER);
        dialog.pack();
        dialog.setSize(560, 360);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private static JLabel renderRecipeNameLabel(String recipeName) {
        JLabel nameLabel = new JLabel(StringHandler.capitalize(recipeName));
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 18f));
        nameLabel.setForeground(new Color(33, 37, 41));
        return nameLabel;
    }

    private static JLabel renderNumberOfServings(Integer numberOfServings) {
        JLabel servingsLabel = new JLabel(numberOfServings + " refeições");
        servingsLabel.setFont(servingsLabel.getFont().deriveFont(Font.PLAIN, 13f));
        servingsLabel.setForeground(new Color(108, 117, 125));
        return servingsLabel;
    }

    private static  JTextArea renderIngredientsArea(String ingredientsText) {
        JTextArea ingredientsArea = new JTextArea();
        ingredientsArea.setEditable(false);
        ingredientsArea.setOpaque(false);
        ingredientsArea.setLineWrap(true);
        ingredientsArea.setWrapStyleWord(true);
        ingredientsArea.setFont(ingredientsArea.getFont().deriveFont(Font.PLAIN, 13f));
        ingredientsArea.setForeground(new Color(108, 117, 125));
        ingredientsArea.setText(ingredientsText);
        return ingredientsArea;
    }

    private static JButton createActionButton(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFont(b.getFont().deriveFont(Font.BOLD, 12f));
        b.setOpaque(true);
        b.setBorder(new EmptyBorder(8, 14, 8, 14));
        return b;
    }

    private static void stylePrimaryButton(JButton b) {
        b.setBackground(new Color(76, 132, 255));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFont(b.getFont().deriveFont(Font.BOLD, 13f));
        b.setOpaque(true);
        b.setBorder(new EmptyBorder(10, 16, 10, 16));
    }

    // Monta linhas no formato: "<quantidade> - <nome>"
    private static String buildIngredientsText(Recipe recipe) {
        try {
            Object ingredientsObj = recipe.getIngredients();
            if (ingredientsObj instanceof Collection<?>) {
                StringBuilder sb = new StringBuilder();
                for (Object item : (Collection<?>) ingredientsObj) {
                    String name = extractIngredientName(item);
                    String qty  = extractIngredientQuantity(item);
                    String line;
                    if (!qty.isBlank() && !name.isBlank()) {
                        line = qty + " - " + name;
                    } else if (!name.isBlank()) {
                        line = name;
                    } else {
                        line = String.valueOf(item);
                    }

                    if (!line.isBlank()) {
                        sb.append(line).append("\n");
                    }
                }
                return sb.toString().trim();
            }
        } catch (Exception ignored) { }
        return "";
    }

    // Obtém o nome do ingrediente por reflexão em getters comuns
    private static String extractIngredientName(Object item) {
        if (item == null) return "";

        // 1) item.getIngredient_name()
        try {
            var m = item.getClass().getMethod("getIngredient_name");
            Object v = m.invoke(item);
            if (v != null) return v.toString();
        } catch (Exception ignored) { }

        // 2) item.getName()
        try {
            var m = item.getClass().getMethod("getName");
            Object v = m.invoke(item);
            if (v != null) return v.toString();
        } catch (Exception ignored) { }

        // 3) item.getNome()
        try {
            var m = item.getClass().getMethod("getIngredient_name");
            Object v = m.invoke(item);
            if (v != null) return v.toString();
        } catch (Exception ignored) { }

        // 4) item.getIngredient().getName()/getNome()
        try {
            var mIng = item.getClass().getMethod("getIngredient");
            Object ing = mIng.invoke(item);
            if (ing != null) {
                try {
                    var mName = ing.getClass().getMethod("getName");
                    Object v = mName.invoke(ing);
                    if (v != null) return v.toString();
                } catch (Exception ignored) { }
                try {
                    var mNome = ing.getClass().getMethod("getNome");
                    Object v = mNome.invoke(ing);
                    if (v != null) return v.toString();
                } catch (Exception ignored) { }
            }
        } catch (Exception ignored) { }

        return "";
    }

    // Obtém a quantidade por reflexão em getters comuns
    private static String extractIngredientQuantity(Object item) {
        if (item == null) return "";

        try {
            var m = item.getClass().getMethod("getRequired_quantity");
            Object v = m.invoke(item);
            if (v != null) {
                String s = v.toString().trim();
                if (!s.isEmpty()) return s;
            }
        } catch (Exception ignored) { }

        return "";
    }

    // Painel arredondado simples para visual moderno
    static class RoundedPanel extends JPanel {
        private final int arc;

        public RoundedPanel(int arc) {
            this.arc = arc;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
