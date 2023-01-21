package at.fh.mappdev.rootnavigator.reminder

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import at.fh.mappdev.rootnavigator.NotificationInfo
import at.fh.mappdev.rootnavigator.R
import at.fh.mappdev.rootnavigator.auth.LoginActivity

class NotificationReceiver : BroadcastReceiver() {
    @SuppressLint("LaunchActivityFromNotification")
    override fun onReceive(context: Context, intent: Intent) {
        val newintent = Intent(context, LoginActivity::class.java)
        newintent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, newintent, PendingIntent.FLAG_IMMUTABLE)
        val id = intent.getIntExtra("ID", 1)
        val description = intent.getStringExtra("DESCRIPTION")

        Log.d("NotificationReceiver", "onReceive: $description, $id")
        val notification = NotificationCompat.Builder(context, NotificationInfo.NOTIFICATIONID)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
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