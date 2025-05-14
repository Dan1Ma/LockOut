package com.example.lockouts1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException



class Noticias : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var bottomNavigation: BottomNavigationView
    private val client = OkHttpClient()
    private val noticiasList = mutableListOf<Noticia>()
    private lateinit var adapter: NoticiaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noticias)

        recycler = findViewById(R.id.recyclerNoticias)
        bottomNavigation = findViewById(R.id.bottomNavigationView)

        adapter = NoticiaAdapter(noticiasList) { noticia ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(noticia.url))
            startActivity(intent)
        }

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        cargarNoticias()

        bottomNavigation.selectedItemId = R.id.nav_alertas

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

                    true
                }
                R.id.nav_perfil -> {
                    startActivity(Intent(this, Perfil::class.java))
                    true
                }
                else -> false
            }
        }

    }

    private fun cargarNoticias() {
        val request = Request.Builder()
            .url("https://backendlockout.onrender.com/noticias") // tu URL
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@Noticias, "Error al cargar noticias", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val json = JSONObject(body)
                val noticias = json.getJSONArray("noticias")

                noticiasList.clear()
                for (i in 0 until noticias.length()) {
                    val obj = noticias.getJSONObject(i)
                    noticiasList.add(
                        Noticia(
                            titulo = obj.getString("titulo"),
                            resumen = obj.getString("resumen"),
                            url = obj.getString("url")
                        )
                    )
                }

                runOnUiThread {
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }
}
