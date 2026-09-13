package com.anya.luna

/** Kotlin/JNI boundary for the future on-device GGUF runtime. */
class NativeLocalModel : LocalModelEngine {
    init {
        System.loadLibrary("luna_native")
    }

    override fun isReady(): Boolean = nativeAvailable()

    override fun generate(prompt: String, maxTokens: Int): String {
        if (!isReady()) return "LOCAL_MODEL_NOT_INSTALLED"
        return "LOCAL_MODEL_READY_BUT_INFERENCE_NOT_BOUND"
    }

    private external fun nativeAvailable(): Boolean
}
