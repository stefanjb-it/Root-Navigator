package at.fh.mappdev.rootnavigator.database

import androidx.room.*

@Dao
interface ReminderDatabaseDao {
    @Query("SELECT * FROM Reminders")
    fun getAllReminders(): List<ReminderItemRoom>?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun newReminder(reminder: ReminderItemRoom)

    @Update
    suspend fun updateReminder(reminder: ReminderItemRoom)

    @Delete
    suspend fun deleteReminder(reminder: ReminderItemRoom)

}