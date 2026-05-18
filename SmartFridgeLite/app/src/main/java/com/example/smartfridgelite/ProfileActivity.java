package com.example.smartfridgelite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartfridgelite.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtener datos de sesión
        SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
        String nombre = prefs.getString("nombre", "Usuario");
        String email = prefs.getString("email", "");

        // Mostrar datos
        binding.tvNombre.setText(nombre);
        binding.tvEmail.setText(email);

        // Avatar con inicial del nombre
        String inicial = nombre.substring(0, 1).toUpperCase();
        binding.tvAvatar.setText(inicial);

        // Cerrar sesión
        binding.btnLogout.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Cerrar sesión")
                    .setMessage("¿Seguro que quieres cerrar sesión?")
                    .setPositiveButton("Sí, salir", (dialog, which) -> {
                        // Limpiar sesión
                        getSharedPreferences("session", MODE_PRIVATE)
                                .edit()
                                .clear()
                                .apply();
                        startActivity(new Intent(this, LoginActivity.class));
                        finishAffinity();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
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