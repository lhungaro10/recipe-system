package org.recipe_system.Catalog;

import org.recipe_system.Model.Recipe;
import org.recipe_system.Utils.FilePersistence;

import java.util.ArrayList;

public class RecipeCatalog {
    private FilePersistence<Recipe> recipesPersistence;
    private ArrayList<Recipe> recipes;

    public RecipeCatalog(){
        this.recipesPersistence = new FilePersistence<Recipe>("recipe.data");
        this.recipes = recipesPersistence.readFromFile().orElse(null);
        if(this.recipes == null){
            this.recipes = new ArrayList<Recipe>();
        }
    }

}
