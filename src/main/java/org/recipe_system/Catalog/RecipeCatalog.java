// Em src/main/java/org/recipe_system/Catalog/RecipeCatalog.java
package org.recipe_system.Catalog;

import org.recipe_system.Model.Recipe;
import org.recipe_system.Utils.FilePersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Optional;

public class RecipeCatalog {
    private static final Logger logger = LoggerFactory.getLogger(RecipeCatalog.class);
    private ArrayList<Recipe> recipes;
    private final FilePersistence<Recipe> recipesPersistence;

    public RecipeCatalog() {
        this.recipesPersistence = new FilePersistence<>("recipe.data");
        this.recipes = this.recipesPersistence.readFromFile().orElse(new ArrayList<>());
    }

    public Boolean insertRecipe(Recipe recipe) {
        logger.info("Inserting recipe " + recipe.getName());
        try {
            this.recipes.add(recipe);
            this.recipesPersistence.saveToFile(this.recipes);
            logger.info("Recipe inserted successfully");
            return true;
        } catch (Exception e) {
            logger.error("Error on insert recipe: " + e.getMessage());
            return false;
        }
    }

    public Boolean editRecipe(Recipe recipe) {
        logger.info("Editing recipe with ID: " + recipe.getId());
        try {
            ArrayList<Recipe> tempList = this.recipesPersistence.readFromFile().orElse(new ArrayList<>());
            boolean found = false;
            for (int i = 0; i < tempList.size(); i++) {
                if (tempList.get(i).getId().equals(recipe.getId())) {
                    tempList.set(i, recipe);
                    found = true;
                    break;
                }
            }
            if (!found) {
                logger.warn("Recipe with ID {} not found for editing.", recipe.getId());
                return false;
            }
            this.setRecipes(tempList);
            this.recipesPersistence.saveToFile(this.recipes);
            logger.info("Recipe edited successfully");
            return true;
        } catch (Exception e) {
            logger.error("Error on edit recipe: " + e.getMessage());
            return false;
        }
    }

    public Boolean removeRecipe(Long id) {
        logger.info("Removing recipe with ID: " + id);
        try {
            ArrayList<Recipe> tempList = this.recipesPersistence.readFromFile().orElse(new ArrayList<>());
            boolean removed = tempList.removeIf(recipe -> recipe.getId().equals(id));

            if (!removed) {
                logger.warn("Recipe with ID {} not found for removal.", id);
                return false;
            }
            this.setRecipes(tempList);
            this.recipesPersistence.saveToFile(this.recipes);
            logger.info("Recipe removed successfully");
            return true;
        } catch (Exception e) {
            logger.error("Error on remove recipe: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Recipe> getAllRecipes() {
        return this.recipes;
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    public Recipe getRecipeByName(String name) {
        Optional<Recipe> recipe = this.recipes.stream().filter(r -> r.getName().equalsIgnoreCase(name)).findFirst();
        return recipe.orElse(null);
    }
}