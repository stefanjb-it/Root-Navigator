package at.fh.mappdev.rootnavigator.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast

// the Alarm class used to process set alarm requests
class Alarm(
    private val timeInMillis: Long,
    private var started: Boolean,
) {
    // tell OS to set the alarm at correct time
    fun schedule(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmBroadcastReceiver::class.java)
        val alarmPendingIntent = PendingIntent.getBroadcast(
            context,
            0, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            alarmPendingIntent
        )
        started = true
    }
}