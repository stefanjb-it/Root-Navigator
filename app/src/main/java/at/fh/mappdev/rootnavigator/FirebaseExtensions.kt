package at.fh.mappdev.rootnavigator

import android.app.Activity
import android.widget.Toast

object FirebaseExtensions {
    fun Activity.toast(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}