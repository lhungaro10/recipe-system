package org.recipe_system.Controller;

import org.recipe_system.Catalog.IngredientCatalog;
import org.recipe_system.Model.Ingredient;
import org.recipe_system.Model.User;
import org.recipe_system.Model.System;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    private User currentUser;

    IngredientCatalog ingredientCatalog;

    public Controller() {
        this.ingredientCatalog = new IngredientCatalog();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }


    public Boolean registerIngredient(String name, int qtd_in_stock) {
        List<Ingredient> ingredients = this.ingredientCatalog.getIngredients();
        Ingredient ingredient = new Ingredient(name, qtd_in_stock);

        Integer numberInStock = this.validateNumberInStock(ingredient);

        if (numberInStock == 0) {
            return this.ingredientCatalog.insertIngredient(ingredient);
        } else {
            ingredient.setQtd_in_stock(numberInStock + qtd_in_stock);
        }

        return this.ingredientCatalog.insertIngredient(ingredient);
    }

    public Boolean editIngredient(Ingredient ingredient, String name, int qtd_in_stock) {
        List<Ingredient> ingredients = this.ingredientCatalog.getIngredients();

        Integer numberInStock = this.validateNumberInStock(new Ingredient(name, qtd_in_stock));

        if (numberInStock == 0) {
            return false;
        }

        return this.ingredientCatalog.editIngredient(ingredient, name, qtd_in_stock);
    }

    public ArrayList<Ingredient> listIngredients() {
        return this.ingredientCatalog.getIngredients();
    }

    private Integer validateNumberInStock(Ingredient ingredient) {
        return this.ingredientCatalog.getQtdInStock(ingredient);
    }

}
