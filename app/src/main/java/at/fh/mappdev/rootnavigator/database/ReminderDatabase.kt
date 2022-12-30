package at.fh.mappdev.rootnavigator.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ReminderItemRoom::class], version = 2)
abstract class ReminderDatabase : RoomDatabase() {

    abstract val reminderDao: ReminderDatabaseDao

    companion object {
        private var INSTANCE: ReminderDatabase? = null
        fun getDatabase(context: Context): ReminderDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): ReminderDatabase {
            return Room.databaseBuilder(
                context,
                ReminderDatabase::class.java, "lesson-note-db"
            )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }
    }
}