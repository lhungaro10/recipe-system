package org.recipe_system.Catalog;

import org.recipe_system.Model.Recipe;
import org.recipe_system.Utils.FilePersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class RecipeCatalog {
    private static final Logger logger = LoggerFactory.getLogger(RecipeCatalog.class);
    private FilePersistence<Recipe> recipesPersistence;
    private ArrayList<Recipe> recipes;

    public RecipeCatalog(){
        this.recipesPersistence = new FilePersistence<Recipe>("recipe.data");
        this.recipes = recipesPersistence.readFromFile().orElse(null);
        if(this.recipes == null){
            this.recipes = new ArrayList<Recipe>();
        }
    }

    public Boolean insertRecipe(Recipe recipe) {
        logger.info("Inserting recipe " + recipe.getName());
        try {
            ArrayList<Recipe> tempList = this.recipesPersistence.readFromFile().orElse(new ArrayList<Recipe>());

            tempList.add(recipe);
            this.setRecipes(tempList);
            this.recipesPersistence.saveToFile(this.recipes);
            logger.info("recipe inserted successfully");
            return true;
        } catch (Exception e) {
            logger.error("Error on insert recipe: " + e.getMessage());
            return false;
        }
    }

    public Recipe getRecipeByName(String name) {
        for (Recipe recipe : this.recipes) {
            if (recipe.getName().equalsIgnoreCase(name)) {
                return recipe;
            }
        }
        return null;
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }
}
