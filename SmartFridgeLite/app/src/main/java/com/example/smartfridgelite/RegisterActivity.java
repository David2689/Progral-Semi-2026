package com.example.smartfridgelite;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartfridgelite.databinding.ActivityRegisterBinding;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = AppDatabase.getInstance(this);

        // Botón crear cuenta
        binding.btnRegister.setOnClickListener(v -> {
            String nombre = binding.etNombre.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            String confirmPassword = binding.etConfirmPassword.getText().toString().trim();

            // Validaciones
            if (nombre.isEmpty() || email.isEmpty() ||
                    password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Executors.newSingleThreadExecutor().execute(() -> {
                // Verificar si el email ya existe
                Usuario existente = db.userDao().findByEmail(email);

                runOnUiThread(() -> {
                    if (existente != null) {
                        Toast.makeText(this,
                                "Este correo ya está registrado",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Executors.newSingleThreadExecutor().execute(() -> {
                            Usuario nuevoUsuario = new Usuario(nombre, email, password);
                            db.userDao().insert(nuevoUsuario);

                            runOnUiThread(() -> {
                                Toast.makeText(this,
                                        "¡Cuenta creada exitosamente! 🎉",
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, LoginActivity.class));
                                finish();
                            });
                        });
                    }
                });
            });
        });

        // Volver al login
        binding.tvLogin.setOnClickListener(v -> finish());
    }
}
