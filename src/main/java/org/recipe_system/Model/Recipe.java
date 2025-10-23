package org.recipe_system.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Serializable {
    private String name;
    private int number_of_servings;

    private List<RecipeIngredient> recipeIngredients;

    public Recipe(String name, int number_of_servings) {
        this.name = name;
        this.number_of_servings = number_of_servings;
        this.recipeIngredients = new ArrayList<RecipeIngredient>();
    }

    public boolean add_ingredient(Ingredient ingredient, int qtd) {

        RecipeIngredient newRecipeIngredient = new RecipeIngredient(ingredient.getName(), qtd);
        return this.recipeIngredients.add(newRecipeIngredient);
    }

    public Boolean validateNumber(Integer qtd_in_stock) {
        return qtd_in_stock >= 0;
    }

    public boolean remove_ingredient(RecipeIngredient recipeIngredient) {

        return this.recipeIngredients.remove(recipeIngredient);
    }

    public boolean set_ingredient(RecipeIngredient oldRecipeIngredient, int qtd) {

        if (this.recipeIngredients.contains(oldRecipeIngredient)) {
            oldRecipeIngredient.setRequired_quantity(qtd);
            return true;
        }
        return false;
    }

    // Getters e Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber_of_servings() {
        return number_of_servings;
    }

    public void setNumber_of_servings(int number_of_servings) {
        this.number_of_servings = number_of_servings;
    }

    public List<RecipeIngredient> getRecipeIngredients() {
        return recipeIngredients;
    }
}
