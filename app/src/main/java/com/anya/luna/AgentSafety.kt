package com.anya.luna

object AgentSafety {
    private val destructive = listOf(
        "delete", "remove", "uninstall", "send message", "send sms", "call ",
        "purchase", "buy ", "pay", "transfer", "delete करो", "डिलीट", "पे करो"
    )

    fun requiresConfirmation(text: String): Boolean {
        val c = text.trim().lowercase()
        return destructive.any(c::contains)
    }
}
