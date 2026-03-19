package com.example.amigosqlite;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class lista_amigos extends AppCompatActivity {
    Bundle parametros = new Bundle();
    DB db;
    FloatingActionButton fab;
    ListView lstAmigos;
    Cursor cAmigos;
    final ArrayList<Amigos> alAmigos = new ArrayList<Amigos>();
    final ArrayList<Amigos> alAmigosCopia = new ArrayList<Amigos>();
    JSONArray jsonArray;
    JSONObject jsonObject;
    int posicion = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_amigos);
        parametros.putString("accion", "nuevo");
        db= new DB(this);
        fab = findViewById(R.id.fabAgregarAmigos);
        fab.setOnClickListener(v -> abrirActivity());
        obteneramigos();

    }
    private  void abrirActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        Intent.putExtras(parametros);
        startActivity(intent);

    }
    private void obteneramigos(){
        try {
            cAmigos = db.lista_amigos();
            if(cAmigos.moveToFirst() ){
                jsonArray = new JSONArray();
                do {
                    jsonArray = new JSONArray();
                    jsonObject.put("idAmigo", cAmigos.getString(0));
                    jsonObject.put("nombre", cAmigos.getString(0));
                    jsonObject.put("direccion", cAmigos.getString(0));
                    jsonObject.put("telefono", cAmigos.getString(0));
                    jsonObject.put("email", cAmigos.getString(0));
                    jsonObject.put("dui", cAmigos.getString(0));
                    jsonObject.put("foto", cAmigos.getString(0));
                }while (cAmigos.moveToNext());
                mostraramigos();
            }else{
                mostrarMsg("No hay amigos que mostrar");
                abrirActivity();

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void mostraramigos(){
        Toast.makeText(this, msg, Toast.LENGTH_LONG(1));
    }
}