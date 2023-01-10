package at.fh.mappdev.rootnavigator

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme
import com.google.firebase.auth.FirebaseAuth

class SessionExpiredActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RootNavigatorTheme {
                SessionExpiredUI()
            }
        }
    }
}

@Preview
@Composable
fun SessionExpiredUI(){
    val context = LocalContext.current

    if (FirebaseAuth.getInstance().currentUser != null){
        FirebaseAuth.getInstance().signOut()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your Offline Session has expired!",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.surface,
            fontSize = 48.sp,
        )

        Spacer(modifier = Modifier.padding(bottom = 100.dp))

        Button(
            onClick = {
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 0.dp,
                    start = 32.dp,
                    end = 32.dp
                ),

            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.secondary
            ),
        ) {
            Text(
                text = stringResource(id = R.string.button_back),
                color = MaterialTheme.colors.surface,
                fontSize = 18.sp
            )
        }
    }
}