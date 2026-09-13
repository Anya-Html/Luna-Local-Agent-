package com.anya.luna

import android.content.Context
import android.net.Uri
import java.io.File
import java.security.MessageDigest

/** Keeps imported GGUF models inside app-private storage. No network access is used. */
class ModelStore(private val context: Context) {
    private val modelDir = File(context.filesDir, "models").apply { mkdirs() }

    fun importGguf(uri: Uri, displayName: String? = null): ModelInfo {
        val name = sanitizeName(displayName ?: "model.gguf")
        require(name.endsWith(".gguf", ignoreCase = true)) { "Only .gguf models are supported." }

        val destination = File(modelDir, name)
        context.contentResolver.openInputStream(uri).use { input ->
            requireNotNull(input) { "Could not open the selected model." }
            destination.outputStream().use { output -> input.copyTo(output) }
        }
        return ModelInfo(destination.name, destination.length(), sha256(destination), destination.absolutePath)
    }

    fun listModels(): List<ModelInfo> = modelDir.listFiles()
        ?.filter { it.isFile && it.extension.equals("gguf", ignoreCase = true) }
        ?.sortedBy { it.name.lowercase() }
        ?.map { ModelInfo(it.name, it.length(), sha256(it), it.absolutePath) }
        ?: emptyList()

    private fun sha256(file: File): String {
        val digest = MessageDigest.getInstance("SHA-256")
        file.inputStream().use { input ->
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            while (true) {
                val count = input.read(buffer)
                if (count <= 0) break
                digest.update(buffer, 0, count)
            }
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }

    private fun sanitizeName(raw: String): String {
        val base = raw.substringAfterLast('/').substringAfterLast('\\').trim()
        return base.replace(Regex("[^A-Za-z0-9._-]"), "_").ifBlank { "model.gguf" }
    }
}

data class ModelInfo(
    val name: String,
    val sizeBytes: Long,
    val sha256: String,
    val absolutePath: String
)
