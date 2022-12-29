package at.fh.mappdev.rootnavigator.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface ReminderDatabaseDao {
    @Query("SELECT * FROM Reminders")
    fun getAllReminders(): LiveData<List<ReminderItemRoom>>

    @Insert
    suspend fun newReminder(reminder: ReminderItemRoom)

    @Update
    suspend fun updateReminder(reminder: ReminderItemRoom)

    @Delete
    suspend fun deleteReminder(reminder: ReminderItemRoom)

}