package at.fh.mappdev.rootnavigator

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
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
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import at.fh.mappdev.rootnavigator.database.ReminderItemRoom
import at.fh.mappdev.rootnavigator.database.ReminderRepository
import at.fh.mappdev.rootnavigator.database.ReminderViewModel

class ReminderOverviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RootNavigatorTheme {
                Surface(color = MaterialTheme.colors.primary) {
                }
            }
        }
    }
}

@Composable
fun Reminder(reminder : ReminderItemRoom, Context: Context, viewModel : ReminderViewModel) {
    Card(
        modifier = Modifier.padding(
            vertical = 12.dp
        ),
        backgroundColor = MaterialTheme.colors.primaryVariant,
        shape = RoundedCornerShape(25.dp)
    ) {
        ReminderContent(reminder, Context, viewModel)
    }
}

@Composable
fun ReminderOverviewUI(navController: NavHostController, viewModel: ReminderViewModel, Context: Context = LocalContext.current) {

    val reminders = viewModel.reminders.observeAsState()
    viewModel.refreshReminder()

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

        LazyColumn(
            modifier = Modifier
                .height(530.dp)
                .padding(top = 12.dp)
        ) {
            reminders.value?.map { item {Reminder(it, Context, viewModel)} }
        }

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
private fun ReminderContent(reminder: ReminderItemRoom, Context: Context, viewModel : ReminderViewModel) {
    var expanded by remember { mutableStateOf(false) }
    var isActive by remember { mutableStateOf(reminder.ReminderActive)}

    Column(
        modifier = Modifier
            .padding(
                start = 24.dp,
                end = 12.dp,
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
            Text(
                text = reminder.ReminderDate + " - " + reminder.ReminderTime,
                color = MaterialTheme.colors.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                ReminderRepository.deleteReminder(Context, reminder)
                viewModel.localRefresh()
            }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    tint = MaterialTheme.colors.secondary,
                    contentDescription = "Delete"
                )
            }

            IconButton(
                onClick = {
                    reminder.ReminderActive = !reminder.ReminderActive
                    ReminderRepository.updateReminder(Context, reminder)
                    isActive = !isActive
                }
            ) {
                Icon(
                    imageVector = if (isActive) Icons.Filled.CheckBox else Icons.Filled.CheckBoxOutlineBlank,
                    tint = MaterialTheme.colors.secondary,
                    contentDescription = if (isActive) {
                        stringResource(id = R.string.done)
                    } else {
                        stringResource(id = R.string.undone)
                    }
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
                    }
                )
            }
        }
        Column {
            if (expanded) {
                Text(
                    text = "Description :",
                    color = MaterialTheme.colors.surface,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.padding(top = 16.dp))
                Text(
                    text = reminder.ReminderDescription,
                    color = MaterialTheme.colors.surface,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.padding(top = 16.dp))
            }
        }
    }
}

