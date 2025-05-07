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
                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Pag1::class.java)
                startActivity(intent)
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