package com.example.smartfridgelite;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.smartfridgelite.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ProductRepository repository;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new ProductRepository(getApplication());

        adapter = new ProductAdapter(
                product -> {
                    new androidx.appcompat.app.AlertDialog.Builder(this)
                            .setTitle("Eliminar producto")
                            .setMessage("¿Seguro que quieres eliminar \"" + product.name + "\"?")
                            .setPositiveButton("Sí, eliminar", (dialog, which) ->
                                    repository.delete(product))
                            .setNegativeButton("Cancelar", null)
                            .show();
                },
                product -> {
                    product.inShoppingList = true;
                    repository.update(product);
                },
                product -> {
                    Intent intent = new Intent(this, EditProductActivity.class);
                    intent.putExtra("id", product.id);
                    intent.putExtra("name", product.name);
                    intent.putExtra("category", product.category);
                    intent.putExtra("quantity", product.quantity);
                    intent.putExtra("expirationDate", product.expirationDate);
                    intent.putExtra("inShoppingList", product.inShoppingList);
                    startActivity(intent);
                }
        );

        binding.recyclerProducts.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerProducts.setAdapter(adapter);

        repository.getAllProducts().observe(this, products -> {
            adapter.setProducts(products);
        });

        binding.fabAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddProductActivity.class)));

        binding.btnRecipes.setOnClickListener(v ->
                startActivity(new Intent(this, RecipesActivity.class)));

        binding.btnShopping.setOnClickListener(v ->
                startActivity(new Intent(this, ShoppingListActivity.class)));

        NotificationHelper.scheduleDaily(this);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
    }
}