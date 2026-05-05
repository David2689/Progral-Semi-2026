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

        // Configurar RecyclerView
        adapter = new ProductAdapter(product -> repository.delete(product));
        binding.recyclerProducts.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerProducts.setAdapter(adapter);

        // Observar todos los productos
        repository.getAllProducts().observe(this, products -> {
            adapter.setProducts(products);
        });

        // Botón agregar producto
        binding.fabAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddProductActivity.class)));

        // Botón recetas
        binding.btnRecipes.setOnClickListener(v ->
                startActivity(new Intent(this, RecipesActivity.class)));

        // Botón lista de compras
        binding.btnShopping.setOnClickListener(v ->
                startActivity(new Intent(this, ShoppingListActivity.class)));

        // Programar notificaciones diarias
        NotificationHelper.scheduleDaily(this);
    }
}