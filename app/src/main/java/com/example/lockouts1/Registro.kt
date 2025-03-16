package com.example.lockouts1

import android.os.Bundle
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

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Registro : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Referencias a los elementos de la interfaz
        val editTextNombre = findViewById<EditText>(R.id.editTextNombreRegistro)
        val editTextCorreo = findViewById<EditText>(R.id.editTextCorreo)
        val editTextFechaNacimiento = findViewById<EditText>(R.id.editTextFechaNacimiento)
        val editTextContrasena = findViewById<EditText>(R.id.editTextContrasenaRegistro)
        val editTextRepetirContrasena = findViewById<EditText>(R.id.editTextRepetirContrasena)
        val buttonCrearCuenta = findViewById<Button>(R.id.buttonCrearCuenta)

        // Acción del botón "Crear Cuenta"
        buttonCrearCuenta.setOnClickListener {
            val nombre = editTextNombre.text.toString()
            val correo = editTextCorreo.text.toString()
            val fechaNacimiento = editTextFechaNacimiento.text.toString()
            val contrasena = editTextContrasena.text.toString()
            val repetirContrasena = editTextRepetirContrasena.text.toString()

            // Validación de campos vacíos
            if (nombre.isEmpty() || correo.isEmpty() || fechaNacimiento.isEmpty() || contrasena.isEmpty() || repetirContrasena.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
            // Validación de contraseñas coincidentes
            else if (contrasena != repetirContrasena) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                // Aquí puedes agregar la lógica para guardar los datos en una base de datos

                // Redirigir a la pantalla de inicio de sesión (MainActivity)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Cierra la actividad de registro
            }
        }
    }
}