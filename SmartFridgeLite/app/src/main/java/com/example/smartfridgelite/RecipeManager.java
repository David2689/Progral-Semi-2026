package com.example.smartfridgelite;

import java.util.ArrayList;
import java.util.List;

public class RecipeManager {

    public static class Recipe {
        public String title;
        public String ingredients;
        public String instructions;

        public Recipe(String title, String ingredients, String instructions) {
            this.title = title;
            this.ingredients = ingredients;
            this.instructions = instructions;
        }
    }

    // Base de recetas locales
    private static final List<Recipe> ALL_RECIPES = new ArrayList<Recipe>() {{
        add(new Recipe("Tortilla de huevo",
                "huevo,huevos",
                "Bate los huevos, agrega sal y cocina en sartén con aceite a fuego medio."));

        add(new Recipe("Leche con canela",
                "leche",
                "Calienta la leche, agrega canela y azúcar al gusto. Sirve caliente."));

        add(new Recipe("Carne a la plancha",
                "carne,res,pollo,cerdo",
                "Sazona la carne con sal, pimienta y ajo. Cocina en plancha caliente 5 min por lado."));

        add(new Recipe("Ensalada fresca",
                "tomate,lechuga,zanahoria,pepino",
                "Lava y corta las verduras. Mezcla con aceite de oliva, sal y limón."));

        add(new Recipe("Arroz con leche",
                "leche,arroz",
                "Cocina el arroz con leche, azúcar y canela a fuego bajo por 30 minutos."));

        add(new Recipe("Omelette de verduras",
                "huevo,huevos,tomate,cebolla,chile",
                "Bate huevos, agrega verduras picadas y cocina en sartén tapado."));

        add(new Recipe("Sopa de verduras",
                "zanahoria,papa,cebolla,tomate,apio",
                "Hierve todas las verduras picadas en agua con sal por 20 minutos."));

        add(new Recipe("Plátano frito",
                "plátano,platano,banana",
                "Corta el plátano en rodajas y fríe en aceite caliente hasta dorar."));

        add(new Recipe("Quesillo o quesadilla",
                "queso,tortilla",
                "Pon queso sobre la tortilla y calienta en comal hasta que se derrita."));

        add(new Recipe("Smoothie de frutas",
                "manzana,pera,mango,fresa,naranja,banana,plátano",
                "Licúa las frutas con un poco de agua o leche. Sirve frío."));

        add(new Recipe("Caldo de pollo",
                "pollo",
                "Hierve el pollo con agua, sal, ajo y cebolla por 40 minutos."));

        add(new Recipe("Frijoles con huevo",
                "huevo,huevos,frijol,frijoles",
                "Fríe los frijoles cocidos y agrega huevo batido. Mezcla bien."));

        add(new Recipe("Crema de papa",
                "papa,papas",
                "Hierve las papas, licúa con leche, sal y mantequilla. Sirve caliente."));

        add(new Recipe("Cereal con leche",
                "leche",
                "Sirve cereal en un tazón y agrega leche fría al gusto."));

        add(new Recipe("Guacamole",
                "aguacate,tomate,cebolla",
                "Machaca el aguacate, agrega tomate y cebolla picados, sal y limón."));
    }};

    // Busca recetas que coincidan con los ingredientes por vencer
    public static List<Recipe> findRecipes(List<Product> expiringProducts) {
        List<Recipe> result = new ArrayList<>();

        for (Recipe recipe : ALL_RECIPES) {
            for (Product product : expiringProducts) {
                String productName = product.name.toLowerCase().trim();
                String recipeIngredients = recipe.ingredients.toLowerCase();

                if (recipeIngredients.contains(productName) ||
                        productName.contains(recipeIngredients.split(",")[0])) {
                    if (!result.contains(recipe)) {
                        result.add(recipe);
                    }
                    break;
                }
            }
        }

        return result;
    }
}
