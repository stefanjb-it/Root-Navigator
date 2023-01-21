package at.fh.mappdev.rootnavigator.bars

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import at.fh.mappdev.rootnavigator.bars.BarItem

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
                backgroundColor = MaterialTheme.colors.primary
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
                                    tint = MaterialTheme.colors.surface,
                                    modifier = Modifier.size(28.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = navItem.title,
                                    color = MaterialTheme.colors.surface,
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
                                    color = MaterialTheme.colors.surface,
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