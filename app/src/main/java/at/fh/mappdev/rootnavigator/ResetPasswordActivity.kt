package at.fh.mappdev.rootnavigator

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme
import at.fh.mappdev.rootnavigator.FirebaseUtils.firebaseAuth

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
    var email by remember { mutableStateOf("") }
    fun notEmpty(): Boolean = email.trim().isNotEmpty()

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
                )
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    text = "Reset Password",
                    textAlign = TextAlign.Center,
                    fontSize = 27.sp,
                    color = MaterialTheme.colors.surface
                )
            }

            Spacer(modifier = Modifier.padding(top = 50.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Please enter your E-Mail address",
                    textAlign = TextAlign.Start,
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.surface)
            }

            Spacer(modifier = Modifier.padding(top = 12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    singleLine = true,
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
        }

        Column(
            modifier = Modifier
                .padding(
                    start = 32.dp,
                    end = 32.dp,
                    bottom = 32.dp
                )
        ) {
            Button(
                onClick = {
                    if(notEmpty()) {
                        firebaseAuth.sendPasswordResetEmail(email)
                            .addOnSuccessListener {
                                Toast.makeText(context, "E-Mail was sent successfully.", Toast.LENGTH_SHORT).show()
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
                    color = MaterialTheme.colors.surface,
                    fontSize = 18.sp
                )
            }
        }
    }
}