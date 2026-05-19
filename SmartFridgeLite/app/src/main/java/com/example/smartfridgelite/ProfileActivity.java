package com.example.smartfridgelite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartfridgelite.databinding.ActivityProfileBinding;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
        String nombre = prefs.getString("nombre", "Usuario");
        String email = prefs.getString("email", "");

        binding.tvNombre.setText(nombre);
        binding.tvEmail.setText(email);
        loadProfilePhoto(nombre);

        // Launcher para galería
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        saveAndShowPhoto(imageUri);
                    }
                }
        );

        // Launcher para cámara
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                        saveAndShowBitmap(photo);
                    }
                }
        );

        // Botón cambiar foto — muestra opciones
        binding.btnChangePhoto.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Foto de perfil")
                    .setItems(new String[]{
                            "📷 Tomar foto",
                            "🖼️ Elegir de galería"
                    }, (dialog, which) -> {
                        if (which == 0) {
                            // Abrir cámara
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            cameraLauncher.launch(cameraIntent);
                        } else {
                            // Abrir galería
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            galleryLauncher.launch(galleryIntent);
                        }
                    })
                    .show();
        });

        // Cerrar sesión
        binding.btnLogout.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Cerrar sesión")
                    .setMessage("¿Seguro que quieres cerrar sesión?")
                    .setPositiveButton("Sí, salir", (dialog, which) -> {
                        getSharedPreferences("session", MODE_PRIVATE)
                                .edit().clear().apply();
                        startActivity(new Intent(this, LoginActivity.class));
                        finishAffinity();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }

    private void loadProfilePhoto(String nombre) {
        try {
            InputStream is = openFileInput("profile_photo.jpg");
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            setCircularPhoto(bitmap);
            binding.tvAvatar.setText("");
        } catch (Exception e) {
            binding.tvAvatar.setText(nombre.substring(0, 1).toUpperCase());
            binding.ivProfilePhoto.setImageDrawable(null);
        }
    }

    private void saveAndShowPhoto(Uri imageUri) {
        try {
            InputStream is = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            saveAndShowBitmap(bitmap);
        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar la foto", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveAndShowBitmap(Bitmap bitmap) {
        try {
            // Recortar a cuadrado centrado para que el círculo se vea bien
            int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
            int x = (bitmap.getWidth() - size) / 2;
            int y = (bitmap.getHeight() - size) / 2;
            Bitmap cropped = Bitmap.createBitmap(bitmap, x, y, size, size);

            // Guardar
            FileOutputStream fos = openFileOutput("profile_photo.jpg", MODE_PRIVATE);
            cropped.compress(Bitmap.CompressFormat.JPEG, 85, fos);
            fos.close();

            // Mostrar circular
            setCircularPhoto(cropped);
            binding.tvAvatar.setText("");
            Toast.makeText(this, "Foto actualizada ✅", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error al guardar la foto", Toast.LENGTH_SHORT).show();
        }
    }

    private void setCircularPhoto(Bitmap bitmap) {
        // Crear bitmap circular
        Bitmap circular = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        android.graphics.Canvas canvas = new android.graphics.Canvas(circular);
        android.graphics.Paint paint = new android.graphics.Paint();
        paint.setAntiAlias(true);
        canvas.drawCircle(bitmap.getWidth() / 2f,
                bitmap.getHeight() / 2f,
                bitmap.getWidth() / 2f, paint);
        paint.setXfermode(new android.graphics.PorterDuffXfermode(
                android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        binding.ivProfilePhoto.setImageBitmap(circular);
        binding.ivProfilePhoto.setBackground(null);
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