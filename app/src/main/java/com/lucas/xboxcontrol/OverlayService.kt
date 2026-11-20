package com.lucas.xboxcontrol

import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.IBinder
import android.provider.Settings
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class OverlayService : Service() {
 private var wm: WindowManager? = null
 private var panel: LinearLayout? = null

 override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
  if (!Settings.canDrawOverlays(this)) { stopSelf(); return START_NOT_STICKY }
  if (panel == null) showPanel()
  return START_NOT_STICKY
 }

 private fun showPanel() {
  wm = getSystemService(WINDOW_SERVICE) as WindowManager
  val root = LinearLayout(this).apply {
   orientation = LinearLayout.HORIZONTAL
   gravity = Gravity.CENTER_VERTICAL
   setPadding(8, 5, 8, 5)
   setBackgroundColor(0xB820242B.toInt())
  }

  val left = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER }
  left.addView(controlButton("LB") { sendTap(4) })
  left.addView(controlButton("LT") { sendTap(5) })
  root.addView(left)

  val dpad = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER }
  dpad.addView(controlButton("↑") { sendTap(9) })
  val dpadMid = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER }
  dpadMid.addView(controlButton("←") { sendTap(10) })
  dpadMid.addView(controlButton("＋") { })
  dpadMid.addView(controlButton("→") { sendTap(12) })
  dpad.addView(dpadMid)
  dpad.addView(controlButton("↓") { sendTap(11) })
  root.addView(dpad)

  val leftStick = TextView(this).apply {
   text = "◉\nANALÓGICO L"
   gravity = Gravity.CENTER
   setTextColor(Color.WHITE)
   textSize = 11f
   setBackgroundColor(0x883F4854.toInt())
   setPadding(18, 18, 18, 18)
  }
  root.addView(leftStick)

  val center = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER }
  center.addView(controlButton("VIEW") { sendTap(6) })
  center.addView(controlButton("XBOX") { })
  center.addView(controlButton("MENU") { sendTap(7) })
  root.addView(center)

  val rightStick = TextView(this).apply {
   text = "◉\nANALÓGICO R"
   gravity = Gravity.CENTER
   setTextColor(Color.WHITE)
   textSize = 11f
   setBackgroundColor(0x883F4854.toInt())
   setPadding(18, 18, 18, 18)
  }
  root.addView(rightStick)

  val face = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER }
  face.addView(controlButton("Y") { sendTap(0) })
  val faceMid = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER }
  faceMid.addView(controlButton("X") { sendTap(1) })
  faceMid.addView(controlButton("B") { sendTap(2) })
  face.addView(faceMid)
  face.addView(controlButton("A") { sendTap(3) })
  root.addView(face)

  val right = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER }
  right.addView(controlButton("RB") { sendTap(8) })
  right.addView(controlButton("RT") { sendTap(7) })
  right.addView(controlButton("FECHAR") { stopSelf() })
  root.addView(right)

  val params = WindowManager.LayoutParams(
   WindowManager.LayoutParams.MATCH_PARENT,
   WindowManager.LayoutParams.WRAP_CONTENT,
   WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
   WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
   PixelFormat.TRANSLUCENT
  ).apply { gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL }

  wm?.addView(root, params)
  panel = root
 }

 private fun controlButton(label: String, action: () -> Unit) =
  Button(this).apply {
   text = label
   textSize = 10f
   minWidth = 0
   minimumWidth = 0
   setPadding(5, 2, 5, 2)
   setOnClickListener { action() }
  }

 private fun sendTap(index: Int) {
  val svc = TouchAccessibilityService.instance
  if (svc == null) {
   Toast.makeText(this, "Ative Xbox Control em Acessibilidade", Toast.LENGTH_SHORT).show()
   return
  }
  val dm = resources.displayMetrics
  val coords = listOf(
   .78f to .72f, .72f to .64f, .84f to .64f, .78f to .56f,
   .20f to .18f, .30f to .18f, .50f to .18f, .70f to .18f,
   .80f to .18f, .20f to .55f, .14f to .62f, .20f to .69f, .26f to .62f
  )
  val p = coords[index.coerceIn(0, coords.lastIndex)]
  svc.tap(dm.widthPixels * p.first, dm.heightPixels * p.second)
 }

 override fun onDestroy() {
  panel?.let { try { wm?.removeView(it) } catch (_: Exception) {} }
  panel = null
  super.onDestroy()
 }
 override fun onBind(intent: Intent?): IBinder? = null
}