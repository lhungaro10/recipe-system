package org.recipe_system.Catalog;

import org.recipe_system.Model.Ingredient;
import org.recipe_system.Model.Recipe;
import org.recipe_system.Model.RecipeIngredient;
import org.recipe_system.Model.RecipeQuery;
import org.recipe_system.Utils.FilePersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class RecipeQueryCatalog {
    private static final Logger logger = LoggerFactory.getLogger(RecipeQueryCatalog.class);

    // Pasta "data" no diretório do projeto
    private static final Path DATA_DIR  = Paths.get(System.getProperty("user.dir"), "data")
            .toAbsolutePath()
            .normalize();
    private static final Path DATA_PATH = DATA_DIR.resolve("query.data");


    private ArrayList<RecipeQuery> recipesquery;
    private final FilePersistence<RecipeQuery> recipesQueryPersistence;
    private IngredientCatalog ingredientCatalog;

    public RecipeQueryCatalog() {
        ensureDataDir();
        this.ingredientCatalog = new IngredientCatalog();
        this.recipesQueryPersistence = new FilePersistence<RecipeQuery>(DATA_PATH.toString());
        this.recipesquery = this.recipesQueryPersistence.readFromFile().orElse(new ArrayList<>());
        logger.info("Recipes Query carregadas: {} de {}", this.recipesquery.size(), DATA_PATH);
    }



    private void ensureDataDir() {
        try {
            Files.createDirectories(DATA_DIR);
        } catch (IOException e) {
            logger.error("Falha ao criar diretório de dados: {}", DATA_DIR, e);
        }
    }

    public Boolean insertRecipeQuery(RecipeQuery recipequery) {
        logger.info("Inserting recipe query {}", recipequery.getRecipe().getName());
        try {
            this.recipesquery.add(recipequery);
            logger.debug("recipesquery {}", this.recipesquery.size());
            this.recipesQueryPersistence.saveToFile(this.recipesquery);
            logger.info("Recipe query inserted successfully em {}", DATA_PATH);
            return true;
        } catch (Exception e) {
            logger.error("Error on insert recipe query: {}", e.getMessage(), e);
            return false;
        }
    }

    public Boolean hasRequiredIngredients(RecipeQuery recipeQuery) {
        ArrayList<RecipeIngredient> recipeIngredients = recipeQuery.getRecipe().getIngredients();
        ArrayList<Ingredient> ingredientsInStock = this.ingredientCatalog.getAllIngredients();

        for (RecipeIngredient ingredient : recipeIngredients) {
            for (Ingredient ingredientInStock : ingredientsInStock) {
                if (ingredient.getIngredient_name().equals(ingredientInStock.getName())) {
                    if (ingredientInStock.getQtd_in_stock() < ingredient.getRequired_quantity() * recipeQuery.getTarget_number_of_servings()) {
                        return false;
                    }
                }
            }

        }
        return true;
    }
    public Boolean consumeIngredients(RecipeQuery recipeQuery) {
        ArrayList<RecipeIngredient> recipeIngredients = recipeQuery.getRecipe().getIngredients();
        ArrayList<Ingredient> ingredientsInStock = this.ingredientCatalog.getAllIngredients();

        for (RecipeIngredient ingredient : recipeIngredients) {
            for (Ingredient ingredientInStock : ingredientsInStock) {
                if (ingredient.getIngredient_name().equals(ingredientInStock.getName())) {
                    int newQuantity = ingredientInStock.getQtd_in_stock() - (ingredient.getRequired_quantity() * recipeQuery.getTarget_number_of_servings());
                    if (newQuantity < 0) {
                        return false; // Not enough stock
                    }
                    ingredientInStock.setQtd_in_stock(newQuantity);
                    this.ingredientCatalog.insertIngredient(ingredientInStock);
                }
            }

        }
        return true;
    }

    public ArrayList<RecipeQuery> getAllRecipeQueries() {
        return this.recipesquery;
    }


    }
