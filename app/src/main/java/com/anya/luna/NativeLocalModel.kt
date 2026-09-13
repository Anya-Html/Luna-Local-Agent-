package com.anya.luna

/** Kotlin/JNI boundary for the on-device llama.cpp/GGUF runtime. */
class NativeLocalModel : LocalModelEngine {
    init {
        System.loadLibrary("luna_native")
    }

    override fun isReady(): Boolean = nativeAvailable()

    override fun generate(prompt: String, maxTokens: Int): String {
        require(maxTokens in 1..4096) { "maxTokens must be between 1 and 4096" }
        if (!isReady()) return "LOCAL_MODEL_NOT_INSTALLED"
        return nativeGenerate(prompt, maxTokens)
    }

    private external fun nativeAvailable(): Boolean
    private external fun nativeGenerate(prompt: String, maxTokens: Int): String
}
