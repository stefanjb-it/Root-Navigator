package at.fh.mappdev.rootnavigator

import android.content.Context
import android.os.Bundle
import android.widget.Space
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme

class AlarmActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RootNavigatorTheme {
                Surface(color = MaterialTheme.colors.primary) {
                    SettingUi()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AlarmUi(Context: Context = LocalContext.current) {
    val switchStateOn = remember {mutableStateOf(false)}
    val switchStateAutomatic = remember {mutableStateOf(false)}

    var wakeUpTime by remember { mutableStateOf("") }
    var numberOfAlarms by remember { mutableStateOf("") }
    var interval by remember { mutableStateOf("") }
    var wakeUpSound by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
    ) {

        Column(
            modifier = Modifier
                .padding(
                    horizontal = 32.dp,
                    vertical = 32.dp
                )
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
                    onCheckedChange = {switchStateOn.value = it},
                )
            }

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
                    onCheckedChange = {switchStateAutomatic.value = it},
                )
            }
            
            Spacer(modifier = Modifier.padding(top = 10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Wake up time",
                    textAlign = TextAlign.Left,
                    fontSize = 18.sp,
                )
                Spacer(modifier = Modifier.weight(1f))
                TextField(
                    value = wakeUpTime,
                    onValueChange = { wakeUpTime = it },
                    modifier = Modifier
                        .height(height = 60.dp)
                        .width(width = 150.dp),
                    label = { Text(text = "X min before") },
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

            Spacer(modifier = Modifier.padding(top = 10.dp))

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

            Spacer(modifier = Modifier.padding(top = 10.dp))

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

            Spacer(modifier = Modifier.padding(top = 10.dp))

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

            Row(
                modifier = Modifier
                    .padding(
                        top = 50.dp
                    )
            ) {
                Button(
                    onClick = { Toast.makeText(Context, "Saved", Toast.LENGTH_SHORT).show() },
                    modifier = Modifier
                        .fillMaxWidth(),
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
}