package com.example.tienda_android_ropa;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.activity.result.*;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
public class AgregarActivity extends AppCompatActivity {

    private EditText etCodigo, etNombre, etMarca, etTalla, etPrecio, etDescripcion;
    private ImageView imgPreview;
    private String fotoPath = "";
    private ProductoDAO dao;

    private final ActivityResultLauncher<String> pickImage =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    fotoPath = uri.toString();
                    Glide.with(this).load(uri)
                            .transform(new CircleCrop())
                            .into(imgPreview);
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);
        setTitle("Agregar Producto");

        dao = new ProductoDAO(this);
        etCodigo = findViewById(R.id.etCodigo);
        etNombre = findViewById(R.id.etNombre);
        etMarca = findViewById(R.id.etMarca);
        etTalla = findViewById(R.id.etTalla);
        etPrecio = findViewById(R.id.etPrecio);
        etDescripcion = findViewById(R.id.etDescripcion);
        imgPreview = findViewById(R.id.imgPreview);
        Button btnFoto = findViewById(R.id.btnSeleccionarFoto);
        Button btnGuardar = findViewById(R.id.btnGuardar);
        btnFoto.setOnClickListener(v -> pickImage.launch("image/*"));

        btnGuardar.setOnClickListener(v -> {
            if (validar()) {
                Producto p = new Producto(
                        etCodigo.getText().toString().trim(),
                        etNombre.getText().toString().trim(),
                        etMarca.getText().toString().trim(),
                        etTalla.getText().toString().trim(),
                        Double.parseDouble(etPrecio.getText().toString().trim()),
                        etDescripcion.getText().toString().trim(),
                        fotoPath
                );
                long result = dao.insertar(p);
                if (result > 0) {
                    Toast.makeText(this, "Producto guardado ✓", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Error: código duplicado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean validar() {
        if (etCodigo.getText().toString().trim().isEmpty()) {
            etCodigo.setError("Requerido"); return false;
        }
        if (etNombre.getText().toString().trim().isEmpty()) {
            etNombre.setError("Requerido"); return false;
        }
        if (etPrecio.getText().toString().trim().isEmpty()) {
            etPrecio.setError("Requerido"); return false;
        }
        return true;
    }
}
