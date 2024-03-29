package at.fh.mappdev.rootnavigator.auth

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import at.fh.mappdev.rootnavigator.*
import at.fh.mappdev.rootnavigator.FirebaseUtils.firebaseAuth
import at.fh.mappdev.rootnavigator.R
import at.fh.mappdev.rootnavigator.database.GlobalVarHolder
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseUser

// central starting activity
class LoginActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationPermissionCode = 999

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPrefs = getSharedPreferences(packageName, Context.MODE_PRIVATE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(NotificationInfo.NOTIFICATIONID, NotificationInfo.NOTIFICATIONNAME, NotificationManager.IMPORTANCE_HIGH)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)

            val alarmChannel = NotificationChannel(NotificationInfo.ALARMID, NotificationInfo.ALARMNAME, NotificationManager.IMPORTANCE_HIGH)
            val alarmManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            alarmManager.createNotificationChannel(alarmChannel)
        }

        setContent {
            RootNavigatorTheme {
                Surface(color = MaterialTheme.colors.background) {
                    LoginUI(sharedPrefs)
                    askPermission()
                }
            }
        }
    }

    fun askPermission(){
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        } else {

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    GlobalVarHolder.location.value = location
                }
        }
    }

    override fun onStart() {
        super.onStart()
        val sharedPrefs = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        if (sharedPrefs.getBoolean(GlobalVarHolder.STAYLOGGEDIN, false)){
            val user: FirebaseUser? = firebaseAuth.currentUser
            user?.let {
                Toast.makeText(this, "Signed in successfully!", Toast.LENGTH_SHORT).show()
                user.getIdToken(true).addOnSuccessListener { result ->
                    GlobalVarHolder.userIdToken = result.token ?: ""
                }
                val intent = Intent(this, AuthActivity::class.java)
                this.startActivity(intent)
            }
        }
    }
}

// UI for Login Process
@Composable
fun LoginUI(preferences: SharedPreferences){
    var email by remember { mutableStateOf("") }
    var password by remember{ mutableStateOf("")}
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var stayLoggedIn by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current

    fun notEmpty(): Boolean = email.trim().isNotEmpty() && password.trim().isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .paint(
                painter = painterResource(if (!isSystemInDarkTheme()) R.drawable.threelines_new_light else R.drawable.threelines_new),
                contentScale = ContentScale.FillWidth
            ),
        verticalArrangement = Arrangement.Center
    ){
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 32.dp,
                )
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Sign In",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.secondary,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 27.sp
                    )
                )
            }

            Spacer(modifier = Modifier.padding(top = 48.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "E-Mail",
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colors.surface,
                    fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.padding(top = 12.dp))

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
                    color = MaterialTheme.colors.surface,
                    fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.padding(top = 12.dp))

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
                            Icon(imageVector  = image, description, tint = MaterialTheme.colors.secondary)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.padding(top = 64.dp))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Stay Logged In?",
                    textAlign=TextAlign.Start,
                    color = MaterialTheme.colors.surface
                )

                Spacer(modifier = Modifier.weight(1f))

                Checkbox(
                    checked = stayLoggedIn,
                    onCheckedChange = {stayLoggedIn = !stayLoggedIn},
                    colors = CheckboxDefaults.colors(
                        uncheckedColor = MaterialTheme.colors.secondary,
                        checkedColor = MaterialTheme.colors.secondary
                    ),
                )
            }

            Spacer(modifier = Modifier.padding(top = 36.dp))

            Row(modifier = Modifier.fillMaxWidth()){
                Button(
                    onClick = {
                        preferences.edit().putBoolean(GlobalVarHolder.STAYLOGGEDIN, stayLoggedIn)
                            .apply()

                        if (notEmpty()) {
                            firebaseAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { signIn ->
                                    if (signIn.isSuccessful) {
                                        signIn.result.user?.getIdToken(true)?.addOnSuccessListener { result ->
                                            GlobalVarHolder.userIdToken = result.token ?: ""
                                        }
                                        Toast.makeText(
                                            context,
                                            "Signed in successfully!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        val intent = Intent(context, AuthActivity::class.java)
                                        context.startActivity(intent)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Sign in failed!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        } else {
                            Toast.makeText(
                                context,
                                "Please enter your user credentials.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 60.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary
                    ),
                ) {
                    Text(
                        text = stringResource(id = R.string.button_sign_in),
                        color = MaterialTheme.colors.onSurface,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.padding(top = 76.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                ClickableText(text = AnnotatedString("Forgot Password?"),
                    onClick = {
                        val intent = Intent(context, ResetPasswordActivity::class.java)
                        context.startActivity(intent)
                    },
                    style = TextStyle(
                        color = MaterialTheme.colors.surface,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Start
                    )
                )

                Spacer(modifier = Modifier.weight(1f))

                ClickableText(text = AnnotatedString("Sign Up"),
                    modifier = Modifier.testTag("SignUp"),
                    onClick = {
                        val intent = Intent(context, RegistrationActivity::class.java)
                        context.startActivity(intent)
                    },
                    style = TextStyle(
                        color = MaterialTheme.colors.surface,
                        fontSize = 18.sp,
                        textAlign = TextAlign.End
                    )
                )
            }
        }
    }
}