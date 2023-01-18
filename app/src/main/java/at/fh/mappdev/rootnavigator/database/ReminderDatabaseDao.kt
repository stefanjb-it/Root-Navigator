package at.fh.mappdev.rootnavigator.database

import androidx.room.*

@Dao
interface ReminderDatabaseDao {
    @Query("SELECT * FROM Reminders")
    suspend fun getAllReminders(): MutableList<ReminderItemRoom>?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun newReminder(reminder: ReminderItemRoom)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateReminder(reminder: ReminderItemRoom)

    @Delete
    suspend fun deleteReminder(reminder: ReminderItemRoom)
}