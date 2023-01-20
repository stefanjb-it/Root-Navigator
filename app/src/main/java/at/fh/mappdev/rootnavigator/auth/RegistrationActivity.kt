package at.fh.mappdev.rootnavigator.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.fh.mappdev.rootnavigator.FirebaseUtils.firebaseAuth
import at.fh.mappdev.rootnavigator.FirebaseUtils.firebaseUser
import at.fh.mappdev.rootnavigator.*
import at.fh.mappdev.rootnavigator.R
import at.fh.mappdev.rootnavigator.database.GlobalVarHolder
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPrefs = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        setContent {
            RootNavigatorTheme {
                Surface(color = MaterialTheme.colors.primary) {
                    RegistrationUIMode(sharedPrefs)
                }
            }
        }
    }
}

@Composable
fun RegistrationUIMode(preferences: SharedPreferences){
    var studentMode by remember { mutableStateOf(false) }
    var buttonClicked by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        //.background(MaterialTheme.colors.primary)
        .background(MaterialTheme.colors.background)
        .paint(
            painter = painterResource(if (!isSystemInDarkTheme()) R.drawable.threelines_new_light else R.drawable.threelines_new),
            contentScale = ContentScale.FillWidth
        )
    ){
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 32.dp,
                )
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Card(
                    backgroundColor = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(25.dp),
                    elevation = 10.dp
                ) {
                    Text(modifier = Modifier.padding(16.dp),
                        text = "Choose your mode",
                        textAlign = TextAlign.Center,
                        fontSize = 27.sp,
                        color = MaterialTheme.colors.secondary)
                }

            }

            Spacer(modifier = Modifier.padding(top = 50.dp))

            Row {
                Button(
                    onClick = {
                        studentMode = true
                        buttonClicked = true
                              },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .testTag("StudentButton"),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                ) {
                    Text(
                        text = stringResource(id = R.string.button_student),
                        color = MaterialTheme.colors.onSurface,
                        fontSize = 27.sp
                    )
                }
            }

            Spacer(modifier = Modifier.padding(top = 50.dp))

            Row {
                Button(
                    onClick = {
                        studentMode = false
                        buttonClicked = true
                        },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .testTag("StandardButton"),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                ) {
                    Text(
                        text = stringResource(id = R.string.button_standard),
                        color = MaterialTheme.colors.secondary,
                        fontSize = 27.sp
                    )
                }
            }
        }
    }

    if (buttonClicked) {
        RegistrationUIAccount(studentMode, preferences)
    }
}

@Composable
fun RegistrationUIAccount(studentMode: Boolean, preferences: SharedPreferences){

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("")}
    var passwordRepeat by remember { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var passwordRepeatVisible by rememberSaveable { mutableStateOf(false) }
    var buttonClicked by remember { mutableStateOf(false) }

    Column(modifier = Modifier
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
                    horizontal = 32.dp,
                )
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Card(
                    backgroundColor = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(25.dp),
                    elevation = 10.dp
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "Enter Account Data",
                        textAlign = TextAlign.Center,
                        fontSize = 27.sp,
                        color = MaterialTheme.colors.secondary
                    )
                }

            }

            Spacer(modifier = Modifier.padding(top = 50.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Card(
                    backgroundColor = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(25.dp),
                    elevation = 10.dp
                ) {
                    Column() {
                    //Row(modifier = Modifier
                        //.fillMaxWidth()) {
                        Text(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 3.dp),
                            text = "E-Mail",
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                            color = MaterialTheme.colors.secondary)
                    //}

                    Row(modifier = Modifier.fillMaxWidth()) {
                        TextField(
                            value = email,
                            onValueChange = { email = it },
                            singleLine = true,
                            modifier = Modifier
                                .height(height = 60.dp)
                                .fillMaxWidth()
                                .testTag("UserEmail"),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = MaterialTheme.colors.primary
                            ),
                            textStyle = TextStyle(
                                fontFamily = FontFamily.SansSerif,
                                color = MaterialTheme.colors.surface,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text
                            )
                        )
                    }
                }}

            }


            Spacer(modifier = Modifier.padding(top = 50.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Card(
                    backgroundColor = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(25.dp),
                    elevation = 10.dp
                ) {
                    Column() {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 3.dp),
                            text = "Password",
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                            color = MaterialTheme.colors.secondary
                        )

                        Row(modifier = Modifier.fillMaxWidth()) {
                            TextField(
                                value = password,
                                onValueChange = { password = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(height = 60.dp)
                                    .testTag("UserPassword"),
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = MaterialTheme.colors.primary
                                ),
                                singleLine = true,
                                textStyle = TextStyle(
                                    fontFamily = FontFamily.SansSerif,
                                    color = MaterialTheme.colors.surface,
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center
                                ),
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                trailingIcon = {
                                    val image = if (passwordVisible)
                                        Icons.Filled.Visibility
                                    else Icons.Filled.VisibilityOff

                                    val description =
                                        if (passwordVisible) "Hide password" else "Show password"

                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(imageVector = image, description)
                                    }
                                }
                            )
                        }
                    }
                }
            }


            Spacer(modifier = Modifier.padding(top = 50.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Card(
                    backgroundColor = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(25.dp),
                    elevation = 10.dp
                ) {
                    Column() {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 3.dp),
                            text = "Repeat Password",
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                            color = MaterialTheme.colors.secondary
                        )

                        Row(modifier = Modifier.fillMaxWidth()) {
                            TextField(
                                value = passwordRepeat,
                                onValueChange = { passwordRepeat = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(height = 60.dp)
                                    .testTag("UserPasswordRepeat"),
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = MaterialTheme.colors.primary
                                ),
                                singleLine = true,
                                textStyle = TextStyle(
                                    fontFamily = FontFamily.SansSerif,
                                    color = MaterialTheme.colors.surface,
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center
                                ),
                                visualTransformation = if (passwordRepeatVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                trailingIcon = {
                                    val image = if (passwordRepeatVisible)
                                        Icons.Filled.Visibility
                                    else Icons.Filled.VisibilityOff

                                    val description =
                                        if (passwordRepeatVisible) "Hide password" else "Show password"

                                    IconButton(onClick = { passwordRepeatVisible = !passwordRepeatVisible }) {
                                        Icon(imageVector = image, description, tint = MaterialTheme.colors.secondary)
                                    }
                                }
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .padding(
                        top = 60.dp
                    )
            ) {
                Button(
                    onClick = { buttonClicked = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 60.dp)
                        .testTag("RegistrationScreenTwoButton"),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary),
                ) {
                    Text(
                        text = stringResource(id = R.string.button_continue),
                        color = MaterialTheme.colors.onSurface,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }

    fun notEmpty(): Boolean = email.trim().isNotEmpty() &&
            password.trim().isNotEmpty() && passwordRepeat.trim().isNotEmpty()

    fun matchPasswords(): Boolean = password == passwordRepeat
    fun checkEmail() = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    if (buttonClicked) {

        if (notEmpty()) {
            if (checkEmail()) {
                if (matchPasswords()) {
                    val pwRegex = """^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$""".toRegex()

                    if (pwRegex.matches(password)){
                        RegistrationUIAddress(studentMode, email, password, preferences)
                    } else {
                        buttonClicked = false
                        Toast.makeText(LocalContext.current, "Passwords does not have the needed complexity.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    buttonClicked = false
                    Toast.makeText(LocalContext.current, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                }
            } else {
                buttonClicked = false
                Toast.makeText(LocalContext.current, "E-Mail is not valid.", Toast.LENGTH_SHORT).show()
            }
        } else {
            buttonClicked = false
            Toast.makeText(LocalContext.current, "Please enter valid credentials.", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun RegistrationUIAddress(StudentMode: Boolean, Email:String, Password:String, preferences: SharedPreferences){

    var rootPoint by remember { mutableStateOf("") }
    var preferredLine by remember { mutableStateOf("") }
    var preferredDestination by remember { mutableStateOf("") }

    val context = LocalContext.current

    fun sendEmailVerification() {
        firebaseUser?.let {
            it.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Email sent to $Email", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun notEmpty(): Boolean = rootPoint.trim().isNotEmpty() &&
            preferredLine.trim().isNotEmpty() && preferredDestination.trim().isNotEmpty()

    Column(modifier = Modifier
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
                    horizontal = 32.dp,
                )
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Card(
                    backgroundColor = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(25.dp),
                    elevation = 10.dp
                ) {
                    Text(
                        modifier = Modifier
                            .padding(16.dp),
                        text = "Enter Address Data",
                        textAlign = TextAlign.Center,
                        fontSize = 27.sp,
                        color = MaterialTheme.colors.secondary
                    )
                }

            }

            Spacer(modifier = Modifier.padding(top = 50.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = rootPoint,
                    label = { Text(text = "Root Point", color = MaterialTheme.colors.secondary) },
                    onValueChange = { rootPoint = it },
                    singleLine = true,
                    modifier = Modifier
                        .height(height = 60.dp)
                        .fillMaxWidth()
                        .testTag("RootPoint"),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.primary
                    ),
                    textStyle = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        color = MaterialTheme.colors.surface,
                        fontSize = 18.sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    )
                )
            }

            Spacer(modifier = Modifier.padding(top = 50.dp))

            Spacer(modifier = Modifier.padding(top = 12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = preferredLine,
                    label = { Text(text = "Preferred Line", color = MaterialTheme.colors.secondary) },
                    onValueChange = { preferredLine = it },
                    singleLine = true,
                    modifier = Modifier
                        .height(height = 60.dp)
                        .fillMaxWidth()
                        .testTag("Lines"),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.primary
                    ),
                    textStyle = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        color = MaterialTheme.colors.surface,
                        fontSize = 18.sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    )
                )
            }

            Spacer(modifier = Modifier.padding(top = 50.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = preferredDestination,
                    label = { Text(text = "Destination", color = MaterialTheme.colors.secondary) },
                    onValueChange = { preferredDestination = it },
                    singleLine = true,
                    modifier = Modifier
                        .height(height = 60.dp)
                        .fillMaxWidth()
                        .testTag("Destination"),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.primary
                    ),
                    textStyle = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        color = MaterialTheme.colors.surface,
                        fontSize = 18.sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    )
                )
            }

            Row(
                modifier = Modifier
                    .padding(
                        top = 60.dp
                    )
            ) {
                Button(
                    onClick = {
                          if (notEmpty()) {
                              if(StudentMode) {
                                  preferences.edit().putString(GlobalVarHolder.TYPE, "Student").apply()
                              } else {
                                  preferences.edit().putString(GlobalVarHolder.TYPE, "Normal").apply()
                              }
                              preferences.edit().putString(GlobalVarHolder.PREFERREDLINE, preferredLine).apply()
                              preferences.edit().putString(GlobalVarHolder.ROOTPOINT, rootPoint).apply()
                              preferences.edit().putBoolean(GlobalVarHolder.TOBESAVED, true).apply()

                              firebaseAuth.createUserWithEmailAndPassword(Email, Password)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            task.result.user?.getIdToken(true)?.addOnSuccessListener { result ->
                                                GlobalVarHolder.userIdToken = result.token ?: ""
                                            }
                                            Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT).show()
                                            sendEmailVerification()
                                            val intent = Intent(context, AuthActivity::class.java)
                                            context.startActivity(intent)
                                        } else {
                                            Toast.makeText(context, "Athentication failed!", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                          } else {
                              Toast.makeText(context, "Please enter valid address data.", Toast.LENGTH_SHORT).show()
                          }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 60.dp)
                        .testTag("RegistrationFinishButton"),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                ) {
                    Text(
                        text = stringResource(id = R.string.button_continue),
                        color = MaterialTheme.colors.onSurface,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

