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

        repository = new ProductRepository(getApplication());
        adapter = new ProductAdapter(product -> {
            product.inShoppingList = false;
            repository.update(product);
        });

        binding.recyclerShopping.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerShopping.setAdapter(adapter);

        repository.getShoppingList().observe(this, products -> {
            adapter.setProducts(products);
        });
    }
}
