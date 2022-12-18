package at.fh.mappdev.rootnavigator

import android.content.Context
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme

var type = mutableStateOf("")


class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RootNavigatorTheme {
                Surface(color = MaterialTheme.colors.background) {
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

    Row() {
        Column(modifier = Modifier.padding(top = 120.dp, start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Type",
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(
                        top = 15.dp,
                        bottom = 20.dp
                    )
            )

            Text(text = "Degree Program",
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(
                        top = 40.dp,
                        bottom = 20.dp)
            )

            Text(text = "Group",
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                modifier = Modifier.
                padding(
                    top = 30.dp,
                    bottom = 20.dp)
            )

            Text(text = "Preferred Rootpoint",
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                modifier = Modifier.padding(
                    top = 30.dp,
                    bottom = 20.dp)
            )

            Text(text = "Preferred Lines",
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                modifier = Modifier.padding(
                    top = 30.dp,
                    bottom = 20.dp)
            )
        }
        Column(
            modifier = Modifier.padding(top = 120.dp, start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {expanded = !expanded},
                modifier = Modifier.height(height = 60.dp)) {
                TextField(
                    readOnly = true,
                    value = type,
                    onValueChange = {type = it},
                    label = {Text("Type")})
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }) {
                    typelist.forEach {
                        DropdownMenuItem(onClick = {
                            type = it
                            expanded = false
                        }) {
                            Text(text = it)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.padding(top = 15.dp))
            OutlinedTextField(
                value = degreeprogram,
                onValueChange = { degreeprogram = it },
                modifier = Modifier.height(height = 60.dp),
                label = { Text(text = "Degree Program") },
                textStyle = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 18.sp,
                    color = Color.Black
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )
            Spacer(modifier = Modifier.padding(top = 15.dp))
            OutlinedTextField(
                value = group,
                onValueChange = { group = it },
                modifier = Modifier.height(height = 60.dp),
                label = { Text(text = "Group") },
                textStyle = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 18.sp,
                    color = Color.Black
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )
            Spacer(modifier = Modifier.padding(top = 15.dp))
            OutlinedTextField(
                value = preferredRootpoint,
                onValueChange = { preferredRootpoint = it },
                modifier = Modifier.height(height = 60.dp),
                label = { Text(text = "Rootpoint") },
                textStyle = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 18.sp,
                    color = Color.Black
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )
            Spacer(modifier = Modifier.padding(top = 15.dp))
            OutlinedTextField(
                value = preferredLine,
                onValueChange = { preferredLine = it },
                modifier = Modifier.height(height = 60.dp),
                label = { Text(text = "Line") },
                textStyle = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 18.sp,
                    color = Color.Black
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )
        }
    }
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 720.dp, start = 20.dp, end = 20.dp)) {
        Button(
            onClick = { Toast.makeText(Context, "Saved", Toast.LENGTH_SHORT).show() },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.orange)),
        ) {
            Text(text = "Save", color = Color.White, fontSize = 18.sp)
        }
    }
}