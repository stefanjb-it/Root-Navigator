package at.fh.mappdev.rootnavigator.alarm

import android.app.TimePickerDialog
import android.content.Context
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
import at.fh.mappdev.rootnavigator.R
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme
import java.util.*

class AlarmActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RootNavigatorTheme {
                Surface(color = MaterialTheme.colors.primary) {
                    // SettingUi()
                }
            }
        }
    }
}

@Composable
fun AlarmUi(context: Context = LocalContext.current) {
    val switchStateOn = remember {mutableStateOf(false)}
    val switchStateAutomatic = remember {mutableStateOf(false)}

    var wakeUpTime by remember { mutableStateOf("") }
    var numberOfAlarms by remember { mutableStateOf("") }
    var interval by remember { mutableStateOf("") }
    var wakeUpSound by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR)
    val minute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = TimePickerDialog(
        context,
        { _: TimePicker, hour: Int, minute: Int ->
            wakeUpTime = "${if (hour <10) { "0" + hour} else {hour}}:${if (minute <10) { "0" + minute} else {minute}}"
        }, hour, minute, true
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            //.background(MaterialTheme.colors.primary)
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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "OFF / ON",
                    textAlign = TextAlign.Left,
                    fontSize = 18.sp,
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

            Spacer(modifier = Modifier.padding(top = 8.dp))

            androidx.compose.animation.AnimatedVisibility(
                visible = switchStateOn.value,
                enter = slideInVertically(initialOffsetY = { -it }),
                exit = slideOutVertically(targetOffsetY = { -it }),
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Wake up time",
                            textAlign = TextAlign.Left,
                            fontSize = 18.sp,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = { timePickerDialog.show() },
                            modifier = Modifier
                                .width(width = 150.dp)
                                .height(height = 35.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                        ) {
                            Text(
                                text = if (wakeUpTime == "") "Pick Time" else wakeUpTime,
                                color = MaterialTheme.colors.onSurface,
                                fontSize = 15.sp
                            )
                        }
                    }
                })

            Spacer(modifier = Modifier.padding(top = 24.dp))

            androidx.compose.animation.AnimatedVisibility(
                visible = switchStateOn.value,
                enter = slideInVertically(initialOffsetY = { -it }),
                exit = slideOutVertically(targetOffsetY = { -it }),
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Number of alarms",
                            textAlign = TextAlign.Left,
                            fontSize = 18.sp,
                            color = MaterialTheme.colors.surface
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        TextField(
                            value = numberOfAlarms,
                            onValueChange = { numberOfAlarms = it },
                            singleLine = true,
                            modifier = Modifier
                                .height(height = 60.dp)
                                .width(width = 150.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = MaterialTheme.colors.primary
                            ),
                            label = { Text(text = "times", color = MaterialTheme.colors.secondary) },
                            textStyle = TextStyle(
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 18.sp,
                                color = MaterialTheme.colors.surface
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )
                    }
                })

            Spacer(modifier = Modifier.padding(top = 24.dp))

            androidx.compose.animation.AnimatedVisibility(
                visible = switchStateOn.value,
                enter = slideInVertically(initialOffsetY = { -it }),
                exit = slideOutVertically(targetOffsetY = { -it }),
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Interval",
                            textAlign = TextAlign.Left,
                            fontSize = 18.sp,
                            color = MaterialTheme.colors.surface
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        TextField(
                            value = interval,
                            onValueChange = { interval = it },
                            singleLine = true,
                            modifier = Modifier
                                .height(height = 60.dp)
                                .width(width = 150.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = MaterialTheme.colors.primary
                            ),
                            label = { Text(text = "minutes", color = MaterialTheme.colors.secondary) },
                            textStyle = TextStyle(
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 18.sp,
                                color = MaterialTheme.colors.surface
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )
                    }
                })

            Spacer(modifier = Modifier.padding(top = 24.dp))

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
                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
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