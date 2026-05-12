package com.example.smartfridgelite;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.smartfridgelite.databinding.ActivityShoppingListBinding;

public class ShoppingListActivity extends AppCompatActivity {

    private ActivityShoppingListBinding binding;
    private ProductRepository repository;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShoppingListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("🛒 Lista de compras");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        repository = new ProductRepository(getApplication());

        adapter = new ProductAdapter(
                product -> {
                    new androidx.appcompat.app.AlertDialog.Builder(this)
                            .setTitle("Quitar de compras")
                            .setMessage("¿Ya compraste \"" + product.name + "\"? Se quitará de tu lista.")
                            .setPositiveButton("Sí, ya lo compré", (dialog, which) -> {
                                product.inShoppingList = false;
                                repository.update(product);
                            })
                            .setNegativeButton("Cancelar", null)
                            .show();
                },
                product -> {}
        );

        binding.recyclerShopping.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerShopping.setAdapter(adapter);

        repository.getShoppingList().observe(this, products -> {
            adapter.setProducts(products);
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