package com.lucas.xboxcontrol

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

  val root = LinearLayout(this).apply {
   orientation = LinearLayout.VERTICAL
   gravity = Gravity.CENTER
   setPadding(24, 18, 24, 18)
  }
  root.addView(TextView(this).apply {
   text = "XBOX CONTROL\n\nAtive as permissões e abra o Roblox com o painel flutuante."
   textSize = 18f
   gravity = Gravity.CENTER
  })
  root.addView(button("1. Permitir sobreposição") {
   if (!Settings.canDrawOverlays(this))
    startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
   else Toast.makeText(this, "Permissão já concedida", Toast.LENGTH_SHORT).show()
  })
  root.addView(button("2. Ativar acessibilidade") {
   startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
  })
  root.addView(button("3. Iniciar somente o controle") {
   startOverlay()
  })
  root.addView(button("ABRIR ROBLOX + CONTROLE") {
   if (!Settings.canDrawOverlays(this)) {
    Toast.makeText(this, "Conceda primeiro a permissão de sobreposição", Toast.LENGTH_LONG).show()
   } else {
    startOverlay()
    val launch = packageManager.getLaunchIntentForPackage("com.roblox.client")
    if (launch != null) {
     launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
     startActivity(launch)
    } else {
     Toast.makeText(this, "Roblox não foi encontrado instalado", Toast.LENGTH_LONG).show()
    }
   }
  })
  root.addView(button("Parar controle") { stopService(Intent(this, OverlayService::class.java)) })
  root.addView(TextView(this).apply {
   text = "\nProtótipo: o painel é overlay. Os botões ainda enviam toques experimentais; os analógicos visuais ainda não fazem movimento contínuo. A compatibilidade com Roblox precisa ser testada."
   textSize = 12f
  })
  setContentView(root)
 }

 private fun startOverlay() {
  if (!Settings.canDrawOverlays(this)) {
   Toast.makeText(this, "Conceda a sobreposição primeiro", Toast.LENGTH_LONG).show()
   return
  }
  startService(Intent(this, OverlayService::class.java))
  Toast.makeText(this, "Controle flutuante iniciado", Toast.LENGTH_SHORT).show()
 }

 private fun button(label: String, action: () -> Unit) =
  Button(this).apply { text = label; setOnClickListener { action() } }
}