package org.recipe_system.Controller;

import org.recipe_system.Catalog.IngredientCatalog;
import org.recipe_system.Model.Ingredient;
import org.recipe_system.Model.Recipe;
import org.recipe_system.Model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    private User currentUser;

    IngredientCatalog ingredientCatalog;

    public Controller() {
        this.ingredientCatalog = new IngredientCatalog();
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
        List<Ingredient> ingredients = this.ingredientCatalog.getIngredients();
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

        List<Ingredient> ingredients = this.ingredientCatalog.getIngredients();

        return this.ingredientCatalog.editIngredient(ingredient, name, qtd_in_stock);
    }

    public Boolean removeIngredient(Ingredient ingredient) {
        if(!this.ingredientCatalog.verifyIngredientExists(ingredient)){
            return false;
        }
        return this.ingredientCatalog.removeIngredient(ingredient);
    }

    public ArrayList<Ingredient> listIngredients() {
        return this.ingredientCatalog.getIngredients();
    }

    private Integer getNumberInStock(Ingredient ingredient) {
        return this.ingredientCatalog.getQtdInStock(ingredient);
    }

//    ================ Recipe Methods ===================
    public Boolean registerRecipe(String name, Integer number_of_servings, ArrayList<Ingredient> ingredients, ArrayList<Integer> qtds) {
        logger.debug("\nTentando registrar ingrediente: \nnome='{}', \nnumber_of_servings={}, \ningredients={}, \nqtds={}", name, number_of_servings, ingredients, qtds);

        System.out.println();
        return null;
    }

    private Boolean validateCounts(ArrayList<Ingredient> ingredients, Integer qtds) {
        return null;
    }



    private Boolean validateNumber(Integer qtd_in_stock) {
        return qtd_in_stock >= 0;
    }

    // o retorno foi negado aqui para o nome da função fazer sentido.
    private Boolean validateName(String name){
        return !name.isEmpty();
    }

    public void addRecipe(Recipe newRecipe) {
    }
}
