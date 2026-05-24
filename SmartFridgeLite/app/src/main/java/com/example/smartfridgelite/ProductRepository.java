package com.example.smartfridgelite;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.content.SharedPreferences;

public class ProductRepository {

    private final ProductDao productDao;
    private final ExecutorService executor;
    private final android.content.Context context;

    public ProductRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        productDao = db.productDao();
        executor = Executors.newSingleThreadExecutor();
        context = application.getApplicationContext();
    }

    public void insert(Product product) {
        executor.execute(() -> {
            // Room genera el id al insertar
            long generatedId = productDao.insertAndGetId(product);
            product.id = (int) generatedId;

            // Ahora sincronizar con Firebase con el id correcto
            SharedPreferences prefs = context.getSharedPreferences("session",
                    android.content.Context.MODE_PRIVATE);
            String userId = String.valueOf(prefs.getInt("id", 0));
            FirebaseManager.saveProduct(product, userId);
        });
    }

    public void update(Product product) {
        executor.execute(() -> {
            productDao.update(product);
            SharedPreferences prefs = context.getSharedPreferences("session",
                    android.content.Context.MODE_PRIVATE);
            String userId = String.valueOf(prefs.getInt("id", 0));
            FirebaseManager.saveProduct(product, userId);
        });
    }

    public void delete(Product product) {
        executor.execute(() -> {
            productDao.delete(product);
            SharedPreferences prefs = context.getSharedPreferences("session",
                    android.content.Context.MODE_PRIVATE);
            String userId = String.valueOf(prefs.getInt("id", 0));
            FirebaseManager.deleteProduct(product, userId);
        });
    }

    public LiveData<List<Product>> getAllProducts() {
        return productDao.getAllProducts();
    }

    public LiveData<List<Product>> getExpiringSoon() {
        long now = System.currentTimeMillis();
        long twoDays = now + (2L * 24 * 60 * 60 * 1000);
        return productDao.getExpiringSoon(now, twoDays);
    }

    public LiveData<List<Product>> getShoppingList() {
        return productDao.getShoppingList();
    }

    public List<Product> getExpiringSoonSync() {
        long now = System.currentTimeMillis();
        long twoDays = now + (2L * 24 * 60 * 60 * 1000);
        return productDao.getExpiringSoonSync(now, twoDays);
    }
}