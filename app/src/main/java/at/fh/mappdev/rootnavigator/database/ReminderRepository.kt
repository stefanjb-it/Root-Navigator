package at.fh.mappdev.rootnavigator.database

import androidx.lifecycle.LiveData

class ReminderRepository(private val reminderDatabaseDao: ReminderDatabaseDao) {

    val AllReminders : LiveData<List<ReminderItemRoom>> = reminderDatabaseDao.getAllReminders()

    suspend fun newReminder(Reminder: ReminderItemRoom) {
        reminderDatabaseDao.newReminder(Reminder)
    }

    suspend fun updateReminder(Reminder: ReminderItemRoom) {
        reminderDatabaseDao.updateReminder(Reminder)
    }

    suspend fun deleteReminder(Reminder: ReminderItemRoom) {
        reminderDatabaseDao.deleteReminder(Reminder)
    }
}