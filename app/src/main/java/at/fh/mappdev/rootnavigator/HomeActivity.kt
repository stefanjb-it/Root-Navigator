package at.fh.mappdev.rootnavigator

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
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
import at.fh.mappdev.rootnavigator.database.ReminderViewModel
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel : ReminderViewModel by viewModels()
        val sharedPrefs = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        setContent {
            RootNavigatorTheme {
                    MyScaffold(viewModel, sharedPrefs)
            }
        }
    }
}

@Composable
fun Connection(name: String) {
    Card(
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        backgroundColor = MaterialTheme.colors.primaryVariant,
        shape = RoundedCornerShape(25.dp)
    ) {
        CardContent(name)
    }
}

@Composable
fun Connections(
    modifier: Modifier = Modifier,
    connections: List<String> = List(1000) { "$it" }
) {
    LazyColumn(
        modifier = modifier
            .background(MaterialTheme.colors.primary)
            .paint(
                painter = painterResource(R.drawable.threelines),
                contentScale = ContentScale.FillWidth
            )
    ) {
        items(items = connections) {
                connection -> Connection(name = connection)
        }
    }
}

@Composable
fun TopBar(navController: NavHostController, bottomBarState: MutableState<Boolean>, topBarState: MutableState<Boolean>){
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
                            painter = painterResource(id = R.drawable.settings),
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
            image = painterResource(R.drawable.home_door),
            route = "home"
        ),
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
                                painter = navItem.image,
                                contentDescription = navItem.title,
                                tint = MaterialTheme.colors.secondary,
                                modifier = Modifier.size(28.dp)
                            )
                        },
                        label = {
                            Text(
                                text = navItem.title,
                                // color = MaterialTheme.colors.surface
                                color = MaterialTheme.colors.primary
                            )
                        }
                    )
                }

            }
        }
    )
}


@Composable
fun NavigationHost(navController: NavHostController, reminderViewModel: ReminderViewModel, preferences : SharedPreferences) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route
    ) {
        composable(NavRoutes.Home.route){
            // Home Screen
            Connections()
        }

        composable(NavRoutes.Reminder.route) {
            // Reminder
            // LoginUI()
            // NewReminderUI()
            ReminderOverviewUI(navController, reminderViewModel)
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
fun MyScaffold(reminderViewModel: ReminderViewModel, preferences: SharedPreferences){
    val scaffoldState = rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val navController = rememberNavController()

    val topBarState = rememberSaveable { (mutableStateOf(true)) }
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

    Scaffold (
        scaffoldState = scaffoldState,
        topBar = { TopBar(navController = navController, bottomBarState = bottomBarState, topBarState = topBarState) },
        content = { NavigationHost(navController = navController, reminderViewModel = reminderViewModel, preferences) },
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
                color = MaterialTheme.colors.surface,
                fontSize = 14.sp
            )
            if (expanded) {
                Text(
                    text = ("Coposem ipsum color sit lazy, " +
                            "padding theme elit, sed do bouncy").repeat(4),
                    color = MaterialTheme.colors.surface,
                    fontSize = 12.sp
                )
            }
        }
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                tint = MaterialTheme.colors.surface,
                contentDescription = if (expanded) {
                    stringResource(id = R.string.show_less)
                } else {
                    stringResource(id = R.string.show_more)
                }
            )
        }
    }
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