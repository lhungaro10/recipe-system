// src/main/java/org/recipe_system/views/RecipeViews/SelectRecipeIngredientsView.java
package org.recipe_system.views.RecipeViews;

import org.recipe_system.Controller.Controller;
import org.recipe_system.Model.Ingredient; // Supondo que você tenha uma classe Ingredient
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SelectRecipeIngredientsView extends JDialog {
    private List<Ingredient> selectedIngredients;

    public SelectRecipeIngredientsView(Frame parent, Controller controller) {
        super(parent, "Selecionar Ingredientes", true); // true torna o diálogo modal
        // ... aqui vai a construção da sua UI (tabela de ingredientes, checkboxes, etc.)

        JButton confirmButton = new JButton("Confirmar");
        confirmButton.addActionListener(e -> {
            // Lógica para coletar os ingredientes selecionados da sua UI
            this.selectedIngredients = new ArrayList<>(); // Preencha esta lista
            // Ex: this.selectedIngredients.add(new Ingredient("Farinha"));
            dispose(); // Fecha o diálogo
        });

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(e -> {
            this.selectedIngredients = null; // Indica que a operação foi cancelada
            dispose(); // Fecha o diálogo
        });

        // Adiciona os botões e outros componentes ao layout
        // ...
        pack();
        setLocationRelativeTo(parent);
    }

    /**
     * Retorna a lista de ingredientes selecionados pelo usuário.
     * Retorna null se o usuário cancelou a seleção.
     */
    public List<Ingredient> getSelectedIngredients() {
        return selectedIngredients;
    }
}