package org.recipe_system.Model;

import org.recipe_system.Catalog.RecipeQueryCatalog;

import java.io.Serializable;

public class RecipeQuery implements Serializable {
    private int target_number_of_servings;
    private boolean made;

    private Recipe recipe;

    public RecipeQuery(Recipe recipe, int target_number_of_servings) {
        this.recipe = recipe;
        this.target_number_of_servings = target_number_of_servings;
        this.made = false;
    }

    public boolean consume_ingredients() {
        RecipeQueryCatalog recipeQueryCatalog = new RecipeQueryCatalog();
        Boolean success = recipeQueryCatalog.consumeIngredients(this);

        return false;
    }

    public Boolean hasRequiredIngredients() {
        RecipeQueryCatalog recipeQueryCatalog = new RecipeQueryCatalog();

        return recipeQueryCatalog.hasRequiredIngredients(this);

    }


    public int getTarget_number_of_servings() {
        return target_number_of_servings;
    }

    public boolean isMade() {
        return made;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setTarget_number_of_servings(int target_number_of_servings) {
        this.target_number_of_servings = target_number_of_servings;
    }

    public void setMade(boolean made) {
        this.made = made;
    }
}
