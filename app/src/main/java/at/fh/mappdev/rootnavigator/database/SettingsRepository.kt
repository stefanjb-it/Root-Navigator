package at.fh.mappdev.rootnavigator.database

import android.content.Context
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object SettingsRepository {

    // get all Settings
    fun getSettings(context: Context) : SettingsItemRoom {
        val db = SettingsDatabase.getDatabase(context.applicationContext)
        return db.settingsDao.getSettings()
    }

    // get amount of Settings
    fun getSettingsCount(context: Context) : Int {
        val db = SettingsDatabase.getDatabase(context.applicationContext)
        return db.settingsDao.getSettingCount()
    }

    // || WORKS || add Settings
    fun newSetting(context: Context, Setting: SettingsItemRoom) {
        val db = SettingsDatabase.getDatabase(context.applicationContext)
        GlobalScope.launch { db.settingsDao.newSettings(Setting) }
    }

    // TODO
    fun updateSetting(context: Context, Setting: SettingsItemRoom) {
        val db = SettingsDatabase.getDatabase(context.applicationContext)
        GlobalScope.launch { db.settingsDao.updateSettings(Setting) }
    }
}