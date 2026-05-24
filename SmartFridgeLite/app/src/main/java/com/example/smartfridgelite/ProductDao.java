package com.example.smartfridgelite;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface ProductDao {

    @Insert
    void insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    @Query("SELECT * FROM products ORDER BY expirationDate ASC")
    LiveData<List<Product>> getAllProducts();

    @Query("SELECT * FROM products WHERE expirationDate BETWEEN :now AND :twoDaysLater")
    LiveData<List<Product>> getExpiringSoon(long now, long twoDaysLater);

    @Query("SELECT * FROM products WHERE inShoppingList = 1")
    LiveData<List<Product>> getShoppingList();

    @Query("SELECT * FROM products WHERE expirationDate BETWEEN :now AND :twoDaysLater ORDER BY expirationDate ASC")
    List<Product> getExpiringSoonSync(long now, long twoDaysLater);

    @Insert
    long insertAndGetId(Product product);
}
