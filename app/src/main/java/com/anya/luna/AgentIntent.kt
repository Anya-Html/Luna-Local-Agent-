package com.anya.luna

/** Structured local intent. The model/parser may propose one of these safe intents. */
sealed interface AgentIntent {
    data object Home : AgentIntent
    data object Back : AgentIntent
    data object Recents : AgentIntent
    data object WifiSettings : AgentIntent
    data object BluetoothSettings : AgentIntent
    data object AccessibilitySettings : AgentIntent
    data class OpenApp(val label: String) : AgentIntent
    data object Unknown : AgentIntent
}
