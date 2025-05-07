package com.example.lockouts1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class Perfil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> {
                    startActivity(Intent(this, Pag1::class.java))
                    true
                }
                R.id.nav_reportar -> {
                    startActivity(Intent(this, Reportar::class.java))
                    true
                }
                R.id.nav_registro -> {
                    startActivity(Intent(this, Registro::class.java))
                    true
                }
                R.id.nav_perfil -> {
                    // Ya estamos aquí
                    true
                }
                else -> false
            }
        }

        // Marca la pestaña de perfil como seleccionada
        bottomNavigation.selectedItemId = R.id.nav_perfil
    }
}
