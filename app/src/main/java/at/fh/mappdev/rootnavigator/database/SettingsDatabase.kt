package at.fh.mappdev.rootnavigator.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SettingsItemRoom::class], version = 1)
abstract class SettingsDatabase : RoomDatabase() {

    abstract val settingsDao: SettingsDatabaseDao

    companion object {
        private var INSTANCE: SettingsDatabase? = null
        fun getDatabase(context: Context): SettingsDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): SettingsDatabase {
            return Room.databaseBuilder(
                context,
                SettingsDatabase::class.java, "setting-db"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}