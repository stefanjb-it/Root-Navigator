package at.fh.mappdev.rootnavigator.auth

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.fh.mappdev.rootnavigator.R
import at.fh.mappdev.rootnavigator.database.GlobalVarHolder
import at.fh.mappdev.rootnavigator.home.HomeActivity
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.concurrent.timerTask

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RootNavigatorTheme {
                Surface(color = MaterialTheme.colors.primary) {
                    AuthUI()
                    checkUser(this)
                }
            }
        }

        val timer = Timer()
        val intent = Intent(this, HomeActivity::class.java)
        val expired = Intent(this, SessionExpiredActivity::class.java)
        val sharedPrefs = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        val calender = Calendar.getInstance()

        if (isDeviceOnline(this)){
            sharedPrefs.edit().putLong(GlobalVarHolder.LASTLOGGEDIN, calender.timeInMillis).apply()

            if (sharedPrefs.getBoolean(GlobalVarHolder.TOBESAVED, false)){
                val user = FirebaseAuth.getInstance().currentUser
                val type = sharedPrefs.getString(GlobalVarHolder.TYPE, "Student")
                val preferredLine = sharedPrefs.getString(GlobalVarHolder.PREFERREDLINE, "")
                val preferredRootpoint = sharedPrefs.getString(GlobalVarHolder.ROOTPOINT, "")
                val student = (type == "Student")

                val userdata = hashMapOf(
                    "Degree Program" to "",
                    "Group" to "",
                    "Preferred Lines" to preferredLine,
                    "Preferred Rootpoint" to preferredRootpoint,
                    "Type" to student
                )

                FirebaseFirestore.getInstance().collection("USER_CONFIG")
                    .document(user!!.uid).set(userdata)
                    .addOnSuccessListener { sharedPrefs.edit().putBoolean(GlobalVarHolder.TOBESAVED, false).apply() }
                    .addOnFailureListener { sharedPrefs.edit().putBoolean(GlobalVarHolder.TOBESAVED, true).apply() }

            } else {

                val reference = FirebaseFirestore.getInstance()
                    .collection("USER_CONFIG")
                    .document(FirebaseAuth.getInstance().currentUser!!.uid)

                reference.get().addOnSuccessListener { document ->
                    if (document != null) {
                        val type = document.getBoolean("Type")
                        if (type == true){
                            sharedPrefs.edit().putString(GlobalVarHolder.TYPE, "Student").apply()
                        } else {
                            sharedPrefs.edit().putString(GlobalVarHolder.TYPE, "Normal").apply()
                        }
                        sharedPrefs.edit().putString(GlobalVarHolder.PROGRAMME, document.getString("Degree Program")).apply()
                        sharedPrefs.edit().putString(GlobalVarHolder.GROUP, document.getString("Group")).apply()
                        sharedPrefs.edit().putString(GlobalVarHolder.ROOTPOINT, document.getString("Preferred Rootpoint")).apply()
                        sharedPrefs.edit().putString(GlobalVarHolder.PREFERREDLINE, document.getString("Preferred Lines")).apply()
                    }
                }
            }
        }

        val expPoint = (calender.timeInMillis - 2592000000L)

        if (sharedPrefs.getLong(GlobalVarHolder.LASTLOGGEDIN, calender.timeInMillis) < expPoint) {
            timer.schedule(timerTask {
                expired.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(expired)
                finishAndRemoveTask()
            }, 500)

        } else {
            timer.schedule(timerTask {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finishAndRemoveTask()
            }, 1000)
        }
    }

    private fun checkUser(context: Context){
        if (FirebaseAuth.getInstance().currentUser == null){
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        checkUser(this)
    }

    override fun onPause() {
        super.onPause()
        checkUser(this)
    }
}

private fun isDeviceOnline(context: Context): Boolean {
    val connManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connManager.getNetworkCapabilities(connManager.activeNetwork)
        if (networkCapabilities == null) {
            Log.d("Internet", "Device Offline")
            return false
        } else {
            Log.d("Internet", "Device Online")
            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_SUSPENDED)
            ) {
                Log.d("Internet", "Connected to Internet")
                return true
            } else {
                Log.d("Internet", "Not connected to Internet")
                return false
            }
        }
    } else {
        val activeNetwork = connManager.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true && activeNetwork.isAvailable
    }
}

@Composable
fun AuthUI(){
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background)
        .paint(
            painter = painterResource(if (!isSystemInDarkTheme()) R.drawable.threelines_new_light else R.drawable.threelines_new),
            contentScale = ContentScale.FillWidth
        )
    ){ Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
        ) {
        CircularProgressIndicator(
            modifier = Modifier
                .height(height = 250.dp)
                .width(width = 250.dp),
            color = MaterialTheme.colors.secondary,
            strokeWidth = 25.dp
        )

        Spacer(modifier = Modifier.padding(top = 50.dp))

        Text(text = "Loading\nProfile",
            color = MaterialTheme.colors.surface,
            textAlign = TextAlign.Center,
            fontSize = 48.sp)
        }
    }
}