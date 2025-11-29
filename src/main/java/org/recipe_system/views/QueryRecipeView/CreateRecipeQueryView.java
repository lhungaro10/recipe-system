
package org.recipe_system.views.QueryRecipeView;

import org.recipe_system.Controller.Controller;
import org.recipe_system.Controller.QueryListeners;
import org.recipe_system.Model.Recipe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CreateRecipeQueryView extends JPanel{

    /**
     * Mostra um diálogo que exibe o nome da receita e um input para
     * informar para quantas pessoas a receita será feita.
     * Não retorna valor.
     */
    public CreateRecipeQueryView(Controller controller, Recipe recipe) {
        if (recipe == null) return;

        // Cores básicas para alinhar com estilo existente
        Color cardBg = new Color(248, 250, 252);
        Color titleColor = new Color(33, 37, 41);
        Color subtitleColor = new Color(108, 117, 125);

        JDialog dialog = new JDialog((Frame) null, "Quantidade de Pessoas", true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setLayout(new BorderLayout(12, 12));
        dialog.getRootPane().setBorder(new EmptyBorder(14, 14, 14, 14));

        JPanel card = new JPanel(new BorderLayout(12, 12));
        card.setBackground(cardBg);
        card.setBorder(new EmptyBorder(12, 12, 12, 12));

        // Cabeçalho com nome da receita
        JLabel nameLabel = new JLabel(recipe.getName());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 16f));
        nameLabel.setForeground(titleColor);

        JLabel hintLabel = new JLabel("Informe para quantas pessoas:");
        hintLabel.setFont(hintLabel.getFont().deriveFont(Font.PLAIN, 12f));
        hintLabel.setForeground(subtitleColor);

        JPanel header = new JPanel(new BorderLayout(6, 6));
        header.setOpaque(false);
        header.add(nameLabel, BorderLayout.NORTH);
        header.add(hintLabel, BorderLayout.SOUTH);

        // Input para número de pessoas (spinner)
        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 1000, 1);
        JSpinner peopleSpinner = new JSpinner(model);
        Dimension spinnerSize = new Dimension(120, peopleSpinner.getPreferredSize().height);
        peopleSpinner.setPreferredSize(spinnerSize);

        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT));
        center.setOpaque(false);
        center.add(peopleSpinner);

        // Ações
        JButton confirmBtn = new JButton("Confirmar");
        confirmBtn.addActionListener(e -> {

            int target = (Integer) peopleSpinner.getValue();
            controller.consultRecipe(recipe, target, new QueryListeners() {
                @Override
                public Boolean onConfirmQuery() {
                    int choice = JOptionPane.showConfirmDialog(
                            dialog,
                            "Deseja executar a receita \"" + recipe.getName() + "\" para " + target + " pessoa(s)?",
                            "Confirmar execução",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );
                    return choice == JOptionPane.YES_OPTION;
                }

                @Override
                public void onInsufficientIngredients() {
                    JOptionPane.showMessageDialog(
                            dialog,
                            "Ingredientes insuficientes para executar a receita.",
                            "Ingredientes insuficientes",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            });
            dialog.dispose();
        });

        JButton cancelBtn = new JButton("Cancelar");
        cancelBtn.addActionListener(e -> dialog.dispose());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        actions.add(confirmBtn);
        actions.add(cancelBtn);

        card.add(header, BorderLayout.NORTH);
        card.add(center, BorderLayout.CENTER);
        card.add(actions, BorderLayout.SOUTH);

        dialog.add(card, BorderLayout.CENTER);
        dialog.pack();
        dialog.setSize(420, dialog.getHeight());
        dialog.setLocationRelativeTo(null);

        // Garantir foco no spinner ao abrir
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                SwingUtilities.invokeLater(() -> peopleSpinner.requestFocusInWindow());
            }
        });

        dialog.setVisible(true);
    }
}
