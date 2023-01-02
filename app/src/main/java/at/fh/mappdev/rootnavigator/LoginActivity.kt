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
import at.fh.mappdev.rootnavigator.FirebaseUtils.firebaseAuth
import at.fh.mappdev.rootnavigator.database.PrefHolder
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme
import com.google.firebase.auth.FirebaseUser

class LoginActivity : ComponentActivity() {
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
    }

    override fun onStart() {
        super.onStart()
        val sharedPrefs = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        if (sharedPrefs.getBoolean(PrefHolder.STAYLOGGEDIN, false)){
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
                    preferences.edit().putBoolean(PrefHolder.STAYLOGGEDIN, stayLoggedIn).apply()

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
                    onClick = {/*TODO Implement method*/},
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