package at.fh.mappdev.rootnavigator.database

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object ReminderRepository {
    var allReminders : MutableList<ReminderItemRoom>? = mutableListOf()

    // get all Reminders
    fun getReminders(context: Context) {
        val db = ReminderDatabase.getDatabase(context.applicationContext)
        allReminders = db.reminderDao.getAllReminders()
    }

    // || WORKS || add Reminder
    fun newReminder(context: Context, Reminder: ReminderItemRoom) {
        val db = ReminderDatabase.getDatabase(context.applicationContext)
        GlobalScope.launch { db.reminderDao.newReminder(Reminder) }
    }

    // TODO
    fun updateReminder(context:Context, Reminder: ReminderItemRoom) {
        val db = ReminderDatabase.getDatabase(context.applicationContext)
        GlobalScope.launch {
            db.reminderDao.updateReminder(Reminder)
        }
    }

    // TODO
    fun deleteReminder(context:Context, Reminder: ReminderItemRoom) {
        val db = ReminderDatabase.getDatabase(context.applicationContext)
        GlobalScope.launch { db.reminderDao.deleteReminder(Reminder) }
        allReminders?.remove(Reminder)
    }
}