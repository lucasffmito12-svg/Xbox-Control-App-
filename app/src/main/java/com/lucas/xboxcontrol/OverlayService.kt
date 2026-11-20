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
  val box = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(8, 6, 8, 6); setBackgroundColor(0xCC20242B.toInt()) }
  val title = TextView(this).apply { text = "XBOX CONTROL - arraste aqui"; setTextColor(Color.WHITE); textSize = 12f; gravity = Gravity.CENTER }
  box.addView(title)
  val top = LinearLayout(this)
  listOf("Y", "X", "B", "A").forEachIndexed { i, s -> top.addView(controlButton(s) { sendTap(i) }) }
  box.addView(top)
  val middle = LinearLayout(this)
  listOf("LB", "LT", "MENU", "RT", "RB").forEachIndexed { i, s -> middle.addView(controlButton(s) { sendTap(i + 4) }) }
  box.addView(middle)
  val bottom = LinearLayout(this)
  listOf("↑", "←", "↓", "→").forEachIndexed { i, s -> bottom.addView(controlButton(s) { sendTap(i + 9) }) }
  box.addView(bottom)
  box.addView(controlButton("FECHAR") { stopSelf() })
  val params = WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT).apply { gravity = Gravity.TOP or Gravity.START; x = 18; y = 130 }
  wm?.addView(box, params); panel = box
  title.setOnTouchListener(object : View.OnTouchListener {
   var downX = 0f; var downY = 0f; var originX = 0; var originY = 0
   override fun onTouch(v: View, e: MotionEvent): Boolean {
    when (e.action) {
     MotionEvent.ACTION_DOWN -> { downX = e.rawX; downY = e.rawY; originX = params.x; originY = params.y; return true }
     MotionEvent.ACTION_MOVE -> { params.x = originX + (e.rawX - downX).toInt(); params.y = originY + (e.rawY - downY).toInt(); wm?.updateViewLayout(box, params); return true }
    }; return true
   }
  })
 }
 private fun controlButton(label: String, action: () -> Unit) = Button(this).apply { text = label; textSize = 10f; minWidth = 0; setPadding(4, 1, 4, 1); setOnClickListener { action() } }
 private fun sendTap(index: Int) {
  val svc = TouchAccessibilityService.instance
  if (svc == null) { Toast.makeText(this, "Ative Xbox Control em Acessibilidade", Toast.LENGTH_SHORT).show(); return }
  val dm = resources.displayMetrics
  val coords = listOf(.78f to .72f, .72f to .64f, .84f to .64f, .78f to .56f, .20f to .18f, .30f to .18f, .50f to .18f, .70f to .18f, .80f to .18f, .20f to .55f, .14f to .62f, .20f to .69f, .26f to .62f)
  val p = coords[index.coerceIn(0, coords.lastIndex)]
  svc.tap(dm.widthPixels * p.first, dm.heightPixels * p.second)
 }
 override fun onDestroy() { panel?.let { try { wm?.removeView(it) } catch (_: Exception) {} }; panel = null; super.onDestroy() }
 override fun onBind(intent: Intent?): IBinder? = null
}
