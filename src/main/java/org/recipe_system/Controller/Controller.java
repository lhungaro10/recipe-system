package org.recipe_system.Controller;

import org.recipe_system.Catalog.IngredientCatalog;
import org.recipe_system.Catalog.RecipeCatalog;
import org.recipe_system.Catalog.RecipeQueryCatalog;
import org.recipe_system.Model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.System;
import java.util.ArrayList;
import java.util.List;



public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    private User currentUser;

    IngredientCatalog ingredientCatalog;
    RecipeCatalog recipeCatalog;
    RecipeQueryCatalog recipeQueryCatalog;

    public Controller() {
        this.ingredientCatalog = new IngredientCatalog();
        this.recipeCatalog = new RecipeCatalog();
        this.recipeQueryCatalog = new RecipeQueryCatalog();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

//    ================ Ingredient Methods ===================
    public Boolean registerIngredient(String name, int qtd_in_stock) {
        System.out.println(this.validateName(name));
        System.out.println(this.validateNumber(qtd_in_stock));
        if(!this.validateName(name) || !this.validateNumber(qtd_in_stock)){
            return false;
        }
        List<Ingredient> ingredients = this.ingredientCatalog.getAllIngredients();
        Ingredient ingredient = new Ingredient(name.trim().toLowerCase(), qtd_in_stock);

        Integer numberInStock = this.getNumberInStock(ingredient);

        if (numberInStock > 0) {
            ingredient.setQtd_in_stock(numberInStock + qtd_in_stock);
        }

        return this.ingredientCatalog.insertIngredient(ingredient);
    }

    public Boolean editIngredient(Ingredient ingredient, String name, int qtd_in_stock) {
        if(!this.validateName(name) || !this.validateNumber(qtd_in_stock)){
            return false;
        }

        List<Ingredient> ingredients = this.ingredientCatalog.getAllIngredients();

        return this.ingredientCatalog.editIngredient(ingredient, name, qtd_in_stock);
    }

    public Boolean deleteIngredient(Ingredient ingredient) {
        Boolean flag = this.any_recipe_use_ingredient(ingredient);
        logger.debug("flag any recipe use ingredient: {}", flag);
        if(flag){
            return false;
        }

        return this.ingredientCatalog.deleteIngredient(ingredient);
    }

    public Boolean any_recipe_use_ingredient(Ingredient ingredient){
        ArrayList<Recipe> recipes = this.recipeCatalog.getAllRecipes();
        for(Recipe recipe: recipes){
            Boolean hasIngredient = recipe.hasIngredient(ingredient);
            if(hasIngredient){return true;}
        }
        return false;
    }

    public ArrayList<Ingredient> getAllIngredients() {
        return this.ingredientCatalog.getAllIngredients();
    }

    private Integer getNumberInStock(Ingredient ingredient) {
        return this.ingredientCatalog.getQtdInStock(ingredient);
    }

//    ================ Recipe Methods ===================
    public Boolean registerRecipe(String name, Integer number_of_servings, ArrayList<Ingredient> ingredients, ArrayList<Integer> qtds) {
        logger.info("Init register recipe");
        if(!this.validateCounts(ingredients, qtds) || !this.validateName(name) || !this.validateNumber(number_of_servings)) {
           return Boolean.FALSE;
        }
        logger.info("After first recipe validation");

        Recipe newRecipe = new Recipe(name, number_of_servings);

        logger.info("After instantiate recipe");

        for(int i = 0; i < ingredients.size(); i++) {
            if(!newRecipe.validateNumber(qtds.get(i))) {
                logger.error("Falha ao validar quantidade do ingrediente: {}", ingredients.get(i).getName());
                return Boolean.FALSE;
            }
            if(!newRecipe.add_ingredient(ingredients.get(i),qtds.get(i))) {
                logger.error("Falha ao adicionar quantidade do ingrediente: {}", ingredients.get(i).getName());
                return Boolean.FALSE;
            }
        }
        logger.info("After instantiate add RecipeIngredients");

        return this.recipeCatalog.insertRecipe(newRecipe);
    }

    public Boolean editRecipe(Recipe recipe, String name, Integer number_of_servings, ArrayList<Ingredient> ingredients, ArrayList<Integer> qtds) {
        logger.info("Init edit recipe");
        if(!this.validateCounts(ingredients, qtds) || !this.validateName(name) || !this.validateNumber(number_of_servings)) {
           return Boolean.FALSE;
        }
        logger.info("After first recipe validation");

        recipe.setName(name);
        recipe.setNumber_of_servings(number_of_servings);


        for(int i = 0; i < ingredients.size(); i++) {
            RecipeIngredient ri = recipe.getRecipeIngredient(ingredients.get(i));

            if(ri != null) {
                if (!ri.validateNumber(qtds.get(i))) continue;
                ri.setRequired_quantity(qtds.get(i));
                continue;
            }

            recipe.add_ingredient(ingredients.get(i),qtds.get(i));
        }

        logger.info("After add RecipeIngredients");

        recipe.remove_ingredient_not_in(this.getRecipeIngredientsToKeep(ingredients, qtds));

        this.recipeCatalog.editRecipe(recipe);



        return Boolean.TRUE;
    }

    public ArrayList<Recipe> getAllRecipes(){
        return this.recipeCatalog.getAllRecipes();
    }


    public Boolean delete_recipe(Recipe recipe){
        return this.recipeCatalog.delete_recipe(recipe);
    }

    public Boolean consultRecipe(Recipe recipe, Integer target, QueryListeners listener){
        RecipeQuery query = new RecipeQuery(recipe, target);

        Boolean canMake = query.hasRequiredIngredients();

        if (canMake) {
            Boolean confirm = listener.onConfirmQuery();
            if (confirm) {
                query.setMade(true);
                query.consume_ingredients();
                this.recipeQueryCatalog.insertRecipeQuery(query);
                return Boolean.TRUE;
            }
        } else {
            query.setMade(false);
            this.recipeQueryCatalog.insertRecipeQuery(query);
            listener.onInsufficientIngredients();
        }
        return Boolean.FALSE;
    }

    public ArrayList<RecipeQuery> getAllRecipeQueries(){
        return this.recipeQueryCatalog.getAllRecipeQueries();
    }

    private Boolean validateCounts(ArrayList<Ingredient> ingredients, ArrayList<Integer> qtds) {
        return ingredients.size() == qtds.size();
    }



    private Boolean validateNumber(Integer qtd_in_stock) {
        return qtd_in_stock >= 0;
    }

    // o retorno foi negado aqui para o nome da função fazer sentido.
    private Boolean validateName(String name){
        return !name.isEmpty();
    }

    private ArrayList<RecipeIngredient> getRecipeIngredientsToKeep(ArrayList<Ingredient> ingredients, ArrayList<Integer> qtds) {
        ArrayList<RecipeIngredient> recipeIngredients = new ArrayList<RecipeIngredient>();
        for(int i = 0; i < ingredients.size(); i++) {
            RecipeIngredient recipeIngredient = new RecipeIngredient(ingredients.get(i).getName(), qtds.get(i));
            recipeIngredients.add(recipeIngredient);
        }
        return recipeIngredients;
    }

    public Boolean verifyRecipeAlreadyExists(String Name){
        return this.recipeCatalog.getRecipeByName(Name) != null;
    }
}
