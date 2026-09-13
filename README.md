# Luna Local Agent

Privacy-first Android AI agent designed for on-device execution.

## Privacy contract
- No cloud AI API.
- No `android.permission.INTERNET` in the app manifest.
- No analytics or telemetry dependency.
- Runtime command processing is designed to stay on-device.
- Accessibility is opt-in and controlled by Android system settings.
- No security-boundary bypassing.

## Current architecture
Voice/Input -> Offline STT -> Local GGUF LLM -> Structured Intent -> Safety Gate -> Android Action -> Offline TTS

The repository currently contains the local-model abstraction and deterministic offline intent fallback. A native llama.cpp/GGUF implementation is intentionally not claimed until its native runtime is actually bundled and tested.

## Current prototype
- Jetpack Compose launcher/status UI
- Local command router
- Structured intent model
- Deterministic Hindi/English offline fallback parser
- Wi-Fi, Bluetooth and Accessibility settings intents
- Accessibility global Back/Home/Recents bridge
- Local model interface with no network implementation

## Roadmap
1. Bundle/build llama.cpp through Android NDK
2. Load a small quantized GGUF model from app-local storage/assets
3. Add fully offline speech recognition
4. Add fully offline speech output
5. Expand permitted Android public APIs and accessibility actions
6. Add local encrypted memory
7. Add explicit confirmation UI for destructive/privacy-sensitive actions

Luna must never bypass Android security boundaries, secure screens, passwords, banking protections, or permission prompts.
