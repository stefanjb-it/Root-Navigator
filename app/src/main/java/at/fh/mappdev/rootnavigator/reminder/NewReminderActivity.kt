package at.fh.mappdev.rootnavigator.reminder

import android.Manifest
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import at.fh.mappdev.rootnavigator.NotificationInfo
import at.fh.mappdev.rootnavigator.R
import at.fh.mappdev.rootnavigator.database.GlobalVarHolder
import at.fh.mappdev.rootnavigator.database.ReminderItemRoom
import at.fh.mappdev.rootnavigator.database.ReminderRepository
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme
import java.util.*

object NewReminderActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RootNavigatorTheme {

            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewReminderUI(navController: NavHostController, alarmManager: AlarmManager, preferences: SharedPreferences, bottomBarState: MutableState<Boolean>,context: Context = LocalContext.current){
    val priolist = listOf("High", "Medium", "Low")
    var expanded by remember { mutableStateOf(false) }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("") }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        NotificationInfo.NOTIFICATIONPERMISSION = ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    } else {
        NotificationInfo.NOTIFICATIONPERMISSION = true
    }

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    calendar.time = Date()

    val datePickerDialog = DatePickerDialog (
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            date = "$dayOfMonth/${month+1}/$year"
        }, year, month, day
    )

    val timePickerDialog = TimePickerDialog(
        context,
        { _: TimePicker, hour: Int, minute: Int ->
            time = "${if (hour <10) { "0" + hour} else {hour}}:${if (minute <10) { "0" + minute} else {minute}}"
        }, hour, minute, true
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .paint(
                painter = painterResource(if (!isSystemInDarkTheme()) R.drawable.threelines_light else R.drawable.threelines),
                contentScale = ContentScale.FillWidth
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, top = 32.dp, end = 32.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Button(onClick = { datePickerDialog.show() },
                modifier = Modifier
                    .width(width = 150.dp)
                    .height(height = 35.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
            ) {
                Text(
                    text = if (date == "") "Pick Date" else date,
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(onClick = { timePickerDialog.show() },
                modifier = Modifier
                    .width(width = 150.dp)
                    .height(height = 35.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
            ) {
                Text(
                    text = if (time=="") "Pick Time" else time,
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.padding(top = 12.dp))


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                backgroundColor = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(25.dp),
                elevation = 10.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, end = 32.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                Text(
                    text = "Select Priority",
                    color = MaterialTheme.colors.surface,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.weight(1f))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier
                        .height(height = 60.dp)
                        .width(100.dp)
                ) {
                    TextField(
                        readOnly = true,
                        value = priority ,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            textColor = MaterialTheme.colors.surface
                        ),
                        onValueChange = { priority = it },
                        label = { Text(text = "Priority", color = MaterialTheme.colors.surface) })
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {expanded = false},
                        modifier = Modifier.background(MaterialTheme.colors.primaryVariant),
                    ) {
                        priolist.forEach {
                            DropdownMenuItem(
                                onClick = {
                                    priority = it
                                    expanded = false}
                            ) {
                                Text(text = it, color = MaterialTheme.colors.onSurface)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.padding(top = 12.dp))

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            TextField(
                value = description,
                label = { Text(text = "Description", color = MaterialTheme.colors.surface) },
                onValueChange = { description = it },
                modifier = Modifier
                    .height(height = 150.dp)
                    .fillMaxWidth()
                    .testTag("Description"),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.primary,
                ),
                textStyle = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    color = MaterialTheme.colors.surface,
                    fontSize = 14.sp
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )
        }

        Spacer(modifier = Modifier.padding(top = 12.dp))

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    if (date == ""){
                        date = calendar.get(Calendar.DAY_OF_MONTH).toString() + "/" + (calendar.get(Calendar.MONTH)+ 1).toString() + "/" + calendar.get(Calendar.YEAR).toString()
                    }

                    if (time == "") {
                        time =
                            (calendar.get(Calendar.HOUR_OF_DAY) + 1).toString() + ":" + calendar.get(
                                Calendar.MINUTE
                            ).toString()
                    }

                    if(priority != "" && description != "") {

                        val newReminder = ReminderItemRoom(
                            ReminderDate = date,
                            ReminderTime = time,
                            ReminderActive = false,
                            ReminderDescription = description,
                            ReminderPriority = priority
                        )
                        ReminderRepository.newReminder(context, newReminder)

                        val idNoti = preferences.getInt(GlobalVarHolder.NOTIFICATIONID, 1)
                        preferences.edit().putInt(GlobalVarHolder.NOTIFICATIONID, idNoti + 1)
                            .apply()
                        val dateArray = date.split("/")
                        val timeArray = time.split(":")

                        val selectedDateTime = Calendar.getInstance()
                        selectedDateTime.set(
                            dateArray[2].toInt(),
                            dateArray[1].toInt() - 1,
                            dateArray[0].toInt(),
                            timeArray[0].toInt(),
                            timeArray[1].toInt(),
                            0
                        )

                        if (NotificationInfo.NOTIFICATIONPERMISSION) {
                            setAlarm(
                                context,
                                alarmManager,
                                selectedDateTime.timeInMillis,
                                idNoti,
                                description
                            )
                        }

                        Toast.makeText(context, "Reminder saved", Toast.LENGTH_SHORT).show()

                        bottomBarState.value = true
                        navController.navigate("reminder") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    } else {
                        Toast.makeText(context, "Please select a Priority!", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
            ) {
                Text(
                    text = stringResource(id = R.string.button_save),
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 18.sp
                )
            }
        }
    }
}

fun setAlarm(context: Context, alarmManager: AlarmManager, time : Long, id : Int, description: String){
    val intent = Intent(context, NotificationReceiver::class.java)
    intent.putExtra("ID", id)
    intent.putExtra("DESCRIPTION", description)
    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    Log.i("Notification", "Alarm set for $time with id $id and description $description")
    Log.i("Notification", "Function Start")
    alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent)
}