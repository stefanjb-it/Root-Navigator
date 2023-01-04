package at.fh.mappdev.rootnavigator

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
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
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.twotone.Home
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import at.fh.mappdev.rootnavigator.database.*
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme
import java.util.*

class HomeActivity : ComponentActivity(), LocationListener {
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    var location: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPrefs = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        setContent {
            RootNavigatorTheme {
                MyScaffold(sharedPrefs)
                getLocation()
            }
        }
    }
    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 2)
        }
        // ToDo change minTime and mindDistance
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1f, this)
    }
    override fun onLocationChanged(location: Location) {
        this.location = location
        GlobalVarHolder.location.value = location
        Toast.makeText(this, "Location updated: " + location.longitude + " " + location.latitude, Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun Connection(station: SafeStationDetails, preferences: SharedPreferences) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp, /*horizontal = 16.dp*/),
        backgroundColor = MaterialTheme.colors.primaryVariant,
        shape = RoundedCornerShape(25.dp)
    ) {
        CardContent(station, preferences)
    }
}

@Composable
fun Connections(
    modifier: Modifier = Modifier,
    connections: List<String> = List(1000) { "$it" },
    preferences: SharedPreferences
) {
    var currentLocation = GlobalVarHolder.location.observeAsState()
    val lat = 47.06727184602459
    val long = 15.442097181893473
    // var stationsIdResponse : State<ResponseType?> = BackendHandler.getNearbyStations(currentLocation.value?.latitude ?: (-1).toDouble(), currentLocation.value?.longitude ?: (-1).toDouble(), 1000).observeAsState()//= BackendHandler.getNearbyStations(lat, long, 250).observeAsState()
    var stationsIdResponse : State<ResponseType?> = BackendHandler.getNearbyStations(lat, long, 1000).observeAsState()
    var finalMap = BackendHandler.getStationMap().observeAsState()
    Log.e("StationsList", stationsIdResponse.value?.content.toString())

    when (stationsIdResponse.value?.done) {
        true -> {
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
                Log.v("finalMap", finalMap.value.toString())
                finalMap.value?.forEach {
                    item {
                        Connection(station = it.value, preferences)
                    }
                }
                /*connections.forEach {
                item {
                    Connection(name = it)
                }
            }*/
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
        // image = painterResource(R.drawable.settings),
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
                            // color = MaterialTheme.colors.primaryVariant,
                            style = MaterialTheme.typography.h1,
                            modifier = Modifier
                                .offset(y = 2.dp)
                        )
                    }
                },

                backgroundColor = MaterialTheme.colors.primaryVariant,
                // backgroundColor = MaterialTheme.colors.onSurface,
                navigationIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.logo_no_text),
                        contentDescription = "Logo",
                        modifier = Modifier.size(48.dp)
                    )
                },
                actions = {
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
fun BottomBar(navController: NavHostController, bottomBarState: MutableState<Boolean>, topBarState: MutableState<Boolean>){
    val barItems = listOf(
        BarItem(
            title = "Home",
            //image = painterResource(R.drawable.home_door),
            image = Icons.Outlined.Home,
            route = "home"
        ),
        BarItem(
            title = "Reminder",
            //image = painterResource(R.drawable.clipboard_notes),
            image = Icons.Filled.ListAlt,
            route = "reminder"
        ),
        BarItem(
            title = "Alarm",
            // image = painterResource(R.drawable.alarm_clock),
            image = Icons.Filled.Alarm,
            route = "alarm"
        ),
        BarItem(
            title = "Timetable",
            // image = painterResource(R.drawable.calendar_days),
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
                // backgroundColor = MaterialTheme.colors.onSurface
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
                }

                barItems.forEach { navItem ->
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
                                // color = MaterialTheme.colors.surface
                                color = MaterialTheme.colors.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                }

            }
        }
    )
}


@Composable
fun NavigationHost(navController: NavHostController, preferences : SharedPreferences) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route
    ) {
        composable(NavRoutes.Home.route){
            // Home Screen
            Connections(preferences = preferences)
        }

        composable(NavRoutes.Reminder.route) {
            // Reminder
            // LoginUI()
            // NewReminderUI()
            ReminderOverviewUI(navController)
        }

        composable(NavRoutes.NewReminder.route){
            NewReminderUI(navController)
        }

        composable(NavRoutes.Alarm.route) {
            // Alarm
            // RegistrationUIMode()
            AlarmUi()
        }

        composable(NavRoutes.Timetable.route) {
            // Timetable
            TimetableUI()
        }

        composable(NavRoutes.Settings.route) {
            // Settings
            SettingUi(navController, preferences)
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MyScaffold(preferences: SharedPreferences){
    val scaffoldState = rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val navController = rememberNavController()

    val topBarState = rememberSaveable { (mutableStateOf(true)) }
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

    Scaffold (
        scaffoldState = scaffoldState,
        topBar = { TopBar(navController = navController, bottomBarState = bottomBarState, topBarState = topBarState) },
        content = { NavigationHost(navController = navController, preferences) },
        bottomBar = { BottomBar(navController = navController, bottomBarState = bottomBarState, topBarState = topBarState) }
    )
}


@Composable
fun CardContent(station: SafeStationDetails, preferences: SharedPreferences) {
    var expanded by remember { mutableStateOf(false) }

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
            when(firstPrefLine(station.departures, preferences)[0]) {
                "train" -> Icon(
                    imageVector = Icons.Filled.Train,
                    tint = MaterialTheme.colors.surface,
                    contentDescription = "route type",
                    modifier = Modifier
                        .weight(2f)
                )
                "bus" -> Icon(
                    imageVector = Icons.Filled.DirectionsBus,
                    tint = MaterialTheme.colors.surface,
                    contentDescription = "route type",
                    modifier = Modifier
                        .weight(2f)
                )
                else -> Icon(
                    imageVector = Icons.Filled.Tram,
                    tint = MaterialTheme.colors.surface,
                    contentDescription = "route type",
                    modifier = Modifier
                        .weight(2f)
                )
            }

            /*
            Icon(
                imageVector = Icons.Filled.Train,
                tint = MaterialTheme.colors.surface,
                contentDescription = "route type",
                modifier = Modifier
                    .weight(2f)
            )
            */

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
                    text = firstPrefLine(station.departures, preferences)[1],
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = depature.line.name,
                            color = MaterialTheme.colors.onSurface,
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

/*
@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Darkmode"
)
@Preview(
    showBackground = true,
    widthDp = 320,
    name = "Lightmode"
)
@Composable
private fun ConnectionsPreview() {
    RootNavigatorTheme {
        Connections()
    }
}
*/