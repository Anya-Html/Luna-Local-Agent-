package com.anya.lunaagent

import android.content.Context
import android.content.Intent
import android.provider.Settings

sealed interface AgentAction {
    data class OpenSettings(val action: String) : AgentAction
}

class CommandRouter(private val context: Context) {
    fun route(command: String): AgentAction? {
        val text = command.trim().lowercase()
        return when {
            text == "open settings" || text == "settings kholo" ->
                AgentAction.OpenSettings(Settings.ACTION_SETTINGS)
            text == "open wifi settings" || text == "wifi kholo" ->
                AgentAction.OpenSettings(Settings.ACTION_WIFI_SETTINGS)
            text == "open bluetooth settings" || text == "bluetooth kholo" ->
                AgentAction.OpenSettings(Settings.ACTION_BLUETOOTH_SETTINGS)
            else -> null
        }
    }

    fun execute(action: AgentAction) {
        when (action) {
            is AgentAction.OpenSettings -> {
                context.startActivity(Intent(action.action).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }
    }
}
