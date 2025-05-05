package com.example.lockouts1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import com.google.android.material.bottomnavigation.BottomNavigationView


class Pag1 : AppCompatActivity() {

    // Componentes de la pantalla
    lateinit var etPrefijo: EditText
    lateinit var etNumero: EditText
    lateinit var btnBuscar: Button
    lateinit var tvResultado: TextView
    lateinit var bottomNavigation: BottomNavigationView

    // Cliente HTTP
    val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pag1)

        // Asociar variables a los componentes visuales
        etPrefijo = findViewById(R.id.etPrefijo)
        etNumero = findViewById(R.id.etNumero)
        btnBuscar = findViewById(R.id.btnBuscar)
        tvResultado = findViewById(R.id.tvResultado)
        bottomNavigation = findViewById(R.id.bottomNavigationView)

        // Listener de navegación
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> {
                    Toast.makeText(this, "Inicio seleccionado", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_busquedas -> {
                    Toast.makeText(this, "Búsquedas seleccionado", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_reportar -> {
                    Toast.makeText(this, "Reportar seleccionado", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_estafas_comunes -> {
                    Toast.makeText(this, "Estafas Comunes seleccionada", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        // Selecciona la primera opción por defecto
        bottomNavigation.selectedItemId = R.id.nav_inicio

        // Acción del botón
        btnBuscar.setOnClickListener {
            val prefijo = etPrefijo.text.toString().trim()
            val numero = etNumero.text.toString().trim()

            if (prefijo.isNotEmpty() && numero.isNotEmpty()) {
                buscarNumero(prefijo, numero)
            } else {
                tvResultado.text = "Ingresa prefijo y número"
            }
        }
    }

    // Función para llamar a la API (sin cambios)
    fun buscarNumero(prefijo: String, numero: String) {
        val url = "http://10.0.2.2/buscar_numeros/buscar_numero.php?prefijo=$prefijo&numero=$numero"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    tvResultado.text = "Error: ${e.message}"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                runOnUiThread {
                    try {
                        val json = JSONObject(responseData)
                        val estatus = json.getString("estatus")
                        tvResultado.text = "Estatus: $estatus"
                    } catch (e: Exception) {
                        tvResultado.text = "Respuesta inesperada: $responseData"
                    }
                }
            }
        })
    }
}
