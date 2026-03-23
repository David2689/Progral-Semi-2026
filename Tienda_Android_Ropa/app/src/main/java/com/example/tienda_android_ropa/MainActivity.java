package com.example.tienda_android_ropa;

import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ProductoDAO dao;
    private ProductoAdapter adapter;
    private List<Producto> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));
        dao = new ProductoDAO(this);
        RecyclerView rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fabAgregar);
        fab.setOnClickListener(v ->
                startActivity(new Intent(this, AgregarActivity.class)));

        cargarLista();
    }
    private void cargarLista() {
        lista = dao.obtenerTodos();
        if (adapter == null) {
            adapter = new ProductoAdapter(this, lista, new ProductoAdapter.OnItemClickListener() {
                @Override
                public void onEditar(Producto p) {
                    Intent i = new Intent(MainActivity.this, EditarActivity.class);
                    i.putExtra("id", p.getId());
                    i.putExtra("codigo", p.getCodigo());
                    i.putExtra("nombre", p.getNombre());
                    i.putExtra("marca", p.getMarca());
                    i.putExtra("talla", p.getTalla());
                    i.putExtra("precio", p.getPrecio());
                    i.putExtra("descripcion", p.getDescripcion());
                    i.putExtra("foto", p.getFotoPath());
                    startActivity(i);
                }
                @Override
                public void onEliminar(Producto p) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Eliminar")
                            .setMessage("¿Eliminar " + p.getNombre() + "?")
                            .setPositiveButton("Sí", (d, w) -> {
                                dao.eliminar(p.getId());
                                Toast.makeText(MainActivity.this,
                                        "Eliminado", Toast.LENGTH_SHORT).show();
                                cargarLista();
                            })
                            .setNegativeButton("Cancelar", null)
                            .show();
                }
            });
            RecyclerView rv = findViewById(R.id.recyclerView);
            rv.setAdapter(adapter);
        } else {
            adapter.actualizarLista(lista);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        cargarLista();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_buscar) {
            startActivity(new Intent(this, BuscarActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}