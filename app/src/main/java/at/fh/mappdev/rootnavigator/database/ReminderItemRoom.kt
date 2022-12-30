package at.fh.mappdev.rootnavigator.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Reminders")
data class ReminderItemRoom(

    @PrimaryKey(autoGenerate = true)
    var ReminderId: Long = 1L,

    @ColumnInfo(name = "date")
    var ReminderDate: String,

    @ColumnInfo(name = "time")
    var ReminderTime: String,

    @ColumnInfo(name = "description")
    var ReminderDescription : String,

    @ColumnInfo(name = "active")
    var ReminderActive: Boolean

)