package com.example.lockouts1

import android.os.Bundle
import android.os.Build

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.lockouts1.ui.theme.LockOutS1Theme
import android.content.Intent
import android.content.pm.PackageManager

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ✅ Solicitar permiso para READ_CALL_LOG en tiempo de ejecución
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CALL_LOG),
                1002
            )
        }


        // ✅ Pedir permiso de notificaciones en Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }


        // Referencias a los elementos de la interfaz
        val editTextNombre = findViewById<EditText>(R.id.editTextNombre)
        val editTextContrasena = findViewById<EditText>(R.id.editTextContrasena)
        val buttonIniciarSesion = findViewById<Button>(R.id.buttonIniciarSesion)
        val buttonRegistro = findViewById<Button>(R.id.buttonRegistro)

        // Acción del botón "Iniciar Sesión"
        buttonIniciarSesion.setOnClickListener {
            val nombre = editTextNombre.text.toString()
            val contrasena = editTextContrasena.text.toString()

            if (nombre.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                val client = OkHttpClient()

                val json = """
    {
        "nombre": "$nombre",
        "contrasena": "$contrasena"
    }
""".trimIndent()

                val requestBody = RequestBody.create(
                    "application/json; charset=utf-8".toMediaTypeOrNull(), json
                )

                val request = Request.Builder()
                    .url("https://backendlockout.onrender.com/login") // 🔁 tu URL real aquí
                    .post(requestBody)
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "Error de red", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val respuesta = response.body?.string()
                        val jsonResponse = JSONObject(respuesta)
                        val success = jsonResponse.getBoolean("success")
                        val message = jsonResponse.getString("message")

                        runOnUiThread {
                            if (success) {
                                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@MainActivity, Pag1::class.java))
                            } else {
                                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })

                // Aquí vamos a agregar la lógica para validar el inicio de sesión con una base de datos
            }
        }

        // Acción del botón "Registrarse"
        buttonRegistro.setOnClickListener {
            val intent = Intent(this, Registro::class.java)
            startActivity(intent)
        }
    }

}