package com.example.parcial_1;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String[] UNIDADES = {
            "Pie Cuadrado", "Vara Cuadrada", "Yarda Cuadrada",
            "Metro Cuadrado", "Tarea", "Manzana", "Hectárea"
    };

    private static final double[] A_M2 = {
            0.092903, 0.698737, 0.836127,
            1.0, 6250.0, 6989.0, 10000.0
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ── TabHost ──────────────────────────────────────────
        TabHost tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        tabHost.addTab(tabHost.newTabSpec("Agua")
                .setContent(R.id.tab1).setIndicator("💧 Agua"));

        tabHost.addTab(tabHost.newTabSpec("Area")
                .setContent(R.id.tab2).setIndicator("📐 Área"));

        // ── Pestaña 1: Agua ──────────────────────────────────
        EditText etMetros = findViewById(R.id.etMetros);
        TextView tvAgua   = findViewById(R.id.tvResultadoAgua);

        findViewById(R.id.btnCalcularAgua).setOnClickListener(v -> {
            String s = etMetros.getText().toString().trim();
            if (s.isEmpty()) {
                Toast.makeText(this, "Ingrese los metros consumidos", Toast.LENGTH_SHORT).show();
                return;
            }

            double m = Double.parseDouble(s);
            double total = Math.min(m, 10) * 1.50;
            if (m > 10) total += Math.min(m - 10, 10) * 2.00;
            if (m > 20) total += Math.min(m - 20, 10) * 3.00;
            if (m > 30) total += (m - 30) * 4.50;

            tvAgua.setText(String.format("Metros consumidos: %.2f m³\n💲 Total a pagar: $%.2f", m, total));
            tvAgua.setVisibility(View.VISIBLE);
        });

        // ── Pestaña 2: Área ──────────────────────────────────
        EditText etArea        = findViewById(R.id.etValorArea);
        Spinner  spinnerOrigen = findViewById(R.id.spinnerOrigen);
        Spinner  spinnerDest   = findViewById(R.id.spinnerDestino);
        TextView tvArea        = findViewById(R.id.tvResultadoArea);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, UNIDADES);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrigen.setAdapter(adapter);
        spinnerDest.setAdapter(adapter);

        findViewById(R.id.btnConvertir).setOnClickListener(v -> {
            String s = etArea.getText().toString().trim();
            if (s.isEmpty()) {
                Toast.makeText(this, "Ingrese un valor", Toast.LENGTH_SHORT).show();
                return;
            }

            double valor     = Double.parseDouble(s);
            int    origen    = spinnerOrigen.getSelectedItemPosition();
            int    destino   = spinnerDest.getSelectedItemPosition();
            double resultado = (valor * A_M2[origen]) / A_M2[destino];

            tvArea.setText(String.format("%.4f %s\n=\n%.6f %s",
                    valor, UNIDADES[origen], resultado, UNIDADES[destino]));
            tvArea.setVisibility(View.VISIBLE);
        });
    }
}