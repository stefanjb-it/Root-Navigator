package at.fh.mappdev.rootnavigator.alarm

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import at.fh.mappdev.rootnavigator.NotificationInfo
import at.fh.mappdev.rootnavigator.R
import at.fh.mappdev.rootnavigator.auth.LoginActivity
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme
import java.util.*

// screen to set an alarm
class AlarmActivity : ComponentActivity() {
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
fun AlarmUi(context: Context = LocalContext.current, alarmManager: AlarmManager, preferences : SharedPreferences,) {
    val switchStateOn = remember {mutableStateOf(false)}
    var wakeUpTime by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        NotificationInfo.NOTIFICATIONPERMISSION = ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    } else {
        NotificationInfo.NOTIFICATIONPERMISSION = true
    }

    val timePickerDialog = TimePickerDialog(
        context,
        { _: TimePicker, hour: Int, minute: Int ->
            wakeUpTime = "${if (hour <10) { "0" + hour} else {hour}}:${if (minute <10) { "0" + minute} else {minute}}"
        }, hour, minute, true
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .paint(
                painter = painterResource(if (!isSystemInDarkTheme()) R.drawable.threelines_new_light else R.drawable.threelines_new),
                contentScale = ContentScale.FillWidth
            )
    ) {

        Column(
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                )
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                backgroundColor = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(25.dp),
                elevation = 10.dp
            ) {
                Column(modifier = Modifier.padding(all = 24.dp),
                verticalArrangement = Arrangement.Center){
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "OFF / ON",
                            textAlign = TextAlign.Left,
                            fontSize = 24.sp,
                            color = MaterialTheme.colors.surface
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(
                            checked = switchStateOn.value,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colors.secondary,
                                checkedTrackColor = MaterialTheme.colors.secondary,
                                uncheckedThumbColor = MaterialTheme.colors.surface,
                                uncheckedTrackColor = MaterialTheme.colors.surface
                            ),
                            onCheckedChange = { switchStateOn.value = it },
                        )
                    }

                    androidx.compose.animation.AnimatedVisibility(
                        visible = switchStateOn.value,
                        enter = slideInVertically(initialOffsetY = { -it }),
                        exit = slideOutVertically(targetOffsetY = { -it }),
                        content = {

                            Row(modifier = Modifier.padding(top = 18.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = { timePickerDialog.show() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(height = 65.dp),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                                ) {
                                    Text(
                                        text = if (wakeUpTime == "") "Pick Time" else wakeUpTime,
                                        color = MaterialTheme.colors.onSurface,
                                        fontSize = 24.sp
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(
                    bottom = 32.dp,
                    start = 16.dp,
                    end = 16.dp
                ))
        {
            Button(
                onClick = {
                    if (switchStateOn.value && wakeUpTime != ""){

                        val timeArray = wakeUpTime.split(":")
                        val id = preferences.getInt("ALARMID", 1)

                        preferences.edit().putInt("ALARMID",(id+1)).apply()

                        val selectedDateTime = Calendar.getInstance()
                        selectedDateTime.set(
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH),
                            timeArray[0].toInt(),
                            timeArray[1].toInt(),
                            0
                        )

                        if (selectedDateTime.timeInMillis < Calendar.getInstance().timeInMillis){
                            selectedDateTime.timeInMillis += 86400000L
                        }

                        if (NotificationInfo.NOTIFICATIONPERMISSION) {
                            scheduleAlarm(context, selectedDateTime.timeInMillis)
                        }

                        Toast.makeText(context, "SET, remember to alter media volume!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Please select or enter valid values!", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
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

// starts scheduling process
fun scheduleAlarm(ctx:Context, time:Long) {
    val alarm: Alarm = Alarm(
        time,
        true,
    )
    alarm.schedule(ctx)
}