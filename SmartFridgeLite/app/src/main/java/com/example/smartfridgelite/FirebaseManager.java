package com.example.smartfridgelite;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.HashMap;
import java.util.Map;

public class FirebaseManager {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String COLLECTION_PRODUCTS = "products";

    // Guardar producto en Firestore
    public static void saveProduct(Product product, String userId) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", product.name);
        data.put("category", product.category);
        data.put("quantity", product.quantity);
        data.put("expirationDate", product.expirationDate);
        data.put("inShoppingList", product.inShoppingList);
        data.put("localId", product.id);
        data.put("userId", userId);

        db.collection(COLLECTION_PRODUCTS)
                .document(userId + "_" + product.id)
                .set(data)
                .addOnSuccessListener(aVoid ->
                        android.util.Log.d("Firebase", "Producto guardado: " + product.name))
                .addOnFailureListener(e ->
                        android.util.Log.e("Firebase", "Error guardando: " + e.getMessage()));
    }

    // Eliminar producto de Firestore
    public static void deleteProduct(Product product, String userId) {
        db.collection(COLLECTION_PRODUCTS)
                .document(userId + "_" + product.id)
                .delete()
                .addOnSuccessListener(aVoid ->
                        android.util.Log.d("Firebase", "Producto eliminado: " + product.name))
                .addOnFailureListener(e ->
                        android.util.Log.e("Firebase", "Error eliminando: " + e.getMessage()));
    }

    // Sincronizar productos desde Firestore
    public static void syncProducts(String userId, OnProductsSyncedListener listener) {
        db.collection(COLLECTION_PRODUCTS)
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    java.util.List<Product> products = new java.util.ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Product p = new Product(
                                doc.getString("name"),
                                doc.getString("category"),
                                doc.getLong("expirationDate"),
                                doc.getLong("quantity").intValue()
                        );
                        p.inShoppingList = Boolean.TRUE.equals(doc.getBoolean("inShoppingList"));
                        products.add(p);
                    }
                    listener.onSynced(products);
                })
                .addOnFailureListener(e -> {
                    android.util.Log.e("Firebase", "Error sincronizando: " + e.getMessage());
                    listener.onError(e.getMessage());
                });
    }

    public interface OnProductsSyncedListener {
        void onSynced(java.util.List<Product> products);
        void onError(String error);
    }
}