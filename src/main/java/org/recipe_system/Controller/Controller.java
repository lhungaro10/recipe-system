package org.recipe_system.Controller;

import org.recipe_system.Catalog.IngredientCatalog;
import org.recipe_system.Model.Ingredient;
import org.recipe_system.Model.User;

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

    public ArrayList<Ingredient> listIngredients() {
        return this.ingredientCatalog.getIngredients();
    }

    private Integer getNumberInStock(Ingredient ingredient) {
        return this.ingredientCatalog.getQtdInStock(ingredient);
    }

    private Boolean validateNumber(Integer qtd_in_stock) {
        return qtd_in_stock >= 0;
    }

    // o retorno foi negado aqui para o nome da função fazer sentido.
    private Boolean validateName(String name){
        return !name.isEmpty();
    }

}
