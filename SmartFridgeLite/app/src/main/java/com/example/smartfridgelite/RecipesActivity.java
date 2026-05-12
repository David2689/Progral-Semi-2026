package com.example.smartfridgelite;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.smartfridgelite.databinding.ActivityRecipesBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesActivity extends AppCompatActivity {

    private ActivityRecipesBinding binding;
    private RecipeAdapter adapter;
    private ProductRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        repository = new ProductRepository(getApplication());

        adapter = new RecipeAdapter(recipe -> {
            if (recipe.isOnline && recipe.mealId != null) {
                String url = "https://www.themealdb.com/meal/" + recipe.mealId;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        binding.recyclerRecipes.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerRecipes.setAdapter(adapter);

        loadRecipes();
    }

    private void loadRecipes() {
        binding.progressBar.setVisibility(View.VISIBLE);

        Executors.newSingleThreadExecutor().execute(() -> {
            List<Product> expiring = repository.getExpiringSoonSync();

            runOnUiThread(() -> {
                if (expiring.isEmpty()) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.tvIngredients.setText(
                            "✅ No tienes productos por vencer. ¡Tu nevera está bien!");
                    return;
                }

                StringBuilder sb = new StringBuilder("🔍 Ingredientes por vencer: ");
                for (Product p : expiring) {
                    sb.append(p.name).append(", ");
                }
                binding.tvIngredients.setText(sb.toString());

                searchOnline(expiring);
            });
        });
    }

    private void searchOnline(List<Product> expiring) {
        String ingredient = IngredientTranslator.translate(expiring.get(0).name)
                .toLowerCase()
                .replace(" ", "_");

        MealDbClient.getApi()
                .searchByIngredient(ingredient)
                .enqueue(new Callback<MealDbApi.MealResponse>() {
                    @Override
                    public void onResponse(Call<MealDbApi.MealResponse> call,
                                           Response<MealDbApi.MealResponse> response) {
                        binding.progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().meals != null
                                && !response.body().meals.isEmpty()) {

                            List<RecipeAdapter.RecipeItem> items = new ArrayList<>();
                            for (MealDbApi.Meal meal : response.body().meals) {
                                items.add(new RecipeAdapter.RecipeItem(
                                        meal.strMeal, meal.idMeal));
                            }
                            binding.tvIngredients.setText("🌐 Toca una receta para verla completa:");
                            adapter.setRecipes(items);

                        } else {
                            showLocalRecipes(expiring);
                        }
                    }

                    @Override
                    public void onFailure(Call<MealDbApi.MealResponse> call, Throwable t) {
                        binding.progressBar.setVisibility(View.GONE);
                        showLocalRecipes(expiring);
                    }
                });
    }

    private void showLocalRecipes(List<Product> expiring) {
        List<RecipeManager.Recipe> localRecipes = RecipeManager.findRecipes(expiring);
        List<RecipeAdapter.RecipeItem> items = new ArrayList<>();

        for (RecipeManager.Recipe r : localRecipes) {
            items.add(new RecipeAdapter.RecipeItem(r));
        }

        if (items.isEmpty()) {
            binding.tvIngredients.setText("😕 No encontramos recetas para tus ingredientes.");
        } else {
            binding.tvIngredients.setText("📱 Recetas locales (sin internet):");
            adapter.setRecipes(items);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}