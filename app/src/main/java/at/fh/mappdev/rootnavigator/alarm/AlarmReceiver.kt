package at.fh.mappdev.rootnavigator.alarm

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import at.fh.mappdev.rootnavigator.NotificationInfo
import at.fh.mappdev.rootnavigator.R
import at.fh.mappdev.rootnavigator.auth.LoginActivity

class AlarmReceiver : BroadcastReceiver() {
    @SuppressLint("LaunchActivityFromNotification")
    override fun onReceive(context: Context, intent: Intent) {
        val newintent = Intent(context, LoginActivity::class.java)
        newintent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, newintent, PendingIntent.FLAG_IMMUTABLE)
        val id = intent.getIntExtra("ID", 1)

        val alarm = NotificationCompat.Builder(context, NotificationInfo.ALARMID)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentTitle("Alarm")
            .setContentText("Wake up you Donkey!")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(1000, 1000))
            .build()

        val nmc = NotificationManagerCompat.from(context)
        nmc.notify(id, alarm)
    }
}