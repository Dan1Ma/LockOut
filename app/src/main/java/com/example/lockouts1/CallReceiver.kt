package com.example.lockouts1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log

class CallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

        if (state == TelephonyManager.EXTRA_STATE_RINGING && incomingNumber != null) {
            Log.d("CallReceiver", "Llamada entrante: $incomingNumber")

            val cleanNumber = incomingNumber.replace("\\D".toRegex(), "") // quita +, espacios, guiones
            if (cleanNumber.endsWith("613929030")) {
                NotificationHelper.showNotification(context, incomingNumber)
            }

        }
    }
}
