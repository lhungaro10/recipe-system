package org.recipe_system;

import org.recipe_system.Controller.Controller;
import org.recipe_system.Model.Ingredient;
import org.recipe_system.views.Home;

import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        Boolean insertedOne = controller.registerIngredient("banana", 10);
        if (insertedOne) {
            for (Ingredient ingredient : controller.listIngredients()){
                System.out.println(ingredient.toString());
            }
        } else {
            System.out.println("Falha ao inserir ingredientes.");
        }

        SwingUtilities.invokeLater(() -> {
            Home mainView = new Home();

            mainView.setVisible(true);
        });
    }
}