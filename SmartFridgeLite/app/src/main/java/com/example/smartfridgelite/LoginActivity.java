package com.example.smartfridgelite;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartfridgelite.databinding.ActivityLoginBinding;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = AppDatabase.getInstance(this);

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Executors.newSingleThreadExecutor().execute(() -> {
                Usuario usuario = db.userDao().login(email, password);

                runOnUiThread(() -> {
                    if (usuario != null) {
                        getSharedPreferences("session", MODE_PRIVATE)
                                .edit()
                                .putString("nombre", usuario.nombre)
                                .putString("email", usuario.email)
                                .putInt("id", usuario.id)
                                .apply();

                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this,
                                "Correo o contraseña incorrectos",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        binding.tvRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }
}