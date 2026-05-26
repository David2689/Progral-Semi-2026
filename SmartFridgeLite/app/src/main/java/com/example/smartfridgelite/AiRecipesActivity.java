package com.example.smartfridgelite;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartfridgelite.databinding.ActivityAiRecipesBinding;
import java.util.List;
import java.util.concurrent.Executors;

public class AiRecipesActivity extends AppCompatActivity {

    private ActivityAiRecipesBinding binding;
    private ProductRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAiRecipesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        repository = new ProductRepository(getApplication());
        generateAiRecipes();
    }

    private void generateAiRecipes() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.tvRecipes.setVisibility(View.GONE);

        Executors.newSingleThreadExecutor().execute(() -> {
            List<Product> expiring = repository.getExpiringSoonSync();

            runOnUiThread(() -> {
                if (expiring.isEmpty()) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.tvStatus.setText("✅ No tienes productos por vencer.");
                    return;
                }

                StringBuilder sb = new StringBuilder("🔍 Generando recetas con: ");
                for (Product p : expiring) {
                    sb.append(p.name).append(", ");
                }
                binding.tvStatus.setText(sb.toString());

                GeminiManager.generateRecipes(expiring,
                        new GeminiManager.OnRecipeGeneratedListener() {
                            @Override
                            public void onSuccess(String recipe) {
                                runOnUiThread(() -> {
                                    binding.progressBar.setVisibility(View.GONE);
                                    binding.tvRecipes.setVisibility(View.VISIBLE);
                                    binding.tvRecipes.setText(recipe);
                                    binding.tvStatus.setText("✅ Recetas generadas por IA:");
                                });
                            }

                            @Override
                            public void onError(String error) {
                                runOnUiThread(() -> {
                                    binding.progressBar.setVisibility(View.GONE);
                                    binding.tvStatus.setText("❌ Error: " + error);
                                });
                            }
                        });
            });
        });
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
