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
            AgentIntent.Home -> global("home")
            AgentIntent.Back -> global("back")
            AgentIntent.Recents -> global("recents")
            AgentIntent.WifiSettings -> router.route("wifi")
            AgentIntent.BluetoothSettings -> router.route("bluetooth")
            AgentIntent.AccessibilitySettings -> router.route("accessibility")
            is AgentIntent.OpenApp -> router.route("open ${intent.label}")
            AgentIntent.Unknown -> router.route(command)
        }
    }

    private fun global(action: String): String {
        val service = AgentAccessibilityService.instance
            ?: return "Accessibility is not enabled. Enable it in Luna settings first."
        val ok = when (action) {
            "home" -> service.performHome()
            "back" -> service.performBack()
            "recents" -> service.performRecents()
            else -> false
        }
        return if (ok) "Done" else "Android rejected that action."
    }
}
