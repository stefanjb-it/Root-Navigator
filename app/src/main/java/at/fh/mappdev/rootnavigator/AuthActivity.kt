package at.fh.mappdev.rootnavigator

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme
import java.util.*
import kotlin.concurrent.timerTask

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RootNavigatorTheme {
                Surface(color = MaterialTheme.colors.primary) {
                    AuthUI()
                }
            }
        }

        val timer = Timer()
        val intent = Intent(this, HomeActivity::class.java)
        val expired = Intent(this, SessionExpiredActivity::class.java)
        val sharedPrefs = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        val calender = Calendar.getInstance()

        if (isDeviceOnline(this)){
            sharedPrefs.edit().putLong("LASTLOGGEDIN", calender.timeInMillis).apply()
        }

        val expPoint = (calender.timeInMillis - 2592000000L)

        if (sharedPrefs.getLong("LASTLOGGEDIN", calender.timeInMillis) < expPoint) {
            timer.schedule(timerTask { startActivity(expired) }, 500)
        } else {
            timer.schedule(timerTask { startActivity(intent) }, 1000)
        }
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
        .background(MaterialTheme.colors.primary)
        .paint(
            painter = painterResource(R.drawable.threelines),
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