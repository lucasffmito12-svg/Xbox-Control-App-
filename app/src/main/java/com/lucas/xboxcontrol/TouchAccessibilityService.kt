package com.lucas.xboxcontrol

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.view.accessibility.AccessibilityEvent

class TouchAccessibilityService : AccessibilityService() {
 override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
 override fun onInterrupt() {}
 fun tap(x: Float, y: Float) {
  val path = Path().apply { moveTo(x, y) }
  val gesture = GestureDescription.Builder().addStroke(GestureDescription.StrokeDescription(path, 0, 70)).build()
  dispatchGesture(gesture, null, null)
 }
 companion object { @Volatile var instance: TouchAccessibilityService? = null; private set }
 override fun onServiceConnected() { super.onServiceConnected(); instance = this }
 override fun onDestroy() { if (instance === this) instance = null; super.onDestroy() }
}
