package com.anya.lunaagent

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

/**
 * UI-control bridge. Android decides which actions are permitted.
 * This service intentionally contains no network or cloud code.
 */
class AgentAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Phase 1: observe supported UI events. Command execution is routed
        // through an allow-list in CommandRouter rather than arbitrary taps.
    }

    override fun onInterrupt() = Unit
}
