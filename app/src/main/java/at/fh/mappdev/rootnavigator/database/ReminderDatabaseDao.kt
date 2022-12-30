package at.fh.mappdev.rootnavigator.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ReminderDatabaseDao {
    @Query("SELECT * FROM Reminders")
    fun getAllReminders(): List<ReminderItemRoom>?

    @Insert()
    suspend fun newReminder(reminder: ReminderItemRoom)

    @Update
    suspend fun updateReminder(reminder: ReminderItemRoom)

    @Delete
    suspend fun deleteReminder(reminder: ReminderItemRoom)

}