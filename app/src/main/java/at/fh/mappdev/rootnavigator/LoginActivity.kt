package at.fh.mappdev.rootnavigator

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import at.fh.mappdev.rootnavigator.FirebaseUtils.firebaseAuth
import at.fh.mappdev.rootnavigator.database.GlobalVarHolder
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme
import com.google.firebase.auth.FirebaseUser

class LoginActivity : ComponentActivity(), LocationListener {
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPrefs = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        setContent {
            RootNavigatorTheme {
                Surface(color = MaterialTheme.colors.primary) {
                    LoginUI(sharedPrefs)

                }
            }
        }

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        } else {
            getLocation()
        }
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 2)
        }
        // ToDo change minTime and mindDistance
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1f, this)
    }

    override fun onLocationChanged(location: Location) {
        GlobalVarHolder.location.value = location
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
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
                val intent = Intent(this, AuthActivity::class.java)
                this.startActivity(intent)
            }
        }
    }
}

@Composable
fun LoginUI(preferences: SharedPreferences){
    var email by remember { mutableStateOf("") }
    var password by remember{ mutableStateOf("")}
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var stayLoggedIn by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current

    fun notEmpty(): Boolean = email.trim().isNotEmpty() &&
            password.trim().isNotEmpty()

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
                )
        ) {

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
               Text(text = "Sign In",
                   textAlign = TextAlign.Center,
                   color = MaterialTheme.colors.surface,
                   fontSize = 27.sp)
            }

            Spacer(modifier = Modifier.padding(top = 64.dp))

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

            Spacer(modifier = Modifier.padding(top = 76.dp))

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
                        uncheckedColor = MaterialTheme.colors.secondaryVariant
                    ),
                )
            }

            Spacer(modifier = Modifier.padding(top = 36.dp))

            Row(modifier = Modifier.fillMaxWidth()){
                Button(onClick = {
                    preferences.edit().putBoolean(GlobalVarHolder.STAYLOGGEDIN, stayLoggedIn).apply()

                    if (notEmpty()) {
                        firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { signIn ->
                                if (signIn.isSuccessful) {
                                    Toast.makeText(context, "Signed in successfully!", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(context, AuthActivity::class.java)
                                    context.startActivity(intent)
                                } else {
                                    Toast.makeText(context, "Sign in failed!", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(context, "Please enter your user credentials.", Toast.LENGTH_SHORT).show()
                    }
                },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 60.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                ) {
                    Text(
                        text = stringResource(id = R.string.button_sign_in),
                        color = MaterialTheme.colors.surface,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.padding(top = 76.dp))

            Row(modifier = Modifier.fillMaxWidth(),) {
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