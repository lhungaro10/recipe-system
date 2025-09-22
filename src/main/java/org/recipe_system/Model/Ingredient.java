package org.recipe_system.Model;

public class Ingredient {
    private String name;
    private int quantity_in_stock;

    public Ingredient(String name, int quantity_in_stock) {
        this.name = name;
        this.quantity_in_stock = quantity_in_stock;
    }

    // Getters e Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity_in_stock() {
        return quantity_in_stock;
    }

    public void setQuantity_in_stock(int quantity_in_stock) {
        this.quantity_in_stock = quantity_in_stock;
    }
}
