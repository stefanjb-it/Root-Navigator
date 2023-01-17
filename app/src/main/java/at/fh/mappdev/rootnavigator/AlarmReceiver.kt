package at.fh.mappdev.rootnavigator

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlin.random.Random

class AlarmReceiver : BroadcastReceiver() {
    @SuppressLint("LaunchActivityFromNotification")
    override fun onReceive(context: Context, intent: Intent) {
        val newintent = Intent(context, LoginActivity::class.java)
        newintent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getBroadcast(context, 0, newintent, PendingIntent.FLAG_IMMUTABLE)

        val id = intent.getIntExtra("ID", Random.nextInt(1, 999))
        val description = intent.getStringExtra("DESCRIPTION")

        val notification = NotificationCompat.Builder(context, NotificationInfo.NOTIFICATIONID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Reminder")
            .setContentText(description)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .build()

        val nmc = NotificationManagerCompat.from(context)
        nmc.notify(id, notification)
    }
}