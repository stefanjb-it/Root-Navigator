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

    var location : MutableLiveData<Location> = MutableLiveData()

    var NOTIFICATIONID = 1
}