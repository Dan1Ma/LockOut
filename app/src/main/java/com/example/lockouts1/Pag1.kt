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
import android.content.Intent


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

                    true
                }
                R.id.nav_reportar -> {
                    val intent = Intent(this, Reportar::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_alertas -> {
                    val intent = Intent(this, Noticias::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_perfil -> {
                    val intent = Intent(this, Perfil::class.java)
                    startActivity(intent)
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

    // Función para llamar a la API
    fun buscarNumero(prefijo: String, numero: String) {
        val numeroCompleto = "$prefijo$numero"
        val url = "https://backendlockout.onrender.com/buscar-numero?numero=$numeroCompleto"


        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    tvResultado.text = "Este número de teléfono ha sido reportado recientemente, tenga cuidado"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                runOnUiThread {
                    try {
                        val json = JSONObject(responseData)
                        val success = json.getBoolean("success")
                        if (success) {
                            val datos = json.getJSONObject("datos")
                            val tipo = datos.getString("tipo_telefono")
                            val ubicacion = datos.getString("ubicacion")
                            val descripcion = datos.getString("descripcion")
                            val reportes = datos.getInt("numero_reportes")

                            tvResultado.text = """
                                Tipo: $tipo
                                Ubicación: $ubicacion
                                Descripción: $descripcion
                                Nº de Reportes: $reportes
                            """.trimIndent()
                        } else {
                            val mensaje = json.getString("message")
                            tvResultado.text = "No encontrado: $mensaje"
                        }
                    } catch (e: Exception) {
                        tvResultado.text = "Respuesta inesperada: $responseData"
                    }
                }
            }

        })
    }
}
