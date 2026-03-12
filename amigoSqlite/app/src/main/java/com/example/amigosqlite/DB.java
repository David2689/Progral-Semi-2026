package com.example.amigosqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {
    private static final String DataBase_Name="amigos";
    private static final int DataBase_Version=1;
    private static final String SQLDb="create table amigos (idAmigo integer primary key autoincrement, nombre TEXT, direccion TEXT,telefono TEXT, email TEXT, dui TEXT, urlfoto TEXT)";

    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DataBase_Name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SQLiteDatabase.execSQL(SQLDb);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public String administrar_amigos(String accion, String[] datos){
        try {
            SQLiteDatabase db = getWritableDatabase();
            String mensaje ="ok", sql ="";
            switch (accion){
                case "nuevo":
                sql = "INSERT INTO amigos (nombre,dereccion,telefono,email,dui,urlfoto) VALUES('"+datos[1]+"','"+datos[2]+"','"+datos[3]+"','"+datos[4]+"','"+datos[5]+"','"+datos[6]+"')'";
                break;
                case "modificar":
                sql ="'UPDATE amigos SET nombre='"+datos[1]+"',direccion='"+datos[2]+"',telefono='"+datos[3]
                break;
                case ="eliminar":
                sql ="";
                break;
            }
        }
    }
}
