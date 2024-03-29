package at.fh.mappdev.rootnavigator

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import at.fh.mappdev.rootnavigator.database.GlobalVarHolder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingUi(navController: NavHostController, preferences: SharedPreferences, Context: Context = LocalContext.current) {

    val typelist = listOf("Student", "Normal")
    var expanded by remember { mutableStateOf(false) }
    var type by remember { mutableStateOf(preferences.getString(GlobalVarHolder.TYPE, "") ?: "") }
    var degreeprogram by remember { mutableStateOf(preferences.getString(GlobalVarHolder.PROGRAMME, "") ?: "") }
    var group by remember { mutableStateOf(preferences.getString(GlobalVarHolder.GROUP, "") ?: "") }
    var preferredRootpoint by remember { mutableStateOf(preferences.getString(GlobalVarHolder.ROOTPOINT, "") ?: "") }
    var preferredLine by remember { mutableStateOf(preferences.getString(GlobalVarHolder.PREFERREDLINE, "") ?: "") }
    var duration by remember { mutableStateOf(preferences.getString(GlobalVarHolder.REQUESTTIME, "30") ?: "30") }
    val user = FirebaseAuth.getInstance().currentUser

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
                    horizontal = 32.dp,
                    vertical = 32.dp
                )
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {

            // Row 1
            Row(verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "Welcome ${user?.email}!",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.surface,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.padding(bottom = 10.dp))

            // Row 2
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
                            backgroundColor = MaterialTheme.colors.primary,
                            textColor = MaterialTheme.colors.surface
                        ),
                        onValueChange = { type = it },
                        label = { Text("Type", color = MaterialTheme.colors.secondary) })
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {expanded = false},
                        modifier = Modifier.background(MaterialTheme.colors.primary)
                    ) {
                        typelist.forEach {
                            DropdownMenuItem(
                                onClick = {
                                    type = it
                                    expanded = false}
                            ) {
                                Text(text = it, color = MaterialTheme.colors.surface)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.padding(top = 10.dp))

            // Row 3
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
                    singleLine = true,
                    modifier = Modifier
                        .height(height = 60.dp)
                        .width(150.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        textColor = MaterialTheme.colors.surface
                    ),
                    label = { Text(text = "Degree Program", color = MaterialTheme.colors.secondary) },
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
                    text = "Group",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.surface,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                TextField(
                    value = group,
                    onValueChange = { group = it },
                    singleLine = true,
                    modifier = Modifier
                        .height(height = 60.dp)
                        .width(150.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        textColor = MaterialTheme.colors.surface
                    ),
                    label = { Text(text = "Group", color = MaterialTheme.colors.secondary) },
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
                    text = "Preferred Rootpoint",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.surface,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                TextField(
                    value = preferredRootpoint,
                    onValueChange = { preferredRootpoint = it },
                    singleLine = true,
                    modifier = Modifier
                        .height(height = 60.dp)
                        .width(150.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        textColor = MaterialTheme.colors.surface
                    ),
                    label = { Text(text = "Rootpoint", color = MaterialTheme.colors.secondary) },
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

            // Row 6
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
                    singleLine = true,
                    modifier = Modifier
                        .height(height = 60.dp)
                        .width(150.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        textColor = MaterialTheme.colors.surface
                    ),
                    label = { Text(text = "Line", color = MaterialTheme.colors.secondary) },
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

            // Row 7
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Duration:",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.surface,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                TextField(
                    value = duration,
                    onValueChange = { duration = it },
                    singleLine = true,
                    modifier = Modifier
                        .height(height = 60.dp)
                        .width(150.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        textColor = MaterialTheme.colors.surface
                    ),
                    label = { Text(text = "Minutes", color = MaterialTheme.colors.secondary) },
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
            Row(modifier = Modifier.padding(top = 24.dp)) {
                Button(
                    onClick = {
                        val studentParameterCheck = if (type == "Student") degreeprogram != "" && group != "" else true

                        if (type != "" && studentParameterCheck && preferredRootpoint != "" && duration.toIntOrNull() != null && duration.toInt() > 0 && duration.toInt() < 69){

                            preferences.edit().putString(GlobalVarHolder.TYPE, type).apply()
                            preferences.edit().putString(GlobalVarHolder.PROGRAMME, degreeprogram).apply()
                            preferences.edit().putString(GlobalVarHolder.GROUP, group).apply()
                            preferences.edit().putString(GlobalVarHolder.PREFERREDLINE, preferredLine).apply()
                            preferences.edit().putString(GlobalVarHolder.ROOTPOINT, preferredRootpoint).apply()
                            preferences.edit().putString(GlobalVarHolder.REQUESTTIME, duration).apply()
                            GlobalVarHolder.localReqTime = duration
                            Toast.makeText(Context, "Saved successfully", Toast.LENGTH_SHORT).show()

                            val student = (type == "Student")

                            val userdata = hashMapOf(
                                "Degree Program" to degreeprogram,
                                "Group" to group,
                                "Preferred Lines" to preferredLine,
                                "Preferred Rootpoint" to preferredRootpoint,
                                "Type" to student
                            )

                            FirebaseFirestore.getInstance().collection("USER_CONFIG")
                                .document(user!!.uid).set(userdata)
                                .addOnSuccessListener { preferences.edit().putBoolean(GlobalVarHolder.TOBESAVED, false).apply() }
                                .addOnFailureListener { preferences.edit().putBoolean(GlobalVarHolder.TOBESAVED, true).apply() }

                            navController.navigate("home") {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }

                        } else {
                            Toast.makeText(Context, "Please enter valid data for all parameters.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 60.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                ) {
                    Text(
                        text = stringResource(id = R.string.button_save),
                        color = MaterialTheme.colors.onSurface,
                        fontSize = 18.sp
                    )
                }
            }

            // Logout Button Row
            Row(modifier = Modifier.padding(top = 24.dp)) {
                Button(
                    onClick = {
                        if (FirebaseAuth.getInstance().currentUser != null) {
                            FirebaseAuth.getInstance().signOut()
                            preferences.edit().putString(GlobalVarHolder.TYPE, "").apply()
                            preferences.edit().putString(GlobalVarHolder.PROGRAMME, "").apply()
                            preferences.edit().putString(GlobalVarHolder.GROUP, "").apply()
                            preferences.edit().putString(GlobalVarHolder.PREFERREDLINE, "").apply()
                            preferences.edit().putString(GlobalVarHolder.ROOTPOINT, "").apply()
                            preferences.edit().putString(GlobalVarHolder.TYPE, "").apply()
                            preferences.edit().putLong(GlobalVarHolder.LASTLOGGEDIN, 0L).apply()
                            preferences.edit().putBoolean(GlobalVarHolder.TOBESAVED, false).apply()
                            preferences.edit().putInt(GlobalVarHolder.NOTIFICATIONID, 0).apply()
                            preferences.edit().putString(GlobalVarHolder.REQUESTTIME, "30").apply()
                            GlobalVarHolder.localReqTime = "30"
                            GlobalVarHolder.location.value?.altitude = 0.0
                            GlobalVarHolder.location.value?.latitude = 0.0

                            navController.popBackStack()
                            val activity = (Context as? Activity)
                            activity?.finish()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 60.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                ) {
                    Text(
                        text = stringResource(id = R.string.button_sign_out),
                        color = MaterialTheme.colors.onSurface,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.padding(top = 18.dp))

            Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally){
                ClickableText(text = AnnotatedString("If you like this Project, click here!"),
                    onClick = {
                        val openURL = Intent(Intent.ACTION_VIEW)
                        openURL.data = Uri.parse("https://www.buymeacoffee.com/rootnavigator")
                        Context.startActivity(openURL)
                    },
                    style = TextStyle(
                        color = MaterialTheme.colors.surface,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Start
                    )
                )
            }
        }
    }
}