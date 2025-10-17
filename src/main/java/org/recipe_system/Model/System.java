package org.recipe_system.Model;

import java.util.ArrayList;
import java.util.List;

public class System {
    // Relação "Contains" 1 para N com Recipe

    // Relação "Contains" 1 para N com Ingredient
    private List<Ingredient> ingredients;

    public System() {
        this.ingredients = new ArrayList<Ingredient>();
    }

    // Métodos que provavelmente chamariam a interface gráfica

    public void showRegisterRecipe() {
    }

    public void showRegisterIngredient() {
    }

    public void showRecipes() {
    }

    public void showIngredients() {
    }

    public void showEditRecipe() {
    }

    public void showEditIngredient() {
    }

    public void showQueryRecipe() {
    }

    public void showQueries() {
    }

    // Getters para acesso aos dados centrais


    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}
