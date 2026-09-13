package com.anya.luna

/**
 * Deterministic offline parser used before the GGUF model is installed.
 * It never calls a network service and only emits allow-listed intents.
 */
object OfflineIntentParser {
    fun parse(text: String): AgentIntent {
        val c = text.trim().lowercase().replace(Regex("\\s+"), " ")
        if (c.isBlank()) return AgentIntent.Unknown

        return when {
            c == "home" || c == "go home" || c.contains("home screen") || c.contains("होम स्क्रीन") -> AgentIntent.Home
            c == "back" || c == "go back" || c.contains("वापस") -> AgentIntent.Back
            c == "recents" || c.contains("recent apps") || c.contains("हाल के ऐप") -> AgentIntent.Recents
            c.contains("wifi") || c.contains("wi-fi") || c.contains("वाईफाई") || c.contains("वाई-फाई") -> AgentIntent.WifiSettings
            c.contains("bluetooth") || c.contains("ब्लूटूथ") -> AgentIntent.BluetoothSettings
            c.contains("accessibility") || c.contains("एक्सेसिबिलिटी") -> AgentIntent.AccessibilitySettings
            else -> extractOpenApp(c)?.let { AgentIntent.OpenApp(it) } ?: AgentIntent.Unknown
        }
    }

    private fun extractOpenApp(c: String): String? {
        val prefixes = listOf("open ", "launch ", "ओपन ", "खोलो ")
        return prefixes.firstNotNullOfOrNull { prefix ->
            if (c.startsWith(prefix) && c.removePrefix(prefix).isNotBlank()) c.removePrefix(prefix).trim() else null
        }
    }
}
