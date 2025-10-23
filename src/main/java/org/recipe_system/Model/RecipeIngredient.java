package org.recipe_system.Model;

import java.io.Serializable;

public class RecipeIngredient implements Serializable {
    private String ingredient_name;
    private int required_quantity;

    public RecipeIngredient(String ingredient, int required_quantity) {
        this.ingredient_name = ingredient;
        this.required_quantity = required_quantity;
    }

    // Getters e Setters
    public String getIngredient_name() {
        return ingredient_name;
    }

    public void setIngredient_name(String ingredient_name) {
        this.ingredient_name = ingredient_name;
    }

    public int getRequired_quantity() {
        return required_quantity;
    }

    public void setRequired_quantity(int required_quantity) {
        this.required_quantity = required_quantity;
    }
}
