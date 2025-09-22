package org.recipe_system.Model;

public class RecipeIngredient {
    private Ingredient ingredient;
    private int required_quantity;

    public RecipeIngredient(Ingredient ingredient, int required_quantity) {
        this.ingredient = ingredient;
        this.required_quantity = required_quantity;
    }

    // Getters e Setters
    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public int getRequired_quantity() {
        return required_quantity;
    }

    public void setRequired_quantity(int required_quantity) {
        this.required_quantity = required_quantity;
    }
}
