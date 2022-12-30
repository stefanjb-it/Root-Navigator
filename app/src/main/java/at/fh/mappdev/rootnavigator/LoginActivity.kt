package at.fh.mappdev.rootnavigator

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RootNavigatorTheme {
                Surface(color = MaterialTheme.colors.primary) {
                    LoginUI()
                }
            }
        }
    }
}

@Composable
fun LoginUI(){
    var username by remember { mutableStateOf("") }
    var password by remember{ mutableStateOf("")}
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var stayLoggedIn by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current

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
                Text(text = "Username",
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colors.surface,
                    fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.padding(top = 12.dp))
            
            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = username,
                    onValueChange = { username = it },
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
                    val intent = Intent(context, AuthActivity::class.java)
                    context.startActivity(intent)
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