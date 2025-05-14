package com.example.lockouts1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class Perfil : AppCompatActivity() {

    private lateinit var nombreInput: EditText
    private lateinit var correoInput: EditText
    private lateinit var fechaInput: EditText
    private lateinit var guardarButton: Button
    private lateinit var btnCerrarSesion: Button
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        nombreInput = findViewById(R.id.editTextNombrePerfil)
        correoInput = findViewById(R.id.editTextCorreoPerfil)
        fechaInput = findViewById(R.id.editTextFechaPerfil)
        guardarButton = findViewById(R.id.buttonGuardarCambios)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)


        // Simulación: precarga de datos de usuario (puedes cargar desde Firebase o SharedPreferences)
        nombreInput.setText("Daniel")
        correoInput.setText("danieldpmatm@gmail.com")
        fechaInput.setText("2025-05-14")

        guardarButton.setOnClickListener {
            val nombre = nombreInput.text.toString()
            val correo = correoInput.text.toString()
            val fecha = fechaInput.text.toString()

            if (nombre.isEmpty() || correo.isEmpty() || fecha.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val json = """
                {
                    "nombre": "$nombre",
                    "correo": "$correo",
                    "fechaNacimiento": "$fecha"
                }
            """.trimIndent()

            val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            val request = Request.Builder()
                .url("https://backendlockout.onrender.com/actualizar-perfil") // Cambia por tu URL real
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(this@Perfil, "Error al guardar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    runOnUiThread {
                        Toast.makeText(this@Perfil, "Datos guardados con éxito", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
        btnCerrarSesion.setOnClickListener {
            // Si estás usando Firebase para login con Google, también puedes hacer FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Navegación inferior
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigation.selectedItemId = R.id.nav_perfil

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
                R.id.nav_alertas -> {
                    startActivity(Intent(this, Noticias::class.java))
                    true
                }
                R.id.nav_perfil -> true
                else -> false
            }
        }
    }
}



