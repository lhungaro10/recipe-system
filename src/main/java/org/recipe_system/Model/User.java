package org.recipe_system.Model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private List<Recipe> registeredRecipes;

    public User() {
        this.registeredRecipes = new ArrayList<Recipe>();
    }

    public void registerRecipe(Recipe recipe) {
        this.registeredRecipes.add(recipe);
    }

    public List<Recipe> getRegisteredRecipes() {
        return registeredRecipes;
    }
}
