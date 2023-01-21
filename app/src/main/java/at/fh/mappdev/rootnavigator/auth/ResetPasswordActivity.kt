package at.fh.mappdev.rootnavigator.auth

import android.content.Intent
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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme
import at.fh.mappdev.rootnavigator.FirebaseUtils.firebaseAuth
import at.fh.mappdev.rootnavigator.R
import at.fh.mappdev.rootnavigator.reminder.NewReminderActivity.startActivity

class ResetPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RootNavigatorTheme {
                ResetPasswordUI()
            }
        }
    }
}

@Composable
fun ResetPasswordUI(){
    val context = LocalContext.current
    val login = Intent(context, LoginActivity::class.java)
    var email by remember { mutableStateOf("") }
    fun notEmpty(): Boolean = email.trim().isNotEmpty()

    Column(modifier = Modifier
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
                        modifier = Modifier.padding(20.dp),
                        text = "Reset Password",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.secondary,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 27.sp
                        )

                    )
                }
            }

            Spacer(modifier = Modifier.padding(top = 50.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = email,
                    label = { Text(text = "E-Mail", color = MaterialTheme.colors.secondary) },
                    onValueChange = { email = it },
                    singleLine = true,
                    modifier = Modifier
                        .height(height = 60.dp)
                        .fillMaxWidth(),
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
        }

        Column(
            modifier = Modifier
                .padding(
                    start = 32.dp,
                    end = 32.dp,
                    bottom = 80.dp
                )
        ) {
            Button(
                onClick = {
                    if(notEmpty()) {
                        firebaseAuth.sendPasswordResetEmail(email)
                            .addOnSuccessListener {
                                Toast.makeText(context, "E-Mail was sent successfully.", Toast.LENGTH_SHORT).show()
                                startActivity(login)
                            }.addOnFailureListener {
                                Toast.makeText(context, "E-Mail could not be sent!", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(context, "Please enter your E-Mail address.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 60.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
            ) {
                Text(
                    text = stringResource(id = R.string.button_submit),
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 18.sp
                )
            }
        }
    }
}