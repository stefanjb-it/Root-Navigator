package at.fh.mappdev.rootnavigator

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.fh.mappdev.rootnavigator.FirebaseUtils.firebaseAuth
import at.fh.mappdev.rootnavigator.FirebaseUtils.firebaseUser
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
        .background(MaterialTheme.colors.primary)
        .paint(
            painter = painterResource(R.drawable.threelines),
            contentScale = ContentScale.FillWidth
        )
    ){
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 32.dp,
                    vertical = 32.dp
                ),
            verticalArrangement = Arrangement.Center
        ) {

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(text = "Choose your mode",
                    textAlign = TextAlign.Center,
                    fontSize = 27.sp,
                    color = MaterialTheme.colors.surface)
            }

            Spacer(modifier = Modifier.padding(top = 80.dp))

            Row {
                Button(
                    onClick = {
                        studentMode = true
                        buttonClicked = true
                              },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                ) {
                    Text(
                        text = stringResource(id = R.string.button_student),
                        color = MaterialTheme.colors.surface,
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
                        .height(150.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant),
                ) {
                    Text(
                        text = stringResource(id = R.string.button_standard),
                        color = MaterialTheme.colors.surface,
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
                ),
            verticalArrangement = Arrangement.Center
        ) {

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    text = "Enter Account Data",
                    textAlign = TextAlign.Center,
                    fontSize = 27.sp,
                    color = MaterialTheme.colors.surface
                )
            }

            Spacer(modifier = Modifier.padding(top = 50.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "E-Mail",
                    textAlign = TextAlign.Start,
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.surface)
            }

            Spacer(modifier = Modifier.padding(top = 12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .height(height = 60.dp)
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.secondaryVariant
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

            Spacer(modifier = Modifier.padding(top = 36.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Password",
                    textAlign = TextAlign.Start,
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.surface)
            }

            Spacer(modifier = Modifier.padding(top = 12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 60.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.secondaryVariant
                    ),
                    singleLine = true,
                    textStyle = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        color = MaterialTheme.colors.surface,
                        fontSize = 18.sp
                    ),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        val description = if (passwordVisible) "Hide password" else "Show password"

                        IconButton(onClick = {passwordVisible = !passwordVisible}){
                            Icon(imageVector  = image, description)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.padding(top = 36.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Repeat Password",
                    textAlign = TextAlign.Start,
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.surface)
            }

            Spacer(modifier = Modifier.padding(top = 12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = passwordRepeat,
                    onValueChange = { passwordRepeat = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 60.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.secondaryVariant
                    ),
                    singleLine = true,
                    textStyle = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        color = MaterialTheme.colors.surface,
                        fontSize = 18.sp
                    ),
                    visualTransformation = if (passwordRepeatVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if (passwordRepeatVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        val description = if (passwordRepeatVisible) "Hide password" else "Show password"

                        IconButton(onClick = {passwordRepeatVisible = !passwordRepeatVisible}){
                            Icon(imageVector  = image, description)
                        }
                    }
                )
            }

            Row(
                modifier = Modifier
                    .padding(
                        top = 50.dp
                    )
            ) {
                Button(
                    onClick = { buttonClicked = true },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                ) {
                    Text(
                        text = stringResource(id = R.string.button_continue),
                        color = MaterialTheme.colors.surface,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }

    fun notEmpty(): Boolean = email.trim().isNotEmpty() &&
            password.trim().isNotEmpty() && passwordRepeat.trim().isNotEmpty()

    fun matchPasswords(): Boolean = password == passwordRepeat

    // ToDo check with Regex
    // fun checkEmail(): Boolean = email.contains("@")

    fun checkEmail() = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    if (buttonClicked) {

        if (notEmpty()) {
            if (checkEmail()) {
                if (matchPasswords()) {
                    RegistrationUIAddress(studentMode, email, password, preferences)
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
                    Toast.makeText(context, "email sent to $Email", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun notEmpty(): Boolean = rootPoint.trim().isNotEmpty() &&
            preferredLine.trim().isNotEmpty() && preferredDestination.trim().isNotEmpty()

    Column(modifier = Modifier
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
                ),
            verticalArrangement = Arrangement.Center
        ) {

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    text = "Enter Address Data",
                    textAlign = TextAlign.Center,
                    fontSize = 27.sp,
                    color = MaterialTheme.colors.surface
                )
            }

            Spacer(modifier = Modifier.padding(top = 50.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Root Point",
                    textAlign = TextAlign.Start,
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.surface)
            }

            Spacer(modifier = Modifier.padding(top = 12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = rootPoint,
                    onValueChange = { rootPoint = it },
                    modifier = Modifier
                        .height(height = 60.dp)
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.secondaryVariant
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
                Text(text = "Preferred Line",
                    textAlign = TextAlign.Start,
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.surface)
            }

            Spacer(modifier = Modifier.padding(top = 12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = preferredLine,
                    onValueChange = { preferredLine = it },
                    modifier = Modifier
                        .height(height = 60.dp)
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.secondaryVariant
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
                Text(text = "Preferred Destination",
                    textAlign = TextAlign.Start,
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.surface)
            }

            Spacer(modifier = Modifier.padding(top = 12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = preferredDestination,
                    onValueChange = { preferredDestination = it },
                    modifier = Modifier
                        .height(height = 60.dp)
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.secondaryVariant
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
                        top = 50.dp
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

                              firebaseAuth.createUserWithEmailAndPassword(Email, Password)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
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
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                ) {
                    Text(
                        text = stringResource(id = R.string.button_continue),
                        color = MaterialTheme.colors.surface,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

