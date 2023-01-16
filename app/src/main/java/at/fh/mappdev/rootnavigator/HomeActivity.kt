package at.fh.mappdev.rootnavigator

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
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme
import at.fh.mappdev.rootnavigator.database.*
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

@Composable
fun Connection(station: SafeStationDetails, preferences: SharedPreferences) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp),
        backgroundColor = MaterialTheme.colors.primaryVariant,
        shape = RoundedCornerShape(25.dp)
    ) {
        CardContent(station, preferences)
    }
}

@Composable
fun Connections( preferences: SharedPreferences ) {
    var currentLocation = GlobalVarHolder.location.observeAsState()
    //val lat = 47.0672718
    val lat = 47.0727551
    //val long = 15.4420971
    val long = 15.4140822
    var stationsIdResponse : State<ResponseType?> = BackendHandler.getNearbyStations(currentLocation.value?.latitude ?: (-1).toDouble(), currentLocation.value?.longitude ?: (-1).toDouble(), 1000).observeAsState()//= BackendHandler.getNearbyStations(lat, long, 250).observeAsState()
    // var stationsIdResponse : State<ResponseType?> = BackendHandler.getNearbyStations(lat, long, 1000).observeAsState()
    var finalMap = BackendHandler.getStationMap().observeAsState()
    //Log.e("StationsList", stationsIdResponse.value?.content.toString())

    when (stationsIdResponse.value?.done) {
        true -> {
            //Log.e("finalMap", stationsIdResponse.value?.content?.map { it.name }.toString())
            //Log.e("StationsList", stationsIdResponse.value?.content?.size.toString())
            BackendHandler.loadStationDetails(stationsIdResponse.value?.content ?: listOf())
            Toast.makeText(LocalContext.current, "Your data is almost here!", Toast.LENGTH_LONG).show()
        }
        else -> Toast.makeText(
            LocalContext.current,
            "Data is being retrieved...",
            Toast.LENGTH_SHORT
        ).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
            .paint(
                painter = painterResource(R.drawable.threelines),
                contentScale = ContentScale.FillWidth
            )
            .padding(
                start = 16.dp,
                end = 16.dp,
            ),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn()
        {
            if (finalMap.value != null) {
                //Log.v("finalMap", finalMap.value?.map { it.value.station.name }.toString())
                //Log.v("finalMap", finalMap.value?.size.toString())
                finalMap.value?.forEach {
                    item {
                        Connection(station = it.value, preferences)
                    }
                }
            } else {
                item {

                }
            }

        }
    }
}

@Composable
fun TopBar(navController: NavHostController, bottomBarState: MutableState<Boolean>, topBarState: MutableState<Boolean>){
    val settingItem = BarItem(
        title = "Settings",
        image = Icons.Outlined.Settings,
        route = "settings"
    )

    AnimatedVisibility(
        visible = topBarState.value,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it }),
        content = {
            CenterTopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "/",
                            color = MaterialTheme.colors.secondary,
                            style = MaterialTheme.typography.h1,
                            modifier = Modifier
                                .offset(y = 2.dp)
                        )
                        Text(
                            text = "Navigator",
                            color = MaterialTheme.colors.primary,
                            style = MaterialTheme.typography.h1,
                            modifier = Modifier
                                .offset(y = 2.dp)
                        )
                    }
                },

                backgroundColor = MaterialTheme.colors.primaryVariant,
                navigationIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.logo_no_text),
                        contentDescription = "Logo",
                        modifier = Modifier.size(48.dp)
                    )
                },
                actions = {
                    /* Logout Button
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Logout,
                            contentDescription = "Logout",
                            tint = MaterialTheme.colors.secondary,
                            modifier = Modifier
                                .size(28.dp)
                        )
                    }
                    */

                    IconButton(
                        onClick = {
                            navController.navigate(settingItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    ) {
                        Icon(
                            imageVector = settingItem.image,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colors.secondary,
                            modifier = Modifier
                                .size(28.dp)
                        )
                    }

                }
            )

        }
    )
}

@ExperimentalAnimationApi
@Composable
fun BottomBar(navController: NavHostController, bottomBarState: MutableState<Boolean>, topBarState: MutableState<Boolean>, preferences: SharedPreferences, Context: Context = LocalContext.current){
    var barItems = mutableListOf(
            BarItem(
                title = "Home",
                image = Icons.Outlined.Home,
                route = "home"
            ),
            BarItem(
                title = "Reminder",
                image = Icons.Filled.ListAlt,
                route = "reminder"
            ),
            BarItem(
                title = "Alarm",
                image = Icons.Filled.Alarm,
                route = "alarm"
            ),
            BarItem(
            title = "Timetable",
            image = Icons.Filled.DateRange,
            route = "timetable"
            )
    )

    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            BottomNavigation(
                backgroundColor = MaterialTheme.colors.primaryVariant
            ){
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route

                when (backStackEntry?.destination?.route) {
                    "reminder" -> {
                        bottomBarState.value = true
                        topBarState.value = true
                    }
                    "alarm" -> {
                        bottomBarState.value = true
                        topBarState.value = true
                    }
                    "timetable" -> {
                        bottomBarState.value = true
                        topBarState.value = true
                    }
                    "settings" -> {
                        bottomBarState.value = true
                        topBarState.value = true
                    }
                    "home" -> {
                        bottomBarState.value = true
                        topBarState.value = true
                    }
                    "new_reminder" -> {
                        bottomBarState.value = false
                        topBarState.value = false
                    }
                }

                barItems.forEach { navItem ->
                    if(navItem.title == "Timetable" && preferences.getString("TYPE", "Student") == "Normal") {
                        BottomNavigationItem(
                            modifier = Modifier
                                .fillMaxHeight()
                                .offset(y = 3.dp),
                            selected = currentRoute == navItem.route,
                            onClick = {
                                Toast.makeText(Context, "Timetable is only available in Student Mode!", Toast.LENGTH_SHORT).show()
                            },
                            icon = {
                                Icon(
                                    imageVector = navItem.image,
                                    contentDescription = navItem.title,
                                    tint = MaterialTheme.colors.primary,
                                    modifier = Modifier.size(28.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = navItem.title,
                                    color = MaterialTheme.colors.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        )
                    } else {
                        BottomNavigationItem(
                            modifier = Modifier
                                .fillMaxHeight()
                                .offset(y = 3.dp),
                            selected = currentRoute == navItem.route,
                            onClick = {
                                navController.navigate(navItem.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = navItem.image,
                                    contentDescription = navItem.title,
                                    tint = MaterialTheme.colors.secondary,
                                    modifier = Modifier.size(28.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = navItem.title,
                                    color = MaterialTheme.colors.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        )
                    }
                }

            }
        }
    )
}


@Composable
fun NavigationHost(navController: NavHostController, alarmManager: AlarmManager, preferences : SharedPreferences, bottomBarState: MutableState<Boolean>) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route
    ) {
        composable(NavRoutes.Home.route){
            Connections(preferences = preferences)
        }

        composable(NavRoutes.Reminder.route) {
            ReminderOverviewUI(navController)
        }

        composable(NavRoutes.NewReminder.route){
            NewReminderUI(navController, alarmManager, preferences, bottomBarState)
        }

        composable(NavRoutes.Alarm.route) {
            AlarmUi()
        }

        composable(NavRoutes.Timetable.route) {
            TimetableUI(preferences)
        }

        composable(NavRoutes.Settings.route) {
            SettingUi(navController, preferences)
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MyScaffold(preferences: SharedPreferences, alarmManager: AlarmManager){
    val scaffoldState = rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val navController = rememberNavController()

    val topBarState = rememberSaveable { (mutableStateOf(true)) }
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

    Scaffold (
        scaffoldState = scaffoldState,
        topBar = { TopBar(navController = navController, bottomBarState = bottomBarState, topBarState = topBarState) },
        content = { padding -> Column(modifier = Modifier.padding(padding))
            {NavigationHost(navController = navController, alarmManager, preferences, bottomBarState)} },
        bottomBar = { BottomBar(navController = navController, bottomBarState = bottomBarState, topBarState = topBarState, preferences = preferences) }
    )
}


@Composable
fun CardContent(station: SafeStationDetails, preferences: SharedPreferences) {
    var expanded by remember { mutableStateOf(false) }
    val infoList: List<String> = firstPrefLine(station.departures, preferences)

    val sharedLines = preferences.getString(GlobalVarHolder.PREFERREDLINE, "")
    val prefLines : MutableList<String> = mutableListOf()

    if (sharedLines?.contains(",") == true){
        val lines = sharedLines.split(",")
        for (line in lines){
            prefLines.add(line.trim())
        }
    } else {
        prefLines.add(sharedLines.toString())
    }

    Column(
        modifier = Modifier
            .padding(
                start = 4.dp,
                end = 4.dp,
                top = 8.dp,
                bottom = 8.dp
            )
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            when(infoList[0]) {
                "suburban" -> Icon(
                    imageVector = Icons.Filled.Train,
                    tint = if (infoList[2].toBoolean()) { MaterialTheme.colors.secondary } else { MaterialTheme.colors.surface },
                    contentDescription = "route type",
                    modifier = Modifier
                        .weight(2f)
                )
                "bus" -> Icon(
                    imageVector = Icons.Filled.DirectionsBus,
                    tint = if (infoList[2].toBoolean()) { MaterialTheme.colors.secondary } else { MaterialTheme.colors.surface },
                    contentDescription = "route type",
                    modifier = Modifier
                        .weight(2f)
                )
                else -> Icon(
                    imageVector = Icons.Filled.Tram,
                    tint = if (infoList[2].toBoolean()) { MaterialTheme.colors.secondary } else { MaterialTheme.colors.surface },
                    contentDescription = "route type",
                    modifier = Modifier
                        .weight(2f)
                )
            }

            Text(
                text = station.station.name,
                color = MaterialTheme.colors.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(8f)
            )
            Row(modifier = Modifier
                .weight(3f),
            ) {
                Text(
                    text = infoList[1],
                    color = MaterialTheme.colors.surface,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = " min",
                    color = MaterialTheme.colors.surface,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    tint = MaterialTheme.colors.secondary,
                    contentDescription = if (expanded) {
                        stringResource(id = R.string.show_less)
                    } else {
                        stringResource(id = R.string.show_more)
                    },
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
        if (expanded) {
            Column(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Line",
                        color = MaterialTheme.colors.onSurface,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(1.5f)
                    )
                    Text(
                        text = "Dep.",
                        color = MaterialTheme.colors.onSurface,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(1.25f)
                    )
                    Text(
                        text = "Direction",
                        color = MaterialTheme.colors.onSurface,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(5f)
                    )
                    Text(
                        text = "Delay",
                        color = MaterialTheme.colors.onSurface,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(1.25f)
                    )
                }

                Spacer(modifier = Modifier.padding(bottom = 8.dp))

                for (depature in station.departures) {

                    var isPref = false
                    for (prefLine in prefLines){
                        if (depature.line.name.contains(prefLine)) {
                            isPref = true
                            break
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = depature.line.name,
                            color = if (isPref) {MaterialTheme.colors.secondary} else { MaterialTheme.colors.onSurface},
                            fontSize = 14.sp,
                            modifier = Modifier
                                .weight(1.5f)
                        )
                        Text(
                            text = depature.whenThere.substring(11, 16),
                            color = MaterialTheme.colors.surface,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .weight(1.25f)
                        )
                        Text(
                            text = depature.direction,
                            color = MaterialTheme.colors.onSurface,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .weight(5f)
                        )

                        Text(
                            modifier = Modifier
                                .weight(1.25f),
                            text = if (depature.delay == null) { 0 } else { depature.delay.toInt().div(60) }.toString() + " min",
                            color = MaterialTheme.colors.surface,
                            fontSize = 14.sp
                        )
                    }
                }


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
                    Log.i("Type", departure.line.product)
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
                    //Log.i("Type", departures[0].line.product)
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
            //Log.i("Type", departures[0].line.product)
        }
        return result
    }
    return result
}
