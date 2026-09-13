package com.anya.luna

import android.content.Context
import android.content.Intent
import android.provider.Settings

class CommandRouter(private val context: Context) {
    fun route(command: String): String {
        val c = command.trim().lowercase()
        return when {
            c == "home" || c == "go home" || c.contains("home screen") || c.contains("होम स्क्रीन") ->
                "Use the Accessibility bridge for Home; settings navigation is kept separate."
            c.contains("wifi") || c.contains("wi-fi") || c.contains("वाईफाई") || c.contains("वाई-फाई") ->
                launch(Settings.ACTION_WIFI_SETTINGS, "Wi-Fi settings opened")
            c.contains("bluetooth") || c.contains("ब्लूटूथ") ->
                launch(Settings.ACTION_BLUETOOTH_SETTINGS, "Bluetooth settings opened")
            c.contains("accessibility") || c.contains("असिस्ट") || c.contains("एक्सेसिबिलिटी") ->
                launch(Settings.ACTION_ACCESSIBILITY_SETTINGS, "Accessibility settings opened")
            else -> launchAppIfInstalled(c)
        }
    }

    private fun launchAppIfInstalled(command: String): String {
        val apps = context.packageManager.getInstalledApplications(0)
        val match = apps.firstOrNull { app ->
            val label = context.packageManager.getApplicationLabel(app).toString().lowercase()
            command.contains("open $label") || command.contains("launch $label") ||
                command.contains("खोलो $label") || command.contains("ओपन $label")
        } ?: return "I don't have a safe local action for that command yet."

        val intent = context.packageManager.getLaunchIntentForPackage(match.packageName)
            ?: return "That app cannot be launched safely."
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        return "Opened ${context.packageManager.getApplicationLabel(match)}"
    }

    private fun launch(action: String, message: String): String {
        context.startActivity(Intent(action).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        return message
    }
}
