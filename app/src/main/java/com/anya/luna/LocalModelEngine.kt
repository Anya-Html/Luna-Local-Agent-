package com.anya.luna

/**
 * Privacy boundary for the local language model.
 * Implementations must run entirely on-device and must not perform network I/O.
 */
interface LocalModelEngine {
    fun isReady(): Boolean
    fun generate(prompt: String, maxTokens: Int = 256): String
}

/** Safe placeholder until a native llama.cpp/GGUF runtime is bundled. */
class UnavailableLocalModelEngine : LocalModelEngine {
    override fun isReady(): Boolean = false

    override fun generate(prompt: String, maxTokens: Int): String =
        "LOCAL_MODEL_NOT_INSTALLED"
}
