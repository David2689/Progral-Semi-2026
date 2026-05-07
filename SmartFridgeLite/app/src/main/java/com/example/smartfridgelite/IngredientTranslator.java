package com.example.smartfridgelite;

import java.util.HashMap;
import java.util.Map;

public class IngredientTranslator {

    private static final Map<String, String> translations = new HashMap<String, String>() {{
        // Carnes
        put("carne", "beef");
        put("carne de res", "beef");
        put("res", "beef");
        put("pollo", "chicken");
        put("cerdo", "pork");
        put("pescado", "fish");
        put("atún", "tuna");
        put("atun", "tuna");
        put("camarón", "shrimp");
        put("camaron", "shrimp");
        put("pavo", "turkey");
        put("cordero", "lamb");
        put("salchicha", "sausage");
        put("jamón", "ham");
        put("jamon", "ham");
        put("tocino", "bacon");

        // Lácteos
        put("leche", "milk");
        put("queso", "cheese");
        put("mantequilla", "butter");
        put("crema", "cream");
        put("crema de leche", "cream");
        put("yogur", "yogurt");
        put("yogurt", "yogurt");
        put("huevo", "eggs");
        put("huevos", "eggs");

        // Verduras
        put("tomate", "tomato");
        put("cebolla", "onion");
        put("ajo", "garlic");
        put("zanahoria", "carrot");
        put("papa", "potato");
        put("papas", "potato");
        put("lechuga", "lettuce");
        put("espinaca", "spinach");
        put("brócoli", "broccoli");
        put("brocoli", "broccoli");
        put("pepino", "cucumber");
        put("chile", "pepper");
        put("pimiento", "bell pepper");
        put("apio", "celery");
        put("calabaza", "pumpkin");
        put("maíz", "corn");
        put("maiz", "corn");
        put("aguacate", "avocado");
        put("champiñón", "mushroom");
        put("champinon", "mushroom");
        put("berenjena", "eggplant");

        // Frutas
        put("manzana", "apple");
        put("plátano", "banana");
        put("platano", "banana");
        put("banana", "banana");
        put("naranja", "orange");
        put("limón", "lemon");
        put("limon", "lemon");
        put("fresa", "strawberry");
        put("uva", "grape");
        put("mango", "mango");
        put("piña", "pineapple");
        put("pina", "pineapple");
        put("pera", "pear");
        put("sandía", "watermelon");
        put("sandia", "watermelon");
        put("melón", "melon");
        put("melon", "melon");

        // Granos y carbohidratos
        put("arroz", "rice");
        put("pasta", "pasta");
        put("pan", "bread");
        put("harina", "flour");
        put("frijol", "beans");
        put("frijoles", "beans");
        put("lenteja", "lentils");
        put("lentejas", "lentils");
        put("avena", "oats");
        put("tortilla", "tortilla");

        // Otros
        put("aceite", "oil");
        put("azúcar", "sugar");
        put("azucar", "sugar");
        put("sal", "salt");
        put("pimienta", "pepper");
        put("canela", "cinnamon");
        put("vainilla", "vanilla");
        put("chocolate", "chocolate");
        put("café", "coffee");
        put("cafe", "coffee");
    }};

    public static String translate(String spanishIngredient) {
        String lower = spanishIngredient.toLowerCase().trim();

        // Buscar traducción exacta
        if (translations.containsKey(lower)) {
            return translations.get(lower);
        }

        // Buscar si alguna clave está contenida en el nombre
        for (Map.Entry<String, String> entry : translations.entrySet()) {
            if (lower.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        // Si no encuentra traducción usar el nombre original
        return spanishIngredient;
    }
}