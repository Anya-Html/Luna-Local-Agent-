package com.anya.luna

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val modelStore = ModelStore(this)

        setContent {
            var selectedModel by remember { mutableStateOf<ModelInfo?>(modelStore.listModels().firstOrNull()) }
            var status by remember { mutableStateOf("Select a GGUF model to begin.") }
            var output by remember { mutableStateOf("") }
            var busy by remember { mutableStateOf(false) }

            val picker = rememberLauncherForActivityResult(
                ActivityResultContracts.OpenDocument()
            ) { uri: Uri? ->
                if (uri == null) return@rememberLauncherForActivityResult
                runCatching { modelStore.importGguf(uri) }
                    .onSuccess {
                        selectedModel = it
                        output = ""
                        status = "Imported locally: ${it.name}"
                    }
                    .onFailure {
                        status = "Model import failed: ${it.message ?: "unknown error"}"
                    }
            }

            fun runLocalInference(model: ModelInfo) {
                if (busy) return
                busy = true
                status = "Running locally…"
                output = ""
                Thread {
                    val result = runCatching {
                        val engine = NativeLocalModel(model.absolutePath)
                        engine.generate(
                            AgentPrompt.build("Say exactly: Luna local model is working."),
                            maxTokens = 32
                        )
                    }.getOrElse { "LOCAL_MODEL_ERROR: ${it.message ?: "unknown error"}" }
                    runOnUiThread {
                        output = result
                        status = if (result.startsWith("LOCAL_MODEL_")) "Local inference failed." else "Local inference completed."
                        busy = false
                    }
                }.start()
            }

            MaterialTheme {
                Column(
                    Modifier.fillMaxSize().padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Luna Local Agent", style = MaterialTheme.typography.headlineMedium)
                    Text("100% local command engine — cloud AI is disabled.")

                    Button(onClick = { picker.launch(arrayOf("application/octet-stream", "application/*")) }) {
                        Text("Select GGUF Model")
                    }

                    selectedModel?.let { model ->
                        Text("Model: ${model.name}")
                        Text("Size: ${model.sizeBytes / (1024 * 1024)} MB")
                        Text("SHA-256: ${model.sha256.take(16)}…")

                        Button(onClick = { runLocalInference(model) }, enabled = !busy) {
                            Text(if (busy) "Running…" else "Test Local Inference")
                        }
                    } ?: Text("No GGUF model imported.")

                    Button(onClick = { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }) {
                        Text("Enable Accessibility")
                    }

                    Text(status)
                    if (output.isNotBlank()) {
                        Text("Model output:")
                        Text(output)
                    }
                }
            }
        }
    }
}
