package org.recipe_system.Catalog;

import org.recipe_system.Model.Ingredient;
import org.recipe_system.Utils.FilePersistence;

import java.util.ArrayList;

public class IngredientCatalog {
    private FilePersistence<Ingredient> ingredientPersistence;
    private ArrayList<Ingredient> ingredients;

    public IngredientCatalog(){
        this.ingredientPersistence = new FilePersistence<>("ingredient.data");
        this.ingredients = ingredientPersistence.readFromFile().orElse(null);
        if(this.ingredients == null){
            this.ingredients = new ArrayList<Ingredient>();
        }
    }

    public Integer getQtdInStock(Ingredient ingredient) {
        if(this.ingredients == null) return 0;
        for (Ingredient item : this.ingredients) {
            if (item.getName().equals(ingredient.getName())) {
                return item.getQtd_in_stock();
            }
        }
        return 0;
    }

    public Boolean insertIngredient(Ingredient ingredient) {
        try {
            ArrayList<Ingredient> tempList = this.ingredientPersistence.readFromFile().orElse(new ArrayList<Ingredient>());

            Boolean found = false;
            for(Ingredient item : tempList){
                if(item.getName().equals(ingredient.getName())){
                    item.setName(ingredient.getName());
                    item.setQtd_in_stock(ingredient.getQtd_in_stock());
                    found = true;
                    break;
                }
            }
            if (!found) {
                tempList.add(ingredient); // Adiciona novo ingrediente se não existir
            }
            this.setIngredients(tempList);
            this.ingredientPersistence.saveToFile(this.ingredients);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Boolean editIngredient(Ingredient ingredient, String name, Integer qtdInStock) {
        try {
            ArrayList<Ingredient> tempList = this.ingredientPersistence.readFromFile().orElse(new ArrayList<Ingredient>());

            Boolean found = false;
            for(Ingredient item : tempList){
                if(item.getName().equals(ingredient.getName())){
                    item.setName(name);
                    item.setQtd_in_stock(qtdInStock);
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false; // Adiciona novo ingrediente se não existir
            }
            this.setIngredients(tempList);
            this.ingredientPersistence.saveToFile(this.ingredients);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Boolean deleteIngredient(Ingredient ingredient) {
        try {
            ArrayList<Ingredient> tempList = this.ingredientPersistence.readFromFile().orElse(new ArrayList<Ingredient>());

            Ingredient toRemove = null;
            for(Ingredient item : tempList){
                if(item.getName().equals(ingredient.getName())){
                    toRemove = item;
                    break;
                }
            }
            if (toRemove != null) {
                tempList.remove(toRemove); // Remove o ingrediente se existir
            } else {
                return false; // Ingrediente não encontrado
            }
            this.setIngredients(tempList);
            this.ingredientPersistence.saveToFile(this.ingredients);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Boolean verifyIngredientExists(Ingredient ingredient) {
        ArrayList<Ingredient> tempList = this.ingredientPersistence.readFromFile().orElse(new ArrayList<Ingredient>());

        for(Ingredient item : tempList){
            if(item.getName().equals(ingredient.getName())){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Ingredient> getAllIngredients() {
        ArrayList<Ingredient> list = this.ingredientPersistence.readFromFile().orElse(new ArrayList<>());
        return list;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
