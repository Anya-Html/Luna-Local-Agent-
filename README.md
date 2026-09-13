# Luna Local Agent

A privacy-first Android AI agent designed for local execution.

## Hard privacy rule
- No `android.permission.INTERNET` in the app manifest.
- No cloud AI API, telemetry, analytics, or remote command service.
- User data and agent state are intended to stay on-device.
- Sensitive/destructive actions should require explicit confirmation.

## Device target
Designed initially for a 6 GB RAM Android phone with a Snapdragon 6-class CPU. The local model should be a small quantized GGUF model; do not commit model binaries to this repository.

## Architecture
- Kotlin + Jetpack Compose UI
- Local command router with an allow-list
- Android Accessibility Service for permitted UI interaction
- Future local LLM runtime: llama.cpp/GGUF
- Future offline STT/TTS
- Future encrypted local memory

## Android limits
Luna cannot bypass Android security boundaries, passwords, secure screens, banking protections, permission prompts, or privileged system controls. Accessibility access must be explicitly enabled by the user.

## Current status
Phase 1 scaffold: Android project, privacy-first manifest, Accessibility bridge, and a small allow-listed settings command router.

Next: wire in a CPU-friendly local GGUF model, offline voice input/output, encrypted local memory, and a confirmation layer for risky actions.
