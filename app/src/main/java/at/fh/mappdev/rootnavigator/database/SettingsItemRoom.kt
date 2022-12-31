package at.fh.mappdev.rootnavigator.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Settings")
data class SettingsItemRoom (

    @PrimaryKey(autoGenerate = true)
    var SettingsId: Int = 0,

    @ColumnInfo(name = "type")
    var SettingType: String,

    @ColumnInfo(name = "degreeprogram")
    var SettingDegreeProgram: String,

    @ColumnInfo(name= "group")
    var SettingGroup: String,

    @ColumnInfo(name = "preferredrootpoints")
    var SettingsPrefRootpoint: String,

    @ColumnInfo(name = "preferredlines")
    var SettingsPrefLines: String

)