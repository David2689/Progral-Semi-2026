package com.example.smartfridgelite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import com.example.smartfridgelite.databinding.ActivityLoginBinding;
import java.util.concurrent.Executor;
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

        // Verificar si hay sesión guardada para mostrar botón de huella
        SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
        String nombreGuardado = prefs.getString("nombre", "");
        if (!nombreGuardado.isEmpty()) {
            binding.btnBiometric.setVisibility(android.view.View.VISIBLE);
        } else {
            binding.btnBiometric.setVisibility(android.view.View.GONE);
        }

        // Botón iniciar sesión normal
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
                        saveSessionAndGoMain(usuario);
                    } else {
                        Toast.makeText(this,
                                "Correo o contraseña incorrectos",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        // Botón huella digital
        binding.btnBiometric.setOnClickListener(v -> showBiometricPrompt());

        // Ir a registro
        binding.tvRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void showBiometricPrompt() {
        // Verificar si el dispositivo soporta huella
        BiometricManager biometricManager = BiometricManager.from(this);
        int canAuthenticate = biometricManager.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_WEAK);

        if (canAuthenticate != BiometricManager.BIOMETRIC_SUCCESS) {
            Toast.makeText(this,
                    "Tu dispositivo no tiene huella configurada",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Executor executor = ContextCompat.getMainExecutor(this);

        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
                new BiometricPrompt.AuthenticationCallback() {

                    @Override
                    public void onAuthenticationSucceeded(
                            @NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        // Huella correcta — entrar con la sesión guardada
                        SharedPreferences prefs =
                                getSharedPreferences("session", MODE_PRIVATE);
                        String nombre = prefs.getString("nombre", "");
                        if (!nombre.isEmpty()) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Toast.makeText(LoginActivity.this,
                                "Huella no reconocida, intenta de nuevo",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationError(int errorCode,
                                                      @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        if (errorCode != BiometricPrompt.ERROR_USER_CANCELED &&
                                errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                            Toast.makeText(LoginActivity.this,
                                    "Error: " + errString,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Smart Fridge Lite")
                .setSubtitle("Usa tu huella para entrar")
                .setNegativeButtonText("Usar contraseña")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    private void saveSessionAndGoMain(Usuario usuario) {
        getSharedPreferences("session", MODE_PRIVATE)
                .edit()
                .putString("nombre", usuario.nombre)
                .putString("email", usuario.email)
                .putInt("id", usuario.id)
                .apply();

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}