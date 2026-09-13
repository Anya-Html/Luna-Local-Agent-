package com.anya.luna

import android.content.Context

class LocalAgent(context: Context) {
    private val router = CommandRouter(context.applicationContext)

    fun handle(text: String): String {
        val command = CommandParser.normalize(text)
        if (command.isBlank()) return "Say a command."
        if (CommandParser.isDestructive(command)) {
            return "Confirmation required before I can perform that action."
        }
        return router.route(command)
    }
}
