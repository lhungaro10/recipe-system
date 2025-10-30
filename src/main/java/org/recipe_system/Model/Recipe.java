// Em src/main/java/org/recipe_system/Model/Recipe.java
package org.recipe_system.Model;

import java.io.Serializable;
import java.lang.System;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Recipe implements Serializable {
    private static final AtomicLong count = new AtomicLong(System.currentTimeMillis());
    private Long id;
    private String name;
    private Integer number_of_servings;
    private ArrayList<RecipeIngredient> recipeIngredients;

    public Recipe(String name, Integer number_of_servings) {
        this.id = count.incrementAndGet();
        this.name = name;
        this.number_of_servings = number_of_servings;
        this.recipeIngredients = new ArrayList<>();
    }

    public Recipe(String name, int servings, ArrayList<Ingredient> ingredients, ArrayList<Integer> quantities) {
        this.id = count.incrementAndGet();
        this.name = name;
        this.number_of_servings = servings;
        this.recipeIngredients = new ArrayList<>();
        for (int i = 0; i < ingredients.size(); i++) {
            this.add_ingredient(ingredients.get(i), quantities.get(i));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getServings() {
        return number_of_servings;
    }

    public void setNumber_of_servings(Integer number_of_servings) {
        this.number_of_servings = number_of_servings;
    }

    public ArrayList<RecipeIngredient> getIngredients() {
        return recipeIngredients;
    }

    public boolean add_ingredient(Ingredient ingredient, Integer qtd) {
        if (validateNumber(qtd)) {
            this.recipeIngredients.add(new RecipeIngredient(ingredient.getName(), qtd));
            return true;
        }
        return false;
    }

    public RecipeIngredient getRecipeIngredient(Ingredient ingredient) {
        if (ingredient == null) return null;
        String name = ingredient.getName();
        for (RecipeIngredient ri : this.recipeIngredients) {
            if (ri != null && Objects.equals(name, ri.getIngredient_name())) {
                return ri;
            }
        }
        return null;
    }

    public void remove_ingredient_not_in(ArrayList<RecipeIngredient> ingredientsToKeep) {
        this.recipeIngredients.clear();
        this.recipeIngredients.addAll(ingredientsToKeep);
    }

    public boolean validateNumber(Integer number) {
        return number > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(id, recipe.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number_of_servings=" + number_of_servings +
                ", recipeIngredients=" + recipeIngredients +
                '}';
    }
}