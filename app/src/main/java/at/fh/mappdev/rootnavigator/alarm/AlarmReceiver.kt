package at.fh.mappdev.rootnavigator.alarm

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import at.fh.mappdev.rootnavigator.NotificationInfo
import at.fh.mappdev.rootnavigator.R
import at.fh.mappdev.rootnavigator.auth.LoginActivity
import java.util.*


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

/*
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // We're creating a new intent that's going to start the MainActivity
        println("AlarmReceiver: onReceive")
        val scheduledIntent = Intent(context.applicationContext, HomeActivity::class.java)
        scheduledIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        //context.startActivity(scheduledIntent)
        val pendingIntent = PendingIntent.getActivity(context, 0, scheduledIntent, PendingIntent.FLAG_IMMUTABLE)

        val not : Notification = NotificationCompat.Builder(context, NotificationInfo.ALARMID)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentTitle("Alarm")
            .setContentText("Wake up you Donkey!")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(1000, 1000))
            .build()

    }
}*/

class AlarmService : Service(){
    var mp: MediaPlayer? = null
    var vib : Vibrator? = null

    override fun onCreate() {
        super.onCreate()
        mp = MediaPlayer.create(this, R.raw.alarm)
        mp?.setLooping(true)
        vib = getSystemService(VIBRATOR_SERVICE) as Vibrator
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationIntent = Intent(this, RingActivity::class.java)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val alarmTitle = "ALARM"
        val notification: Notification = NotificationCompat.Builder(this, NotificationInfo.ALARMID)
            .setContentTitle(alarmTitle)
            .setContentText("Ring Ring .. Ring Ring")
            .setSmallIcon(at.fh.mappdev.rootnavigator.R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
        mp?.start()
        val pattern = longArrayOf(0, 100, 1000)
        //vib?.vibrate(pattern, 0)
        vib?.vibrate(VibrationEffect.createOneShot(1000, 200))
        startForeground(1, notification)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mp?.stop()
        vib?.cancel()
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

class Alarm(
    private val timeInMillis: Long,
    private var started: Boolean,
) {
    fun schedule(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmBroadcastReceiver::class.java)
        val alarmPendingIntent = PendingIntent.getBroadcast(
            context,
            0, intent, 0)

            val toastText: String = "SET"

            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                alarmPendingIntent
            )
        started = true
    }
}

class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
            val toastText = String.format("Alarm Received")
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
        val notificationIntent = Intent(context.applicationContext, RingActivity::class.java)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            context.startActivity(notificationIntent)
            startAlarmService(context, intent)
    }

    private fun startAlarmService(context: Context, intent: Intent) {
        println("AlarmBroadcastReceiver: startAlarmService")
        val intentService = Intent(context, AlarmService::class.java)
        intentService.putExtra("TITLE", intent.getStringExtra("TITLE") ?: "Alarm")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }
}
/*
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannnel()
    }

    private fun createNotificationChannnel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Alarm Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

    companion object {
        const val CHANNEL_ID = "ALARM_SERVICE_CHANNEL"
    }
}
*/