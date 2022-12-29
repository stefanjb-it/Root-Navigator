package at.fh.mappdev.rootnavigator.database

import androidx.lifecycle.LiveData

class ReminderRepository(private val reminderDatabaseDao: ReminderDatabaseDao) {

    val AllReminder : LiveData<List<ReminderItemRoom>> = reminderDatabaseDao.getAllReminder()

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