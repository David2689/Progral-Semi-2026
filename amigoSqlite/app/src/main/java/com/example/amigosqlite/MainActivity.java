package com.example.amigosqlite;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    DB db;
    Button btn;
    TextView temVal;
    String accion = "nuevo", idAmigo = "", urlFoto;
    Intent tomarFotoIntent;
    FloatingActionButton fab;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = findViewById(R.id.imgFotoAmigo);
        img.setOnClickListener(v -> tomarfoto());
        db = new DB(this);
        btn = findViewById(R.id.btnGuardarAmigo);
        btn.setOnClickListener(v -> guardarAmigo());
        fab = findViewById(R.id.fabListaAmigo);
    }

    private void tomarfoto() {
        tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fotoAmigo = null;
        try {
            fotoAmigo = crearImgAmigo();
            if(fotoAmigo!=null) {
                Uri urifoto = FileProvider.getUriForFile(MainActivity.this, "com.example.amigosqlite.fileprovider", fotoAmigo);
                tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, urifoto);
                startActivityForResult(tomarFotoIntent, 1);
            }else{
                mostrarMsg("Nose pudo tomar la foto");
            }
        } catch (Exception e) {
            mostrarMsg("Error al tomar la foto: " + e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode==1 && resultCode==RESULT_OK){
                img.setImageURI(Uri.parse(urlFoto));
            }else{
                mostrarMsg("No fue posible mostrar la foto");
            }
        } catch (Exception e) {
            mostrarMsg("Error en abrir la camara: "+ e.getMessage());
        }
    }

    private File crearImgAmigo() throws Exception {
        String fechaHoraMs = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),
                fileMane = "foto_" + fechaHoraMs;
        File dirAlmacenamiento = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if (dirAlmacenamiento.exists() == false) {
            dirAlmacenamiento.mkdir();
        }
        File image = File.createTempFile(fileMane, "jpg", dirAlmacenamiento);
        urlFoto = image.getAbsolutePath();
        return image;
    }

    private void guardarAmigo(){
        temVal = findViewById(R.id.txtNombreAmigos);
        String nombre =temVal.getText().toString();
        temVal = findViewById(R.id.txtDireccionAmigos);
        String direccion =temVal.getText().toString();
        temVal = findViewById(R.id.txtTelefonoAmigos);
        String telefono =temVal.getText().toString();
        temVal = findViewById(R.id.txtEmailAmigos);
        String email =temVal.getText().toString();
        temVal = findViewById(R.id.txtDuiAmigos);
        String dui =temVal.getText().toString();

        String[] datos = {idAmigo, nombre,direccion,telefono,email,dui,urlFoto};
        db.administrar_amigos(accion, datos);
        mostrarMsg("Registro de amigo guardado con exito.");;
    }
    private void mostrarMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}