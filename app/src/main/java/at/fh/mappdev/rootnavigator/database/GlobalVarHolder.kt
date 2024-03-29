package at.fh.mappdev.rootnavigator.database

import android.location.Location
import androidx.lifecycle.MutableLiveData

object GlobalVarHolder {
    val TYPE = "TYPE"
    val PROGRAMME = "PROGRAMME"
    val GROUP = "GROUP"
    val ROOTPOINT = "ROOTPOINT"
    val PREFERREDLINE = "PREFERREDLINE"
    val STAYLOGGEDIN = "STAYLOGGEDIN"
    val LASTLOGGEDIN = "ITSADATE"
    val TOBESAVED = "BOOLI"

    var location : MutableLiveData<Location> = MutableLiveData()
    var userIdToken : String = ""
    val uidPrefix = "userId="
    var localReqTime = "30"
    val REQUESTTIME = "REQUESTTIME"

    var NOTIFICATIONID = "NOTIFICATIONID"
    var ALARMID = "ALARMID"
}