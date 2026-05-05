package com.example.smartfridgelite;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductRepository {

    private final ProductDao productDao;
    private final ExecutorService executor;

    public ProductRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        productDao = db.productDao();
        executor = Executors.newSingleThreadExecutor();
    }

    public void insert(Product product) {
        executor.execute(() -> productDao.insert(product));
    }

    public void update(Product product) {
        executor.execute(() -> productDao.update(product));
    }

    public void delete(Product product) {
        executor.execute(() -> productDao.delete(product));
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