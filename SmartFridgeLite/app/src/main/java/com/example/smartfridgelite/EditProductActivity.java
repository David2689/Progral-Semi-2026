package com.example.smartfridgelite;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartfridgelite.databinding.ActivityEditProductBinding;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditProductActivity extends AppCompatActivity {

    private ActivityEditProductBinding binding;
    private ProductRepository repository;
    private Calendar selectedDate = Calendar.getInstance();
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        repository = new ProductRepository(getApplication());

        // Recibir datos del producto a editar
        product = new Product(
                getIntent().getStringExtra("name"),
                getIntent().getStringExtra("category"),
                getIntent().getLongExtra("expirationDate", 0),
                getIntent().getIntExtra("quantity", 1)
        );
        product.id = getIntent().getIntExtra("id", 0);
        product.inShoppingList = getIntent().getBooleanExtra("inShoppingList", false);

        // Rellenar campos con datos actuales
        binding.etName.setText(product.name);
        binding.etCategory.setText(product.category);
        binding.etQuantity.setText(String.valueOf(product.quantity));

        // Mostrar fecha actual
        selectedDate.setTimeInMillis(product.expirationDate);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        binding.tvSelectedDate.setText("Fecha actual: " +
                sdf.format(new Date(product.expirationDate)));

        // Selector de nueva fecha
        binding.btnPickDate.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(this,
                    (view, year, month, day) -> {
                        selectedDate.set(year, month, day, 23, 59, 0);
                        binding.tvSelectedDate.setText("Nueva fecha: " +
                                sdf.format(selectedDate.getTime()));
                    },
                    selectedDate.get(Calendar.YEAR),
                    selectedDate.get(Calendar.MONTH),
                    selectedDate.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        });

        // Guardar cambios
        binding.btnSave.setOnClickListener(v -> {
            String name = binding.etName.getText().toString().trim();
            String category = binding.etCategory.getText().toString().trim();
            String qtyStr = binding.etQuantity.getText().toString().trim();

            if (name.isEmpty() || category.isEmpty() || qtyStr.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            product.name = name;
            product.category = category;
            product.quantity = Integer.parseInt(qtyStr);
            product.expirationDate = selectedDate.getTimeInMillis();

            repository.update(product);
            Toast.makeText(this, "Producto actualizado ✅", Toast.LENGTH_SHORT).show();
            finish();
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
