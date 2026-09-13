plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.anya.luna"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.anya.luna"
        // llama.cpp's Android build path is validated against API 28+.
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"

        ndkVersion = "27.2.12479018"
        ndk {
            abiFilters += listOf("arm64-v8a")
        }
    }

    buildFeatures { compose = true }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
            arguments += listOf(
                "-DANDROID_PLATFORM=android-28",
                "-DGGML_NATIVE=OFF",
                "-DGGML_OPENMP=OFF",
                "-DGGML_LLAMAFILE=OFF",
                "-DLLAMA_OPENSSL=OFF"
            )
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.compose.ui:ui:1.7.8")
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
}
