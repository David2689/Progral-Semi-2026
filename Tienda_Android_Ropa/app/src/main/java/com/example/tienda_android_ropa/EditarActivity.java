package com.example.tienda_android_ropa;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.activity.result.*;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
public class EditarActivity extends AppCompatActivity {

    private EditText etCodigo, etNombre, etMarca, etTalla, etPrecio, etDescripcion;
    private ImageView imgPreview;
    private String fotoPath = "";
    private int productoId;
    private ProductoDAO dao;

    private final ActivityResultLauncher<String> pickImage =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    fotoPath = uri.toString();
                    Glide.with(this).load(uri).transform(new CircleCrop()).into(imgPreview);
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar); // Reutilizamos el mismo layout
        setTitle("Editar Producto");

        dao = new ProductoDAO(this);
        etCodigo = findViewById(R.id.etCodigo);
        etNombre = findViewById(R.id.etNombre);
        etMarca = findViewById(R.id.etMarca);
        etTalla = findViewById(R.id.etTalla);
        etPrecio = findViewById(R.id.etPrecio);
        etDescripcion = findViewById(R.id.etDescripcion);
        imgPreview = findViewById(R.id.imgPreview);
        productoId = getIntent().getIntExtra("id", -1);
        etCodigo.setText(getIntent().getStringExtra("codigo"));
        etNombre.setText(getIntent().getStringExtra("nombre"));
        etMarca.setText(getIntent().getStringExtra("marca"));
        etTalla.setText(getIntent().getStringExtra("talla"));
        etPrecio.setText(String.valueOf(getIntent().getDoubleExtra("precio", 0)));
        etDescripcion.setText(getIntent().getStringExtra("descripcion"));
        fotoPath = getIntent().getStringExtra("foto");

        if (fotoPath != null && !fotoPath.isEmpty()) {
            Glide.with(this).load(fotoPath).transform(new CircleCrop()).into(imgPreview);
        }
        findViewById(R.id.btnSeleccionarFoto).setOnClickListener(v -> pickImage.launch("image/*"));

        findViewById(R.id.btnGuardar).setOnClickListener(v -> {
            Producto p = new Producto(
                    etCodigo.getText().toString().trim(),
                    etNombre.getText().toString().trim(),
                    etMarca.getText().toString().trim(),
                    etTalla.getText().toString().trim(),
                    Double.parseDouble(etPrecio.getText().toString().trim()),
                    etDescripcion.getText().toString().trim(),
                    fotoPath
            );
            p.setId(productoId);
            dao.actualizar(p);
            Toast.makeText(this, "Actualizado ✓", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
