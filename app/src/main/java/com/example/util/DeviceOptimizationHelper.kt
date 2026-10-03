package com.example.util

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast

object DeviceOptimizationHelper {

    fun isBatteryOptimizationIgnored(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val pm = context.getSystemService(Context.POWER_SERVICE) as? PowerManager
            return pm?.isIgnoringBatteryOptimizations(context.packageName) ?: true
        }
        return true
    }

    @SuppressLint("BatteryLife")
    fun requestBatteryOptimizationBypass(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val pm = context.getSystemService(Context.POWER_SERVICE) as? PowerManager
            if (pm != null && !pm.isIgnoringBatteryOptimizations(context.packageName)) {
                try {
                    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                        data = Uri.parse("package:${context.packageName}")
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(intent)
                    return
                } catch (_: Exception) {
                    // Fallback to settings list
                }

                try {
                    val fallbackIntent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(fallbackIntent)
                } catch (e: Exception) {
                    Toast.makeText(context, "No se pudo abrir ajustes de batería", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Las optimizaciones de batería ya están desactivadas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun openAutostartSettings(context: Context): Boolean {
        val packageName = context.packageName

        val autostartIntents = listOf(
            // Xiaomi / MIUI / HyperOS
            Intent().setComponent(
                ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")
            ),
            Intent("miui.intent.action.OP_AUTO_START").addCategory(Intent.CATEGORY_DEFAULT),

            // Samsung
            Intent().setComponent(
                ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity")
            ),
            Intent().setComponent(
                ComponentName("com.samsung.android.sm", "com.samsung.android.sm.ui.battery.BatteryActivity")
            ),
            Intent().setComponent(
                ComponentName("com.samsung.android.sm", "com.samsung.android.sm.app.dashboard.SmartManagerDashBoardActivity")
            ),

            // Huawei / Honor
            Intent().setComponent(
                ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")
            ),
            Intent().setComponent(
                ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity")
            ),

            // Oppo / Realme / ColorOS
            Intent().setComponent(
                ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")
            ),
            Intent().setComponent(
                ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")
            ),
            Intent().setComponent(
                ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")
            ),

            // Vivo / Funtouch / OriginOS / iQOO
            Intent().setComponent(
                ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")
            ),
            Intent().setComponent(
                ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.PurviewTabActivity")
            ),

            // OnePlus
            Intent().setComponent(
                ComponentName("com.oneplus.security", "com.oneplus.security.chainlaunch.view.ChainLaunchAppListActivity")
            ),

            // Transsion (Infinix / Tecno)
            Intent().setComponent(
                ComponentName("com.transsion.phonemanager", "com.transsion.phonemanager.settings.AutoRunSettingsActivity")
            )
        )

        for (intent in autostartIntents) {
            try {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val resolved = context.packageManager.queryIntentActivities(intent, 0)
                if (resolved.isNotEmpty()) {
                    context.startActivity(intent)
                    Toast.makeText(context, "Apertura de menú de Inicio Automático", Toast.LENGTH_SHORT).show()
                    return true
                }
            } catch (_: Exception) {
                // Try next
            }
        }

        // Generic fallback: Application Details Settings
        try {
            val appDetailsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:$packageName")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(appDetailsIntent)
            Toast.makeText(context, "Abre 'Permisos' o 'Batería' para permitir ejecución en segundo plano", Toast.LENGTH_LONG).show()
            return true
        } catch (e: Exception) {
            Toast.makeText(context, "No se pudo abrir los ajustes del dispositivo", Toast.LENGTH_SHORT).show()
            return false
        }
    }
}
