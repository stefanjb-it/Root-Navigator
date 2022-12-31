package at.fh.mappdev.rootnavigator.database

import androidx.room.*

@Dao
interface SettingsDatabaseDao {

    @Query("SELECT * FROM Settings")
    fun getSettings(): SettingsItemRoom

    @Query("SELECT COUNT(*) FROM Settings")
    fun getSettingCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun newSettings(settings: SettingsItemRoom)

    @Update
    suspend fun updateSettings(settings: SettingsItemRoom)

}