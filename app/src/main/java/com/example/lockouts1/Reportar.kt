package com.example.lockouts1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

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
            val phoneTypeInput = findViewById<EditText>(R.id.phonetypeInput)
            val ubicacionInput = findViewById<EditText>(R.id.phoneubiInput)
            val descripcionInput = findViewById<EditText>(R.id.descripcionInput)

            searchButton.setOnClickListener {
                val numero = phoneInput.text.toString().trim()
                val tipo = phoneTypeInput.text.toString().trim()
                val ubicacion = ubicacionInput.text.toString().trim()
                val descripcion = descripcionInput.text.toString().trim()

                if (numero.isEmpty() || tipo.isEmpty() || ubicacion.isEmpty() || descripcion.isEmpty()) {
                    Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val json = """
        {
            "numero_telefono": "$numero",
            "tipo_telefono": "$tipo",
            "ubicacion": "$ubicacion",
            "descripcion": "$descripcion"
        }
    """.trimIndent()

                val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

                val request = Request.Builder()
                    .url("https://backendlockout.onrender.com/reportar") // 🔁 PON tu URL de Render
                    .post(requestBody)
                    .build()

                OkHttpClient().newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        runOnUiThread {
                            Toast.makeText(this@Reportar, "Error de red", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val responseBody = response.body?.string()
                        val jsonRes = JSONObject(responseBody)
                        val success = jsonRes.getBoolean("success")
                        val message = jsonRes.getString("message")

                        runOnUiThread {
                            Toast.makeText(this@Reportar, message, Toast.LENGTH_SHORT).show()

                        }
                    }
                })
            }

        }
    }
}