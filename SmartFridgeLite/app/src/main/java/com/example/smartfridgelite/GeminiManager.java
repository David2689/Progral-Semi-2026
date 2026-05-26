package com.example.smartfridgelite;

import java.util.ArrayList;
import java.util.List;

public class GeminiManager {

    public interface OnRecipeGeneratedListener {
        void onSuccess(String recipe);
        void onError(String error);
    }

    public static void generateRecipes(List<Product> expiringProducts,
                                       OnRecipeGeneratedListener listener) {
        new Thread(() -> {
            try {
                Thread.sleep(1500);
                String result = buildSmartRecipes(expiringProducts);
                listener.onSuccess(result);
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }).start();
    }

    private static String buildSmartRecipes(List<Product> products) {
        List<String> ingredients = new ArrayList<>();
        for (Product p : products) {
            ingredients.add(p.name.toLowerCase().trim());
        }

        List<Recipe> matched = getMatchingRecipes(ingredients);
        if (matched.isEmpty()) {
            matched = getGenericRecipes(ingredients);
        }

        StringBuilder result = new StringBuilder();
        result.append("🤖 Recetas generadas por IA\n");
        result.append("━━━━━━━━━━━━━━━━━━━━━━\n\n");

        int count = 1;
        for (Recipe r : matched) {
            if (count > 3) break;
            result.append("🍽️ Receta ").append(count).append(": ")
                    .append(r.name).append("\n\n");
            result.append("🥗 Ingredientes:\n").append(r.ingredients).append("\n\n");
            result.append("📋 Preparación:\n").append(r.steps).append("\n\n");
            result.append("⏱️ Tiempo: ").append(r.time).append("\n");
            result.append("━━━━━━━━━━━━━━━━━━━━━━\n\n");
            count++;
        }

        return result.toString();
    }

    static class Recipe {
        String name, ingredients, steps, time;
        String[] keywords;

        Recipe(String name, String ingredients, String steps,
               String time, String... keywords) {
            this.name = name;
            this.ingredients = ingredients;
            this.steps = steps;
            this.time = time;
            this.keywords = keywords;
        }
    }

    private static List<Recipe> getAllRecipes() {
        List<Recipe> all = new ArrayList<>();

        all.add(new Recipe("Pollo al ajillo",
                "• 500g de pollo\n• 6 dientes de ajo\n• Aceite\n• Sal y pimienta\n• Limón",
                "1. Corta el pollo en piezas\n2. Lamina el ajo\n3. Dora el ajo en aceite\n4. Agrega el pollo y cocina 20 min\n5. Sazona con sal y limón",
                "35 minutos", "pollo"));

        all.add(new Recipe("Pollo con verduras",
                "• 400g de pollo\n• Cebolla\n• Ajo\n• Salsa de soya\n• Aceite",
                "1. Corta el pollo en tiras\n2. Sofríe ajo y cebolla\n3. Agrega el pollo 10 min\n4. Añade salsa de soya\n5. Cocina 5 min más",
                "25 minutos", "pollo"));

        all.add(new Recipe("Carne a la plancha",
                "• 500g de carne\n• Ajo en polvo\n• Sal y pimienta\n• Romero\n• Aceite",
                "1. Sazona la carne con especias\n2. Calienta la plancha\n3. Cocina 5 min por lado\n4. Deja reposar 5 min\n5. Sirve con limón",
                "20 minutos", "carne", "res", "carne de res", "carne de vaca"));

        all.add(new Recipe("Picadillo de carne",
                "• 400g carne molida\n• Tomate\n• Cebolla\n• Ajo\n• Chile\n• Sal",
                "1. Sofríe cebolla y ajo\n2. Agrega la carne y dora\n3. Añade tomate y chile\n4. Sazona con sal\n5. Cocina 10 min",
                "25 minutos", "carne", "res", "carne molida"));

        all.add(new Recipe("Tortilla de huevo",
                "• 4 huevos\n• 2 papas\n• 1 cebolla\n• Sal\n• Aceite",
                "1. Fríe papas y cebolla\n2. Bate huevos con sal\n3. Mezcla con papas\n4. Cocina en sartén 5 min por lado\n5. Sirve caliente",
                "30 minutos", "huevo", "huevos"));

        all.add(new Recipe("Huevos revueltos",
                "• 3 huevos\n• Tomate\n• Cebolla\n• Chile\n• Sal\n• Aceite",
                "1. Sofríe las verduras\n2. Bate los huevos\n3. Vierte sobre verduras\n4. Revuelve suavemente\n5. Sirve inmediatamente",
                "10 minutos", "huevo", "huevos"));

        all.add(new Recipe("Arroz con leche",
                "• 1 taza arroz\n• 3 tazas leche\n• Azúcar\n• Canela\n• Limón",
                "1. Cocina arroz con agua\n2. Agrega leche caliente\n3. Añade azúcar y canela\n4. Revuelve 20 min a fuego bajo\n5. Sirve con canela",
                "35 minutos", "leche", "crema de leche", "crema"));

        all.add(new Recipe("Atol de leche",
                "• 2 tazas leche\n• Maicena\n• Azúcar\n• Canela\n• Vainilla",
                "1. Disuelve maicena en leche fría\n2. Calienta leche con canela\n3. Agrega maicena\n4. Revuelve hasta espesar\n5. Añade azúcar y vainilla",
                "15 minutos", "leche", "crema"));

        all.add(new Recipe("Salsa de tomate casera",
                "• 4 tomates\n• Cebolla\n• Ajo\n• Sal\n• Aceite\n• Orégano",
                "1. Sofríe cebolla y ajo\n2. Agrega tomates cortados\n3. Cocina 15 min\n4. Licúa o aplasta\n5. Sazona con sal",
                "20 minutos", "tomate", "tomates"));

        all.add(new Recipe("Papas al horno",
                "• 4 papas\n• Aceite\n• Ajo en polvo\n• Sal\n• Paprika\n• Romero",
                "1. Corta papas en cubos\n2. Mezcla con aceite y especias\n3. Coloca en bandeja\n4. Hornea 200°C por 40 min\n5. Voltea a mitad del tiempo",
                "45 minutos", "papa", "papas"));

        all.add(new Recipe("Plátanos fritos",
                "• 2 plátanos\n• Aceite\n• Azúcar\n• Canela",
                "1. Pela y corta en rodajas\n2. Calienta aceite\n3. Fríe 3 min por lado\n4. Espolvorea azúcar y canela\n5. Sirve caliente",
                "15 minutos", "plátano", "platano", "banana"));

        all.add(new Recipe("Sopa de verduras",
                "• Zanahoria\n• Papa\n• Cebolla\n• Tomate\n• Sal\n• Agua",
                "1. Pica todas las verduras\n2. Sofríe cebolla y ajo\n3. Agrega verduras\n4. Cubre con agua o caldo\n5. Cocina 20 min",
                "30 minutos", "zanahoria", "verdura", "vegetal"));

        all.add(new Recipe("Quesadillas",
                "• Queso\n• Tortillas\n• Sal",
                "1. Coloca queso sobre tortilla\n2. Dobla o cubre con otra tortilla\n3. Calienta en comal\n4. Voltea cuando esté dorada\n5. Sirve caliente",
                "10 minutos", "queso", "tortilla"));

        // Recetas con pescado
        all.add(new Recipe("Pescado frito",
                "• 500g de pescado\n• Sal y pimienta\n• Limón\n• Ajo en polvo\n• Aceite para freír\n• Harina",
                "1. Limpia y sazona el pescado con sal, pimienta y ajo\n2. Exprime limón sobre el pescado\n3. Pasa por harina ligeramente\n4. Calienta aceite en sartén\n5. Fríe 5 min por cada lado\n6. Escurre en papel absorbente\n7. Sirve con ensalada",
                "20 minutos", "pescado", "tilapia", "bagre"));

        all.add(new Recipe("Pescado al vapor con verduras",
                "• 400g de pescado\n• Zanahoria\n• Cebolla\n• Ajo\n• Sal\n• Limón\n• Cilantro",
                "1. Sazona el pescado con sal y limón\n2. Coloca en vaporera\n3. Agrega verduras alrededor\n4. Cocina al vapor 15 min\n5. Decora con cilantro\n6. Sirve caliente",
                "20 minutos", "pescado", "tilapia"));

// Recetas con arroz
        all.add(new Recipe("Arroz frito con verduras",
                "• 2 tazas arroz cocido\n• 2 huevos\n• Zanahoria\n• Cebolla\n• Ajo\n• Salsa de soya\n• Aceite",
                "1. Pica finamente zanahoria y cebolla\n2. Sofríe ajo y cebolla\n3. Agrega zanahoria y cocina 3 min\n4. Añade el arroz frío\n5. Haz espacio y agrega huevos revueltos\n6. Mezcla todo con salsa de soya\n7. Cocina 5 min más",
                "20 minutos", "arroz"));

        all.add(new Recipe("Arroz a la mexicana",
                "• 1 taza arroz\n• 2 tomates\n• 1 cebolla\n• 2 dientes ajo\n• Caldo de pollo\n• Sal\n• Aceite",
                "1. Licúa tomate, cebolla y ajo\n2. Fríe el arroz en aceite hasta dorar\n3. Agrega la salsa licuada\n4. Añade caldo de pollo\n5. Sazona con sal\n6. Tapa y cocina 20 min a fuego bajo\n7. Esponja con tenedor",
                "30 minutos", "arroz"));

// Recetas con frijoles
        all.add(new Recipe("Frijoles de olla",
                "• 500g frijoles\n• Cebolla\n• Ajo\n• Sal\n• Aceite\n• Chile",
                "1. Lava los frijoles\n2. Pon a cocer con agua, cebolla y ajo\n3. Cocina 1.5 horas a fuego medio\n4. Añade sal cuando estén suaves\n5. Agrega chile al gusto\n6. Sirve con tortillas",
                "2 horas", "frijol", "frijoles"));

        all.add(new Recipe("Frijoles fritos",
                "• 2 tazas frijoles cocidos\n• Cebolla\n• Ajo\n• Sal\n• Aceite\n• Queso (opcional)",
                "1. Sofríe cebolla y ajo en aceite\n2. Agrega los frijoles cocidos\n3. Aplasta con tenedor o licúa\n4. Cocina revolviendo 10 min\n5. Sazona con sal\n6. Sirve con queso rallado",
                "15 minutos", "frijol", "frijoles"));

// Recetas con aguacate
        all.add(new Recipe("Guacamole fresco",
                "• 2 aguacates\n• 1 tomate\n• 1 cebolla\n• Chile\n• Limón\n• Sal\n• Cilantro",
                "1. Abre y machaca los aguacates\n2. Pica tomate, cebolla y chile fino\n3. Mezcla todo con el aguacate\n4. Agrega jugo de limón\n5. Sazona con sal\n6. Añade cilantro picado\n7. Sirve inmediatamente",
                "10 minutos", "aguacate"));

        all.add(new Recipe("Tostadas con aguacate",
                "• 2 aguacates\n• Tostadas\n• Tomate\n• Cebolla\n• Sal\n• Limón\n• Chile",
                "1. Machaca el aguacate con sal y limón\n2. Pica tomate y cebolla\n3. Unta aguacate en cada tostada\n4. Coloca tomate y cebolla encima\n5. Agrega chile al gusto\n6. Sirve inmediatamente",
                "10 minutos", "aguacate"));

// Recetas con zanahoria
        all.add(new Recipe("Ensalada de zanahoria",
                "• 3 zanahorias\n• Limón\n• Sal\n• Aceite de oliva\n• Perejil\n• Ajo",
                "1. Ralla las zanahorias\n2. Mezcla limón, aceite y ajo\n3. Aliña las zanahorias\n4. Añade perejil picado\n5. Refrigera 15 min\n6. Sirve fría",
                "15 minutos", "zanahoria"));

        all.add(new Recipe("Crema de zanahoria",
                "• 4 zanahorias\n• 1 cebolla\n• Ajo\n• Caldo de verduras\n• Crema\n• Sal\n• Pimienta",
                "1. Pica zanahoria, cebolla y ajo\n2. Sofríe cebolla y ajo\n3. Agrega zanahoria y caldo\n4. Cocina 20 min hasta suavizar\n5. Licúa hasta obtener crema\n6. Agrega crema y sazona\n7. Sirve caliente",
                "30 minutos", "zanahoria"));

// Recetas con queso
        all.add(new Recipe("Quesillo gratinado",
                "• 250g queso\n• Pan o tortillas\n• Mantequilla\n• Orégano",
                "1. Precalienta el horno a 180°C\n2. Coloca el queso en molde\n3. Agrega orégano por encima\n4. Hornea 10-15 min\n5. Sirve cuando esté dorado\n6. Acompaña con pan o tortillas",
                "20 minutos", "queso"));

// Recetas con pan
        all.add(new Recipe("Tostadas francesas",
                "• 4 rebanadas de pan\n• 2 huevos\n• Leche\n• Azúcar\n• Canela\n• Mantequilla",
                "1. Bate huevos con leche y canela\n2. Remoja el pan en la mezcla\n3. Derrite mantequilla en sartén\n4. Cocina el pan 3 min por lado\n5. Espolvorea azúcar\n6. Sirve con miel o frutas",
                "15 minutos", "pan"));

        all.add(new Recipe("Bruschetta casera",
                "• 4 rebanadas de pan\n• 2 tomates\n• Ajo\n• Aceite de oliva\n• Sal\n• Orégano\n• Albahaca",
                "1. Tuesta el pan en horno o sartén\n2. Frota ajo sobre el pan caliente\n3. Pica tomates finamente\n4. Mezcla tomates con aceite y sal\n5. Coloca sobre el pan\n6. Agrega orégano y albahaca\n7. Sirve inmediatamente",
                "15 minutos", "pan"));

// Recetas con mango
        all.add(new Recipe("Mango con chile y limón",
                "• 2 mangos\n• Chile en polvo\n• Limón\n• Sal",
                "1. Pela y corta el mango\n2. Coloca en plato o palito\n3. Exprime limón por encima\n4. Espolvorea chile en polvo\n5. Agrega sal al gusto\n6. Sirve frío",
                "5 minutos", "mango"));

        all.add(new Recipe("Licuado de mango",
                "• 2 mangos\n• 1 taza leche\n• Azúcar al gusto\n• Hielo",
                "1. Pela y corta el mango\n2. Coloca en licuadora\n3. Agrega leche y azúcar\n4. Licúa hasta homogéneo\n5. Agrega hielo\n6. Sirve inmediatamente",
                "5 minutos", "mango"));

// Recetas con fresa
        all.add(new Recipe("Fresas con crema",
                "• 500g fresas\n• Crema\n• Azúcar\n• Vainilla",
                "1. Lava y corta las fresas\n2. Mezcla crema con azúcar y vainilla\n3. Bate hasta espesar\n4. Coloca fresas en copa\n5. Cubre con crema\n6. Sirve frío",
                "10 minutos", "fresa", "fresas"));

// Recetas con naranja
        all.add(new Recipe("Jugo de naranja natural",
                "• 6 naranjas\n• Azúcar al gusto\n• Agua (opcional)\n• Hielo",
                "1. Lava las naranjas\n2. Corta por la mitad\n3. Exprime en exprimidor\n4. Agrega azúcar al gusto\n5. Añade agua si deseas\n6. Sirve con hielo",
                "5 minutos", "naranja"));

// Recetas con cebolla
        all.add(new Recipe("Cebolla caramelizada",
                "• 3 cebollas\n• Mantequilla\n• Sal\n• Azúcar\n• Vinagre balsámico",
                "1. Corta cebollas en juliana\n2. Derrite mantequilla en sartén\n3. Agrega las cebollas con sal\n4. Cocina a fuego bajo 30 min\n5. Añade azúcar y vinagre\n6. Cocina 10 min más\n7. Usa como acompañamiento",
                "45 minutos", "cebolla"));

// Recetas con chile
        all.add(new Recipe("Salsa picante casera",
                "• 5 chiles\n• 2 tomates\n• Ajo\n• Cebolla\n• Sal\n• Limón",
                "1. Asa los chiles y tomates\n2. Licúa con ajo y cebolla\n3. Agrega sal y limón\n4. Ajusta picante al gusto\n5. Guarda en frasco\n6. Sirve con cualquier platillo",
                "15 minutos", "chile", "chiles"));

        return all;
    }

    private static List<Recipe> getMatchingRecipes(List<String> ingredients) {
        List<Recipe> matched = new ArrayList<>();
        List<Recipe> all = getAllRecipes();

        for (Recipe recipe : all) {
            for (String keyword : recipe.keywords) {
                boolean found = false;
                for (String ingredient : ingredients) {
                    if (ingredient.contains(keyword) || keyword.contains(ingredient)) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    matched.add(recipe);
                    break;
                }
            }
        }
        return matched;
    }

    private static List<Recipe> getGenericRecipes(List<String> ingredients) {
        List<Recipe> generic = new ArrayList<>();
        String ingredientList = "";
        for (String i : ingredients) {
            ingredientList += "• " + i + "\n";
        }
        String first = ingredients.get(0);

        generic.add(new Recipe(
                "Salteado de " + first,
                ingredientList + "• Sal y pimienta\n• Aceite\n• Ajo",
                "1. Lava y corta los ingredientes\n2. Calienta aceite\n3. Sofríe el ajo\n4. Agrega ingredientes duros primero\n5. Sazona y sirve caliente",
                "20 minutos"));

        generic.add(new Recipe(
                "Sopa de " + first,
                ingredientList + "• Caldo\n• Sal\n• Cebolla",
                "1. Pica en trozos\n2. Sofríe cebolla\n3. Agrega ingredientes\n4. Cubre con caldo\n5. Cocina 20 min",
                "30 minutos"));

        generic.add(new Recipe(
                "Revuelto de " + first,
                ingredientList + "• 2 huevos\n• Sal\n• Aceite",
                "1. Prepara los ingredientes\n2. Bate huevos con sal\n3. Cocina ingredientes 5 min\n4. Añade huevos\n5. Revuelve y sirve",
                "15 minutos"));

        return generic;
    }
}