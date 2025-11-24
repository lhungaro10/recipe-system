// java
// File: `src/main/java/org/recipe_system/views/QueryRecipeView/ListRecipeQueriesView.java`

package org.recipe_system.views.QueryRecipeView;

import org.recipe_system.Controller.Controller;
import org.recipe_system.Model.RecipeQuery;
import org.recipe_system.views.RecipeViews.ListRecipesView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

public class ListRecipeQueriesView extends JFrame {

    private final Controller controller;

    public ListRecipeQueriesView(Controller controller) {
        this.controller = controller;

        setTitle("Lista de Execuções de Receita");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(new EmptyBorder(14, 14, 14, 14));

        // Cores iguais/parecidas com ListRecipesView
        Color cardBg = new Color(248, 250, 252);
        Color cardBorder = new Color(230, 235, 241);
        Color titleColor = new Color(33, 37, 41);
        Color subtitleColor = new Color(108, 117, 125);

        // Painel de listagem com cards
        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        ArrayList<RecipeQuery> queries = controller.getAllRecipeQueries();
        for (RecipeQuery query : queries) {
            ListRecipesView.RoundedPanel card = new ListRecipesView.RoundedPanel(14);
            card.setLayout(new BorderLayout(12, 8));
            card.setBackground(cardBg);
            card.setBorder(new EmptyBorder(14, 16, 14, 16));

            // Cabeçalho com "título" e info auxiliar
            JPanel header = new JPanel(new BorderLayout(8, 8));
            header.setOpaque(false);

            JLabel titleLabel = new JLabel(new StringBuilder("Planejamento da receita: ").append(query.getRecipe().getName()).toString());
            titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
            titleLabel.setForeground(titleColor);

            JLabel infoLabel = new JLabel(getQuerySubtitle(query));
            infoLabel.setFont(infoLabel.getFont().deriveFont(Font.PLAIN, 13f));
            infoLabel.setForeground(subtitleColor);

            header.add(titleLabel, BorderLayout.WEST);
            header.add(infoLabel, BorderLayout.EAST);

            // Corpo: informações adicionais (texto simples)
            JPanel body = new JPanel(new BorderLayout());
            body.setOpaque(false);

            JTextArea detailsArea = new JTextArea();
            detailsArea.setEditable(false);
            detailsArea.setOpaque(false);
            detailsArea.setLineWrap(true);
            detailsArea.setWrapStyleWord(true);
            detailsArea.setFont(detailsArea.getFont().deriveFont(Font.PLAIN, 13f));
            detailsArea.setForeground(subtitleColor);
            detailsArea.setText(buildQueryDetails(query));

            body.add(detailsArea, BorderLayout.CENTER);

            // Rodapé: ações
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            actions.setOpaque(false);

            JButton viewButton = createActionButton("Visualizar", new Color(76, 132, 255), Color.WHITE);
            viewButton.addActionListener(e -> {
                JOptionPane.showMessageDialog(
                        this,
                        buildQueryDetails(query),
                        "Detalhes da Execução",
                        JOptionPane.INFORMATION_MESSAGE
                );
            });

            actions.add(viewButton);

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

        JScrollPane scrollPane = new JScrollPane(
                listPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // Rodapé com botão Fechar
        JButton closeButton = new JButton("Fechar");
        styleSecondaryButton(closeButton);
        closeButton.addActionListener(e -> dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        bottomPanel.add(closeButton);

        add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setSize(720, 520);
        setLocationRelativeTo(null);
    }


    // Subtítulo: por ex. "X pessoas" ou data
    private String getQuerySubtitle(RecipeQuery query) {
        try {
            // Exemplo: se tiver getTargetServings()
            var m = query.getClass().getMethod("getTargetServings");
            Object v = m.invoke(query);
            if (v != null) {
                return v + " pessoa(s)";
            }
        } catch (Exception ignored) { }

        return "";
    }

    // Texto de detalhes exibido no corpo/toString do dialog
    private String buildQueryDetails(RecipeQuery query) {
        StringBuilder sb = new StringBuilder();

        sb.append("Detalhes: \n")
            .append("Número de pessoas: ").append(query.getTarget_number_of_servings()).append("\n")
        .append("Feito: ").append(query.isMade() ? "Sim" : "Não").append("\n");


        return sb.toString().trim();
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

    private static void styleSecondaryButton(JButton b) {
        b.setBackground(new Color(240, 240, 240));
        b.setForeground(Color.BLACK);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFont(b.getFont().deriveFont(Font.BOLD, 13f));
        b.setOpaque(true);
        b.setBorder(new EmptyBorder(10, 16, 10, 16));
    }
}
