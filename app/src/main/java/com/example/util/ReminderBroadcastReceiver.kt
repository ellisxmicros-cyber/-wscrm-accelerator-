package com.example.util

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity

class ReminderBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val leadName = intent.getStringExtra(EXTRA_LEAD_NAME) ?: "Cliente"
        val phone = intent.getStringExtra(EXTRA_LEAD_PHONE) ?: ""
        val notes = intent.getStringExtra(EXTRA_NOTES) ?: "Es momento de hacer seguimiento de ventas"

        val channelId = "crm_followup_channel"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Seguimiento de Ventas CRM",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Recordatorios programados para escribir a prospectos por WhatsApp"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        // WhatsApp direct intent
        val waIntent = if (phone.isNotBlank()) {
            val cleanDigits = phone.replace(Regex("[^0-9]"), "")
            Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$cleanDigits")).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
        } else {
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            System.currentTimeMillis().toInt(),
            waIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("⏰ Seguimiento: $leadName")
            .setContentText(notes)
            .setStyle(NotificationCompat.BigTextStyle().bigText("📞 Teléfono: $phone\n📝 $notes\n\nToca para abrir chat en WhatsApp ahora."))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(android.R.drawable.ic_menu_send, "CHATEAR POR WHATSAPP", pendingIntent)
            .build()

        notificationManager.notify((System.currentTimeMillis() % 100000).toInt(), notification)
    }

    companion object {
        const val EXTRA_LEAD_NAME = "extra_lead_name"
        const val EXTRA_LEAD_PHONE = "extra_lead_phone"
        const val EXTRA_NOTES = "extra_notes"

        fun scheduleReminder(
            context: Context,
            leadId: Long,
            leadName: String,
            phone: String,
            notes: String,
            triggerTimeMs: Long
        ) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
                putExtra(EXTRA_LEAD_NAME, leadName)
                putExtra(EXTRA_LEAD_PHONE, phone)
                putExtra(EXTRA_NOTES, notes)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                leadId.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTimeMs, pendingIntent)
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTimeMs, pendingIntent)
            }
        }
    }
}
