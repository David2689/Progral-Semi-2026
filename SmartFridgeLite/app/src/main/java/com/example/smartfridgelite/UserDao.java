package com.example.smartfridgelite;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {

    @Insert
    void insert(Usuario usuario);

    // Buscar usuario por email y password para login
    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :password LIMIT 1")
    Usuario login(String email, String password);

    // Verificar si el email ya existe al registrarse
    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    Usuario findByEmail(String email);
}