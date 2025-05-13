package com.example.lockouts1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.SignInButton
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class Registro : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    // Lanzador del intent de Google Sign-In
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Toast.makeText(this, "Error al iniciar sesión con Google", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Inicializar Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Configurar Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // debe estar en strings.xml
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Referencias a vistas
        val editTextNombre = findViewById<EditText>(R.id.editTextNombreRegistro)
        val editTextCorreo = findViewById<EditText>(R.id.editTextCorreo)
        val editTextFechaNacimiento = findViewById<EditText>(R.id.editTextFechaNacimiento)
        val editTextContrasena = findViewById<EditText>(R.id.editTextContrasenaRegistro)
        val editTextRepetirContrasena = findViewById<EditText>(R.id.editTextRepetirContrasena)
        val buttonCrearCuenta = findViewById<Button>(R.id.buttonCrearCuenta)
        val googleButton = findViewById<SignInButton>(R.id.googleSignInButton)

        // Registro manual
        buttonCrearCuenta.setOnClickListener {
            val nombre = editTextNombre.text.toString()
            val correo = editTextCorreo.text.toString()
            val fechaNacimiento = editTextFechaNacimiento.text.toString()
            val contrasena = editTextContrasena.text.toString()
            val repetirContrasena = editTextRepetirContrasena.text.toString()

            if (nombre.isEmpty() || correo.isEmpty() || fechaNacimiento.isEmpty() ||
                contrasena.isEmpty() || repetirContrasena.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            } else if (contrasena != repetirContrasena) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            } else {
                val client = OkHttpClient()

                val json = """
    {
        "nombre": "$nombre",
        "correo": "$correo",
        "fechaNacimiento": "$fechaNacimiento",
        "contrasena": "$contrasena"
    }
""".trimIndent()

                val requestBody = RequestBody.create(
                    "application/json; charset=utf-8".toMediaTypeOrNull(), json
                )

                val request = Request.Builder()
                    .url("https://backendlockout.onrender.com/registro") // 🔁 tu URL real aquí
                    .post(requestBody)
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        runOnUiThread {
                            Toast.makeText(this@Registro, "Error de red", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val respuesta = response.body?.string()
                        val success = JSONObject(respuesta).getBoolean("success")
                        runOnUiThread {
                            if (success) {
                                Toast.makeText(this@Registro, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@Registro, MainActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this@Registro, "Error al registrar", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })

            }
        }

        // Registro con Google
        googleButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        }
    }

    // Función que autentica en Firebase con Google
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    Toast.makeText(this, "Bienvenido, ${user?.displayName}", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, Pag1::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Autenticación fallida", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
