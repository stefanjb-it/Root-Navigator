package at.fh.mappdev.rootnavigator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme

class ReminderOverviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RootNavigatorTheme {
                Surface(color = MaterialTheme.colors.primary) {
                    ReminderOverviewUI(navController = rememberNavController())
                }
            }
        }
    }
}

@Composable
fun Reminder(name: String) {
    Card(
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        backgroundColor = MaterialTheme.colors.primaryVariant,
        shape = RoundedCornerShape(25.dp)
    ) {
        ReminderContent(name)
    }
}

@Composable
private fun Reminders(
    reminderList: List<String> = List(10) { "$it" }
) {
    LazyColumn(
        modifier = Modifier.height(530.dp)
    ) {
        items(items = reminderList) {
                reminder -> Reminder(name = reminder)
        }
    }
}

@Composable
fun ReminderOverviewUI(navController: NavHostController, Context: Context = LocalContext.current) {
    // val navController = rememberNavController()
    // val navigation = Navigation()
    // navigation.NavigationHost(navController = navController, NavRoutes.Reminder.route)
    // Navigation(navController = navController, startRoute = NavRoutes.Reminder.route)

    /*
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
     */

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

        Reminders()

        Row(
            modifier = Modifier
                .weight(1f, false)
                .padding(
                    bottom = 90.dp
                )
        ) {
            Button(
                onClick = {
                    navController.navigate("new_reminder") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
            ) {
                Text(
                    text = stringResource(id = R.string.button_new_reminder),
                    color = MaterialTheme.colors.surface,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
private fun ReminderContent(name: String) {
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
                text = "Reminder, $name",
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