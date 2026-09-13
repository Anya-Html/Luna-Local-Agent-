# Luna Local Agent

Privacy-first Android AI agent designed for on-device execution.

## Privacy contract
- No cloud AI API.
- No `android.permission.INTERNET` in the app manifest.
- No analytics or telemetry dependency.
- Commands are intended to be processed locally.
- Accessibility is opt-in and controlled by Android system settings.

## Current prototype
- Compose launcher/status UI
- Local command router
- Wi-Fi, Bluetooth and Accessibility settings intents
- Accessibility global Back/Home/Recents bridge

## Roadmap
1. Offline speech recognition
2. Small quantized GGUF model via llama.cpp
3. Offline TTS
4. More Android public APIs and permitted accessibility actions
5. Local encrypted memory
6. Confirmation layer for destructive/privacy-sensitive actions

Luna must never bypass Android security boundaries, secure screens, passwords, banking protections, or permission prompts.
