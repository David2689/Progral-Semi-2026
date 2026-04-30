package com.example.tiendaropa2;

public class CouchDBHelper {

    private static final String BASE_URL = "http://192.168.80.194:5984";
    private static final String DATABASE = "david";
    private static final String USUARIO = "david123";
    private static final String PASSWORD = "12345";

    public static String getUrl() {
        return BASE_URL + "/" + DATABASE;
    }

    public static String getCredenciales() {
        String credenciales = USUARIO + ":" + PASSWORD;
        return android.util.Base64.encodeToString(
                credenciales.getBytes(), android.util.Base64.NO_WRAP);
    }
}