package at.fh.mappdev.rootnavigator.database

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object ReminderRepository {
    // get all Reminders
    fun getReminders(context: Context) : List<ReminderItemRoom>? {
        val db = ReminderDatabase.getDatabase(context.applicationContext)
        return db.reminderDao.getAllReminders()
    }

    // || WORKS || add Reminder
    fun newReminder(context: Context, Reminder: ReminderItemRoom) {
        val db = ReminderDatabase.getDatabase(context.applicationContext)
        GlobalScope.launch { db.reminderDao.newReminder(Reminder) }
    }

    // TODO
    fun updateReminder(context:Context, Reminder: ReminderItemRoom) {
        val db = ReminderDatabase.getDatabase(context.applicationContext)
        GlobalScope.launch { db.reminderDao.updateReminder(Reminder) }
    }

    // TODO
    fun deleteReminder(context:Context, Reminder: ReminderItemRoom) {
        val db = ReminderDatabase.getDatabase(context.applicationContext)
        GlobalScope.launch { db.reminderDao.deleteReminder(Reminder) }
    }
}