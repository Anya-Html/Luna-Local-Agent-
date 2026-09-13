package com.anya.luna

/** Tiny deterministic parser used before the local LLM is added. */
object CommandParser {
    fun normalize(text: String): String = text.trim().lowercase().replace(Regex("\\s+"), " ")

    fun isDestructive(text: String): Boolean = listOf(
        "delete", "uninstall", "send message", "call", "purchase", "pay"
    ).any { normalize(text).contains(it) }
}
