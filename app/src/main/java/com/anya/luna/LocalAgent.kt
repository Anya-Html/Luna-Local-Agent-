package com.anya.luna

import android.content.Context

class LocalAgent(context: Context) {
    private val appContext = context.applicationContext
    private val router = CommandRouter(appContext)

    fun handle(text: String): String {
        val command = CommandParser.normalize(text)
        if (command.isBlank()) return "Say a command."
        if (AgentSafety.requiresConfirmation(command)) {
            return "CONFIRMATION_REQUIRED: This action can change data, communicate, or spend money."
        }

        return when (val intent = OfflineIntentParser.parse(command)) {
            AgentIntent.Home -> "HOME_ACTION_REQUIRED"
            AgentIntent.Back -> "BACK_ACTION_REQUIRED"
            AgentIntent.Recents -> "RECENTS_ACTION_REQUIRED"
            AgentIntent.WifiSettings -> router.route("wifi")
            AgentIntent.BluetoothSettings -> router.route("bluetooth")
            AgentIntent.AccessibilitySettings -> router.route("accessibility")
            is AgentIntent.OpenApp -> router.route("open ${intent.label}")
            AgentIntent.Unknown -> router.route(command)
        }
    }
}
