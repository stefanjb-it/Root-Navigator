package at.fh.mappdev.rootnavigator

import android.content.Context
import android.content.SharedPreferences
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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import at.fh.mappdev.rootnavigator.database.PrefHolder
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RootNavigatorTheme {
                Surface(color = MaterialTheme.colors.primary) {
                    //SettingUi()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingUi(navController: NavHostController, preferences: SharedPreferences, Context: Context = LocalContext.current) {

    var typelist = listOf("Student", "Normal")
    var expanded by remember { mutableStateOf(false) }

    var type by remember { mutableStateOf(preferences.getString(PrefHolder.TYPE, "") ?: "") }
    var degreeprogram by remember { mutableStateOf(preferences.getString(PrefHolder.PROGRAMME, "") ?: "") }
    var group by remember { mutableStateOf(preferences.getString(PrefHolder.GROUP, "") ?: "") }
    var preferredRootpoint by remember { mutableStateOf(preferences.getString(PrefHolder.ROOTPOINT, "") ?: "") }
    var preferredLine by remember { mutableStateOf(preferences.getString(PrefHolder.PREFERREDLINE, "") ?: "") }

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
                    color = MaterialTheme.colors.surface,
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
                        value = type ,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.secondaryVariant
                        ),
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
                    color = MaterialTheme.colors.surface,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                TextField(
                    value = degreeprogram,
                    onValueChange = { degreeprogram = it },
                    modifier = Modifier
                        .height(height = 60.dp)
                        .width(150.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.secondaryVariant
                    ),
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
                    color = MaterialTheme.colors.surface,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                TextField(
                    value = group,
                    onValueChange = { group = it },
                    modifier = Modifier
                        .height(height = 60.dp)
                        .width(150.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.secondaryVariant
                    ),
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
                    color = MaterialTheme.colors.surface,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                TextField(
                    value = preferredRootpoint,
                    onValueChange = { preferredRootpoint = it },
                    modifier = Modifier
                        .height(height = 60.dp)
                        .width(150.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.secondaryVariant
                    ),
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
                    color = MaterialTheme.colors.surface,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                TextField(
                    value = preferredLine,
                    onValueChange = { preferredLine = it },
                    modifier = Modifier
                        .height(height = 60.dp)
                        .width(150.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.secondaryVariant
                    ),
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
                    onClick = {
                        if (type != "" && degreeprogram != "" && group != "" && preferredRootpoint != ""){

                            preferences.edit().putString(PrefHolder.TYPE, type).apply()
                            preferences.edit().putString(PrefHolder.PROGRAMME, degreeprogram).apply()
                            preferences.edit().putString(PrefHolder.GROUP, group).apply()
                            preferences.edit().putString(PrefHolder.PREFERREDLINE, preferredLine).apply()
                            preferences.edit().putString(PrefHolder.ROOTPOINT, preferredRootpoint).apply()
                            Toast.makeText(Context, "Saved", Toast.LENGTH_SHORT).show()

                            navController.navigate("home") {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }

                        } else {
                            Toast.makeText(Context, "You Donkey! You not Gigasaal!", Toast.LENGTH_SHORT).show()
                        }
                    },
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