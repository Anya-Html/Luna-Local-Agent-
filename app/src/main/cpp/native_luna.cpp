#include <jni.h>

extern "C" JNIEXPORT jboolean JNICALL
Java_com_anya_luna_NativeLocalModel_nativeAvailable(JNIEnv*, jobject) {
    // This returns false until the reviewed llama.cpp/GGUF backend is linked.
    // Keeping the boundary explicit prevents a fake "AI enabled" state.
    return JNI_FALSE;
}
