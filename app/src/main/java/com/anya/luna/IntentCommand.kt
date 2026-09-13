package com.anya.luna

sealed interface IntentCommand {
    data object Home : IntentCommand
    data object Back : IntentCommand
    data object Recents : IntentCommand
    data object WifiSettings : IntentCommand
    data object BluetoothSettings : IntentCommand
    data object AccessibilitySettings : IntentCommand
    data class OpenApp(val label: String) : IntentCommand
    data object None : IntentCommand
}
