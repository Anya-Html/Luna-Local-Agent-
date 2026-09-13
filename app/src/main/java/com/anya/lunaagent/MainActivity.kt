package com.anya.lunaagent

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { LunaHome(onAccessibilityClick = { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }) }
    }
}

@Composable
private fun LunaHome(onAccessibilityClick: () -> Unit) {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Luna Local Agent", style = MaterialTheme.typography.headlineMedium)
            Text("Local-first Android agent. No INTERNET permission is declared.")
            Text("Phase 1: safe device actions + Accessibility integration.")
            Button(onClick = onAccessibilityClick) { Text("Enable Accessibility") }
        }
    }
}
