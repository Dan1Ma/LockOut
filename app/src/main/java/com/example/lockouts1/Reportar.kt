package com.example.lockouts1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView

class Reportar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reportar)

        val phoneInput = findViewById<EditText>(R.id.phoneInput)
        val searchButton = findViewById<Button>(R.id.searchButton)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> {
                    startActivity(Intent(this, Pag1::class.java))
                    true
                }
                R.id.nav_reportar -> {
                    // Ya estamos aquí
                    true
                }
                R.id.nav_registro -> {
                    startActivity(Intent(this, Registro::class.java))
                    true
                }
                R.id.nav_perfil -> {
                    startActivity(Intent(this, Perfil::class.java))

                    true
                }
                else -> false
            }
        }

// Opcional: seleccionar "Reportar" como ítem activo por defecto
        bottomNavigation.selectedItemId = R.id.nav_reportar


        searchButton.setOnClickListener {
            val phoneNumber = phoneInput.text.toString().trim()
            Toast.makeText(this, "Numero Reportado: $phoneNumber", Toast.LENGTH_SHORT).show()
            // Aquí puedes llamar a tu función de búsqueda, API, etc.
        }
    }
}