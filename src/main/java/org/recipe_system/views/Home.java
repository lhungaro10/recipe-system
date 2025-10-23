package org.recipe_system.views;

import org.recipe_system.Controller.Controller;
import org.recipe_system.views.IngredientsViews.AddIngredientView;
import org.recipe_system.views.IngredientsViews.ListAllIngredientsView;
import org.recipe_system.views.RecipeViews.AddRecipeView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Home extends JFrame {
    public Controller controller = new Controller();
    public Home() {
        // --- Configuração básica da janela (JFrame) ---
        super.setTitle("Sistema de Receitas");
        super.setSize(800, 600);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setLocationRelativeTo(null);
        super.setLayout(new BorderLayout(10, 10));

        // --- 1. Cria e configura a Barra de Menus ---
        createMenuBar();

        // --- 2. Painel do título (continua igual) ---
        JLabel titleLabel = new JLabel("Bem-vindo ao Sistema de Receitas");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        super.add(titleLabel, BorderLayout.NORTH);

        // --- 3. Mensagem central ---
        // Como removemos os botões, o centro ficou vazio.
        // Adicionamos um label para preencher o espaço.
        JLabel welcomeMessage = new JLabel(
                "<html><div style='text-align: center;'>" +
                        "Selecione uma opção na barra de menus acima para começar.<br>" +
                        "Você pode cadastrar novas receitas, ingredientes ou planejar o que cozinhar." +
                        "</div></html>"
        );
        welcomeMessage.setFont(new Font("Arial", Font.PLAIN, 16));
        welcomeMessage.setHorizontalAlignment(SwingConstants.CENTER);
        super.add(welcomeMessage, BorderLayout.CENTER);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // --- Menu "Arquivo" ---
        JMenu fileMenu = new JMenu("Arquivo");
        JMenuItem exitItem = new JMenuItem("Sair");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        // --- Menu "Receitas" ---
        JMenu recipesMenu = new JMenu("Receitas");
        JMenuItem addRecipeItem = new JMenuItem("Cadastrar Nova...");
        JMenuItem listRecipesItem = new JMenuItem("Listar Todas...");

        addRecipeItem.addActionListener(e -> {
                AddRecipeView addRecipeDialog = new AddRecipeView(this, controller);
                addRecipeDialog.setVisible(true);
            }
        );
        listRecipesItem.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Funcionalidade 'Ver Receitas' a ser implementada!")
        );

        recipesMenu.add(addRecipeItem);
        recipesMenu.add(listRecipesItem);

        // --- Menu "Ingredientes" ---
        JMenu ingredientsMenu = new JMenu("Ingredientes");
        JMenuItem addIngredientItem = new JMenuItem("Cadastrar Novo...");
        JMenuItem listIngredientsItem = new JMenuItem("Listar Todos...");

        addIngredientItem.addActionListener(e -> {
                AddIngredientView addIngredientDialog = new AddIngredientView(this, controller);
                addIngredientDialog.setVisible(true);
            }
        );
        listIngredientsItem.addActionListener(e -> {
            ListAllIngredientsView listView = new ListAllIngredientsView(controller);
            listView.setVisible(true);
        });

        ingredientsMenu.add(addIngredientItem);
        ingredientsMenu.add(listIngredientsItem);

        // --- Menu "Ações" ---
        JMenu actionsMenu = new JMenu("Ações");
        JMenuItem planRecipeItem = new JMenuItem("Planejar Receita...");

        planRecipeItem.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Funcionalidade 'Planejar Receita' a ser implementada!")
        );

        actionsMenu.add(planRecipeItem);

        // Adiciona os menus à barra de menus
        menuBar.add(fileMenu);
        menuBar.add(recipesMenu);
        menuBar.add(ingredientsMenu);
        menuBar.add(actionsMenu);

        // Define a barra de menus para esta janela (JFrame)
        super.setJMenuBar(menuBar);
    }
}