package at.fh.mappdev.rootnavigator.home

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import at.fh.mappdev.rootnavigator.*
import at.fh.mappdev.rootnavigator.R
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme
import at.fh.mappdev.rootnavigator.database.*
import at.fh.mappdev.rootnavigator.reminder.*
import com.google.android.gms.location.*
import java.util.*

class HomeActivity : ComponentActivity() {
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val interval: Long = 15000
    private val fastestInterval: Long = 5000
    private var location: Location? = null
    private lateinit var mLocationRequest: LocationRequest
    private val requestPermissionCode = 999

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPrefs = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        mLocationRequest = LocationRequest.create()
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show()
        }
        checkForPermission(this)
        startLocationUpdates()

        setContent {
            RootNavigatorTheme {
                MyScaffold(sharedPrefs, alarmManager)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocationProviderClient?.removeLocationUpdates(mLocationCallback)
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation
            locationResult.lastLocation?.let { locationChanged(it) }
        }
    }

    private fun startLocationUpdates() {
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = interval
        mLocationRequest.fastestInterval = fastestInterval

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest = builder.build()
        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationProviderClient!!.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()!!)
    }

    private fun checkForPermission(context: Context) {
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED) {
            return
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                requestPermissionCode)
            return
        }
    }

    fun locationChanged(location: Location) {
        this.location = location
        GlobalVarHolder.location.value = this.location
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestPermissionCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

fun firstPrefLine(departures: MutableList<Departure>, preferences: SharedPreferences) : List<String> {
    val result: MutableList<String> = mutableListOf()
    result.add("")
    result.add("0")
    result.add("false")

    val sharedlines = preferences.getString(GlobalVarHolder.PREFERREDLINE, "")
    val preflines : MutableList<String> = mutableListOf()

    if (sharedlines?.trim() != "" && departures.isNotEmpty()) {
        if (sharedlines?.contains(",") == true){
            val lines = sharedlines.split(",")
            for (line in lines){
                preflines.add(line.trim())
            }
        } else {
            preflines.add(sharedlines.toString())
        }

        for (departure in departures) {
            for (line in preflines) {
                if (departure.line.name.contains(line)){
                    val depmins = departure.whenThere.substring(14,16).toInt()
                    val delay = if (departure.delay.isNullOrBlank()) { 0 } else { departure.delay.toInt().div(60) }
                    val livemin = Calendar.getInstance().get(Calendar.MINUTE)
                    var min = ((depmins + delay) - livemin)
                    if (min < 0) { min = 0 }

                    result.set(0, departure.line.product)
                    result.set(1, min.toString())
                    result.set(2, "true")

                    return  result

                } else {
                    val depmins = departures[0].whenThere.substring(14,16).toInt()
                    val delay = if (departures[0].delay.isNullOrBlank()) { 0 } else {
                        departures[0].delay!!.toInt().div(60) }
                    val livemin = Calendar.getInstance().get(Calendar.MINUTE)
                    var min = ((depmins + delay) - livemin)
                    if (min < 0) { min = 0 }

                    result.set(0, departures[0].line.product)
                    result.set(1, min.toString())
                }
            }
        }
    } else {
        if (departures.isNotEmpty()){
            val depmins = departures[0].whenThere.substring(14,16).toInt()
            val delay = if (departures[0].delay.isNullOrBlank()) { 0 } else {
                departures[0].delay!!.toInt().div(60) }
            val livemin = Calendar.getInstance().get(Calendar.MINUTE)
            var min = ((depmins + delay) - livemin)
            if (min < 0) { min = 0 }

            result.set(0, departures[0].line.product)
            result.set(1, min.toString())
        }
        return result
    }
    return result
}
