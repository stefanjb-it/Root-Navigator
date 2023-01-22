package at.fh.mappdev.rootnavigator.alarm

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.Nullable
import androidx.core.app.NotificationCompat
import at.fh.mappdev.rootnavigator.NotificationInfo
import at.fh.mappdev.rootnavigator.R

class AlarmService : Service(){
    var mp: MediaPlayer? = null
    var vib : Vibrator? = null

    override fun onCreate() {
        super.onCreate()
        mp = MediaPlayer.create(this, R.raw.ringtone_alarm)
        mp?.setLooping(true)
        vib = getSystemService(VIBRATOR_SERVICE) as Vibrator
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationIntent = Intent(this, RingActivity::class.java)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        val alarmTitle = "ALARM"
        val notification: Notification = NotificationCompat.Builder(this, NotificationInfo.ALARMID)
            .setContentTitle(alarmTitle)
            .setContentText("Ring Ring .. Ring Ring")
            .setSmallIcon(at.fh.mappdev.rootnavigator.R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
        mp?.start()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vib?.vibrate(VibrationEffect.createOneShot(1000, 200))
        }
        startForeground(1, notification)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mp?.stop()
        vib?.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}