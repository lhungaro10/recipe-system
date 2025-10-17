package org.recipe_system.Model;

import java.io.Serializable;

public class Ingredient implements Serializable {
    private String name;
    private int qtd_in_stock;

    public Ingredient(String name, int qtd_in_stock) {
        this.name = name;
        this.qtd_in_stock = qtd_in_stock;
    }

    // Getters e Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQtd_in_stock() {
        return qtd_in_stock;
    }

    public void setQtd_in_stock(int qtd_in_stock) {
        this.qtd_in_stock = qtd_in_stock;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", qtd_in_stock=" + qtd_in_stock +
                '}';
    }
}
