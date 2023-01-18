package at.fh.mappdev.rootnavigator.database

import android.content.Context
import kotlinx.coroutines.*

object ReminderRepository {
    var allReminders : MutableList<ReminderItemRoom>? = mutableListOf()

    fun getReminders(context: Context) : List<ReminderItemRoom> {
        val db = ReminderDatabase.getDatabase(context.applicationContext)
        GlobalScope.launch {
            allReminders = db.reminderDao.getAllReminders()
        }
        return allReminders as List<ReminderItemRoom>
    }

    fun newReminder(context: Context, Reminder: ReminderItemRoom) {
        val db = ReminderDatabase.getDatabase(context.applicationContext)
        GlobalScope.launch { db.reminderDao.newReminder(Reminder) }
    }

    fun updateReminder(context:Context, Reminder: ReminderItemRoom) {
        val db = ReminderDatabase.getDatabase(context.applicationContext)
        GlobalScope.launch {
            db.reminderDao.updateReminder(Reminder)
        }
    }

    fun deleteReminder(context:Context, Reminder: ReminderItemRoom) : List<ReminderItemRoom> {
        val db = ReminderDatabase.getDatabase(context.applicationContext)
        GlobalScope.launch { db.reminderDao.deleteReminder(Reminder) }
        allReminders?.remove(Reminder)
        return allReminders as List<ReminderItemRoom>
    }
}