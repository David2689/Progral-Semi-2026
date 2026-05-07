package com.example.smartfridgelite;

import android.os.Bundle;
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

        repository = new ProductRepository(getApplication());

        adapter = new ProductAdapter(
                // Botón X — confirmar antes de quitar de la lista
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
                // Botón + — no hace nada dentro de compras
                product -> {}
        );

        binding.recyclerShopping.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerShopping.setAdapter(adapter);

        repository.getShoppingList().observe(this, products -> {
            adapter.setProducts(products);
        });
    }
}