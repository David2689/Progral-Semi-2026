package com.example.amigosqlite;

import android.widget.BaseAdapter;

import java.util.Base64;

public class utilidades {
    static String url_consulta = "http://192.168.1.5:5984/amigo/_design/amigos/_view/amigos";
    static String url_mta = "http://192.168.1.5:5984/amigo";
    static String user = "david123";
    static String passwd = "12345";
    static String credencialesCodificadas = Base64.getEncoder().encodeToString((user +":"+ passwd).getBytes());
    public String generarUnicoId(){
        return java.util.UUID.randomUUID().toString();
    }
}