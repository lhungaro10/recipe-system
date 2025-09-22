package org.recipe_system.Model;

public class RecipeQuery {
    private int target_number_of_servings;
    private boolean made;

    private Recipe recipe;

    public RecipeQuery(Recipe recipe, int target_number_of_servings) {
        this.recipe = recipe;
        this.target_number_of_servings = target_number_of_servings;
        this.made = false;
    }

    public boolean consume_ingredients() {

        return false;
    }

    public boolean hasRequiredIngredients() {
        return false;
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
}
