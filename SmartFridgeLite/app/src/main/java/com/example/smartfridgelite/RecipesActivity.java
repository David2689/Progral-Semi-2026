package com.example.smartfridgelite;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.smartfridgelite.databinding.ActivityRecipesBinding;
import java.util.List;
import java.util.concurrent.Executors;

public class RecipesActivity extends AppCompatActivity {

    private ActivityRecipesBinding binding;
    private RecipeAdapter adapter;
    private ProductRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new ProductRepository(getApplication());
        adapter = new RecipeAdapter();
        binding.recyclerRecipes.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerRecipes.setAdapter(adapter);

        loadRecipes();
    }

    private void loadRecipes() {
        binding.progressBar.setVisibility(View.VISIBLE);

        Executors.newSingleThreadExecutor().execute(() -> {
            List<Product> expiring = repository.getExpiringSoonSync();

            runOnUiThread(() -> {
                binding.progressBar.setVisibility(View.GONE);

                if (expiring.isEmpty()) {
                    binding.tvIngredients.setText(
                            "✅ No tienes productos por vencer. ¡Tu nevera está bien!");
                    return;
                }

                // Construir texto de ingredientes
                StringBuilder sb = new StringBuilder("🔍 Ingredientes por vencer: ");
                for (Product p : expiring) {
                    sb.append(p.name).append(", ");
                }
                binding.tvIngredients.setText(sb.toString());

                // Buscar recetas locales
                List<RecipeManager.Recipe> recipes =
                        RecipeManager.findRecipes(expiring);

                if (recipes.isEmpty()) {
                    binding.tvIngredients.setText(
                            "😕 No encontramos recetas para tus ingredientes.\n" +
                                    "Ingredientes por vencer: " + sb.toString());
                } else {
                    adapter.setRecipes(recipes);
                }
            });
        });
    }
}