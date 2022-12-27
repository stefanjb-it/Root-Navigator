package at.fh.mappdev.rootnavigator

import android.content.Context
import android.os.Bundle
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
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme

class SettingsActivity : ComponentActivity() {
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
fun SettingUi(Context: Context = LocalContext.current) {
    var typelist = listOf("Student", "Normal")
    var type by remember { mutableStateOf("") }
    var degreeprogram by remember { mutableStateOf("") }
    var group by remember { mutableStateOf("") }
    var preferredRootpoint by remember { mutableStateOf("") }
    var preferredLine by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

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
            // Row 1
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Type",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier
                        .height(height = 60.dp)
                        .width(150.dp)
                ) {
                    TextField(
                        readOnly = true,
                        value = type,
                        onValueChange = { type = it },
                        label = { Text("Type") })
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {expanded = false},
                        modifier = Modifier.background(MaterialTheme.colors.primaryVariant)
                    ) {
                        typelist.forEach {
                            DropdownMenuItem(
                                onClick = {
                                type = it
                                expanded = false}
                            ) {
                                Text(text = it)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.padding(top = 10.dp))

            // Row 2
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Degree Program",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                TextField(
                    value = degreeprogram,
                    onValueChange = { degreeprogram = it },
                    modifier = Modifier
                        .height(height = 60.dp)
                        .width(150.dp),
                    label = { Text(text = "Degree Program") },
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

            // Row 3
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Group",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                TextField(
                    value = group,
                    onValueChange = { group = it },
                    modifier = Modifier
                        .height(height = 60.dp)
                        .width(150.dp),
                    label = { Text(text = "Group") },
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

            // Row 4
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Preferred Rootpoint",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                TextField(
                    value = preferredRootpoint,
                    onValueChange = { preferredRootpoint = it },
                    modifier = Modifier
                        .height(height = 60.dp)
                        .width(150.dp),
                    label = { Text(text = "Rootpoint") },
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

            // Row 5
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Preferred Lines",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                TextField(
                    value = preferredLine,
                    onValueChange = { preferredLine = it },
                    modifier = Modifier
                        .height(height = 60.dp)
                        .width(150.dp),
                    label = { Text(text = "Line") },
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

            // Button Row
            Row(modifier = Modifier.padding(top = 32.dp)) {
                Button(
                    onClick = { Toast.makeText(Context, "Saved", Toast.LENGTH_SHORT).show() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 60.dp),
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