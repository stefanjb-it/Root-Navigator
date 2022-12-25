package at.fh.mappdev.rootnavigator

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TopAppBar
// import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RootNavigatorTheme {
                // Surface(color = MaterialTheme.colors.background) {
                Surface(color = colorResource(id = R.color.dark_gray)) {
                    //Connections()
                    MyScaffold()
                }
            }
        }
    }
}

@Composable
fun Connection(name: String) {
    Card(
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
        backgroundColor = colorResource(id = R.color.light_gray),
        shape = RoundedCornerShape(20.dp)
    ) {
        CardContent(name)
    }
}

@Composable
private fun Connections(
    modifier: Modifier = Modifier,
    connections: List<String> = List(1000) { "$it" }
) {
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = connections) {
                connection -> Connection(name = connection)
        }
    }
}

@Composable
fun TopBar(navController: NavHostController, bottomBarState: MutableState<Boolean>, topBarState: MutableState<Boolean>){
    /*
    TopAppBar(
        title = { Text(text ="Navigator")},
        backgroundColor = colorResource(id = R.color.light_gray)
    )
    */

    val settingItem = BarItem(
        title = "Settings",
        image = painterResource(R.drawable.settings),
        route = "settings"
    )

    AnimatedVisibility(
        visible = topBarState.value,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it }),
        content = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ){
                        Text(
                            text = "/",
                            color = colorResource(id = R.color.orange),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Navigator",
                            color = colorResource(id = R.color.dark_gray),
                            textAlign = TextAlign.Center
                        )
                    }
                },

                backgroundColor = colorResource(id = R.color.light_gray),
                navigationIcon = {
                    // id = R.drawable.logo_no_text
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
                            painter = painterResource(id = R.drawable.settings),
                            contentDescription = "Settings",
                            tint = colorResource(id = R.color.orange),
                            modifier = Modifier
                                .size(28.dp)
                        )
                    }
                }
            )

            /*{
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route

                when (backStackEntry?.destination?.route) {
                    "reminder" -> {
                        topBarState.value = false
                    }
                    "alarm" -> {
                        topBarState.value = false
                    }
                    "timetable" -> {
                        topBarState.value = false
                    }
                    "settings" -> {
                        topBarState.value = false
                    }
                    "home" -> {
                        topBarState.value = true
                    }
                }

                // add Top App Bar

            }*/
        }
    )
}

@ExperimentalAnimationApi
@Composable
fun BottomBar(navController: NavHostController, bottomBarState: MutableState<Boolean>, topBarState: MutableState<Boolean>){
    val barItems = listOf(
        BarItem(
            title = "Reminder",
            image = painterResource(R.drawable.clipboard_notes),
            route = "reminder"
        ),
        BarItem(
            title = "Alarm",
            image = painterResource(R.drawable.alarm_clock),
            route = "alarm"
        ),
        BarItem(
            title = "Timetable",
            image = painterResource(R.drawable.calendar_days),
            route = "timetable"
        )
    )

    // Log.v("BottomBarState", bottomBarState.toString())
    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            BottomNavigation(
                backgroundColor = colorResource(id = R.color.light_gray)
            ){
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route
                // val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
                //backStackEntry?.destination?.route?.let { Log.v("backStackEntry", it) }
                when (backStackEntry?.destination?.route) {
                    "reminder" -> {
                        // Show BottomBar and TopBar
                        bottomBarState.value = false
                        topBarState.value = false
                        //Log.v("State","Reminder!!!")
                    }
                    "alarm" -> {
                        // Show BottomBar and TopBar
                        bottomBarState.value = false
                        topBarState.value = false
                    }
                    "timetable" -> {
                        // Show BottomBar and TopBar
                        bottomBarState.value = false
                        topBarState.value = false
                    }
                    "settings" -> {
                        // Hide BottomBar and TopBar
                        bottomBarState.value = false
                        topBarState.value = false
                    }
                    "home" -> {
                        bottomBarState.value = true
                        topBarState.value = true
                    }
                }

                barItems.forEach { navItem ->
                    BottomNavigationItem(
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
                                painter = navItem.image,
                                contentDescription = navItem.title,
                                tint = colorResource(id = R.color.orange),
                                modifier = Modifier.size(28.dp)
                            )
                        },
                        /*
                        label = {
                            Text(text = navItem.title)
                        }
                        */
                    )
                }

            }
        }
    )
}

@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route
    ) {
        composable(NavRoutes.Home.route){
            Connections()
        }

        composable(NavRoutes.Reminder.route) {
            // Reminder
            SettingUi()
        }

        composable(NavRoutes.Alarm.route) {
            // Alarm
            SettingUi()
        }

        composable(NavRoutes.Timetable.route) {
            // Timetable
            SettingUi()
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MyScaffold(){
    val scaffoldState = rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val navController = rememberNavController()

    val topBarState = rememberSaveable { (mutableStateOf(true)) }
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

    Scaffold (
        scaffoldState = scaffoldState,
        topBar = { TopBar(navController = navController, bottomBarState = bottomBarState, topBarState = topBarState) },
        content = { NavigationHost(navController = navController) },
        bottomBar = { BottomBar(navController = navController, bottomBarState = bottomBarState, topBarState = topBarState) }
    )
}


@Composable
private fun CardContent(name: String) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(modifier = Modifier
            .weight(1f)
            .padding(12.dp)
        ) {
            Text(
                text = "Hello, $name",
                color = colorResource(id = R.color.text_white),
                fontSize = 12.sp
            )
            if (expanded) {
                Text(
                    text = ("Coposem ipsum color sit lazy, " +
                            "padding theme elit, sed do bouncy").repeat(4),
                    color = colorResource(id = R.color.text_white),
                    fontSize = 10.sp

                )
            }
        }
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                tint = colorResource(id = R.color.text_white),
                contentDescription = if (expanded) {
                    stringResource(id = R.string.show_less)
                } else {
                    stringResource(id = R.string.show_more)
                }
            )
        }
    }
}

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
        MyScaffold()
    }
}