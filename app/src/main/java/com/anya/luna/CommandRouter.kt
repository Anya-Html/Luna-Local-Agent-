package com.anya.luna

import android.content.Context
import android.content.Intent
import android.provider.Settings

class CommandRouter(private val context: Context) {
    fun route(command: String): String {
        val c = command.trim().lowercase()
        return when {
            c == "home" || c == "go home" -> launch(Settings.ACTION_HOME_SETTINGS, "Home settings opened")
            c.contains("wifi") -> launch(Settings.ACTION_WIFI_SETTINGS, "Wi-Fi settings opened")
            c.contains("bluetooth") -> launch(Settings.ACTION_BLUETOOTH_SETTINGS, "Bluetooth settings opened")
            c.contains("accessibility") -> launch(Settings.ACTION_ACCESSIBILITY_SETTINGS, "Accessibility settings opened")
            else -> "I don't have a safe local action for that command yet."
        }
    }

    private fun launch(action: String, message: String): String {
        context.startActivity(Intent(action).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        return message
    }
}
