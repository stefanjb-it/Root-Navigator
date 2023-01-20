package at.fh.mappdev.rootnavigator.alarm

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.AlarmManagerCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import at.fh.mappdev.rootnavigator.NotificationInfo
import at.fh.mappdev.rootnavigator.R
import at.fh.mappdev.rootnavigator.auth.LoginActivity
import kotlin.random.Random

class AlarmReceiver : BroadcastReceiver() {
    @SuppressLint("LaunchActivityFromNotification")
    override fun onReceive(context: Context, intent: Intent) {
        val newintent = Intent(context, LoginActivity::class.java)
        newintent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val isAlarm = intent.getBooleanExtra("TYPE", false)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, newintent, PendingIntent.FLAG_IMMUTABLE)
        Log.i("Alarm", isAlarm.toString())

        if (isAlarm) {
            val id = intent.getIntExtra("ID", 1)
            Log.i("Alarm", isAlarm.toString() + " | " + id.toString())
            val alarm = NotificationCompat.Builder(context, NotificationInfo.ALARMID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Alarm")
                .setContentText("Wake up you Donkey!")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build()

            val nmc = NotificationManagerCompat.from(context)
            nmc.notify(id, alarm)
        } else {
            val id = intent.getIntExtra("ID", 1)
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
}