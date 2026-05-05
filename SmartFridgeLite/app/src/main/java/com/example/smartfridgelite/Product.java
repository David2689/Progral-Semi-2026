package com.example.smartfridgelite;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class Product {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String category;
    public long expirationDate;
    public int quantity;
    public boolean inShoppingList;

    public Product(String name, String category, long expirationDate, int quantity) {
        this.name = name;
        this.category = category;
        this.expirationDate = expirationDate;
        this.quantity = quantity;
        this.inShoppingList = false;
    }

    public long getDaysUntilExpiration() {
        long today = System.currentTimeMillis();
        long diff = expirationDate - today;
        return diff / (1000 * 60 * 60 * 24);
    }

    public boolean isExpiringSoon() {
        return getDaysUntilExpiration() <= 2 && getDaysUntilExpiration() >= 0;
    }

    public boolean isExpired() {
        return getDaysUntilExpiration() < 0;
    }
}