package com.example.lockouts1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class CallReceiver : BroadcastReceiver() {

    private val client = OkHttpClient()

    override fun onReceive(context: Context, intent: Intent) {
        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

        if (state == TelephonyManager.EXTRA_STATE_RINGING && incomingNumber != null) {
            Log.d("CallReceiver", "Llamada entrante: $incomingNumber")

            // Limpia el número de caracteres no numéricos
            val rawNumber = incomingNumber.replace("\\D".toRegex(), "")

            // Asegura que tenga el prefijo 34
            val cleanNumber = if (rawNumber.startsWith("34")) rawNumber else "34$rawNumber"

            val url = "https://backendlockout.onrender.com/buscar-numero?numero=$cleanNumber"

            val request = Request.Builder()
                .url(url)
                .get()
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("CallReceiver", "Error al consultar el backend: ${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    if (!response.isSuccessful || body == null) return

                    try {
                        val json = JSONObject(body)
                        if (json.optBoolean("success", false)) {
                            val datos = json.getJSONObject("datos")
                            val descripcion = datos.optString("descripcion", "Este número ha sido reportado")
                            val reportes = datos.optInt("numero_reportes", 1)

                            val mensaje = "$descripcion (Reportado $reportes veces)"
                            NotificationHelper.showNotification(context, "Llamada sospechosa", mensaje)
                        }
                    } catch (e: Exception) {
                        Log.e("CallReceiver", "Error procesando JSON: ${e.message}")
                    }
                }
            })
        }
    }
}
