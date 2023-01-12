package at.fh.mappdev.rootnavigator

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
            .background(MaterialTheme.colors.primary)
            .paint(
                painter = painterResource(R.drawable.threelines),
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
                    text = "ON / OFF",
                    textAlign = TextAlign.Left,
                    fontSize = 18.sp,
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = switchStateOn.value,
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
                            text = "Automatic / Manual",
                            textAlign = TextAlign.Left,
                            fontSize = 18.sp,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(
                            checked = switchStateAutomatic.value,
                            onCheckedChange = { switchStateAutomatic.value = it },
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
                                text = "Pick Time",
                                color = MaterialTheme.colors.surface,
                                fontSize = 14.sp
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
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        TextField(
                            value = numberOfAlarms,
                            onValueChange = { numberOfAlarms = it },
                            modifier = Modifier
                                .height(height = 60.dp)
                                .width(width = 150.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = MaterialTheme.colors.secondaryVariant
                            ),
                            label = { Text(text = "alarms") },
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
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        TextField(
                            value = interval,
                            onValueChange = { interval = it },
                            modifier = Modifier
                                .height(height = 60.dp)
                                .width(width = 150.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = MaterialTheme.colors.secondaryVariant
                            ),
                            label = { Text(text = "interval") },
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
                            text = "Wake-up Sound",
                            textAlign = TextAlign.Left,
                            fontSize = 18.sp,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        TextField(
                            value = wakeUpSound,
                            onValueChange = { wakeUpSound = it },
                            modifier = Modifier
                                .height(height = 60.dp)
                                .width(width = 150.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = MaterialTheme.colors.secondaryVariant
                            ),
                            label = { Text(text = "Select sound") },
                            textStyle = TextStyle(
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 18.sp,
                                color = MaterialTheme.colors.surface
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text
                            )
                        )
                    }
                })
        }

            Column(
                modifier = Modifier
                    .padding(
                        bottom = 32.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
            ) {
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
                                color = MaterialTheme.colors.surface,
                                fontSize = 18.sp
                            )
                        }
            }

    }
}