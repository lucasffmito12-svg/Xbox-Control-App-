package com.lucas.xboxcontrol

import android.app.Activity
import android.content.Intent
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
  val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER; setPadding(26, 26, 26, 26) }
  root.addView(TextView(this).apply { text = "XBOX CONTROL\n\nControle virtual experimental para Android. Conceda as permissões para iniciar."; textSize = 18f; gravity = Gravity.CENTER })
  root.addView(button("1. Permitir sobreposição") {
   if (!Settings.canDrawOverlays(this)) startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
   else Toast.makeText(this, "Permissão já concedida", Toast.LENGTH_SHORT).show()
  })
  root.addView(button("2. Ativar acessibilidade") { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) })
  root.addView(button("3. Iniciar controle flutuante") {
   if (!Settings.canDrawOverlays(this)) Toast.makeText(this, "Conceda a sobreposição primeiro", Toast.LENGTH_LONG).show()
   else { startService(Intent(this, OverlayService::class.java)); Toast.makeText(this, "Controle iniciado", Toast.LENGTH_SHORT).show() }
  })
  root.addView(button("Parar controle") { stopService(Intent(this, OverlayService::class.java)) })
  root.addView(TextView(this).apply { text = "\nAviso: os botões enviam gestos de toque por Acessibilidade. Compatibilidade com Roblox não é garantida. Não é um controle Bluetooth/XInput."; textSize = 13f })
  setContentView(root)
 }
 private fun button(label: String, action: () -> Unit) = Button(this).apply { text = label; setOnClickListener { action() } }
}
