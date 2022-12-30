package at.fh.mappdev.rootnavigator

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.fh.mappdev.rootnavigator.NewReminderActivity.startActivity
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme
import java.util.*
import kotlin.concurrent.timerTask

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RootNavigatorTheme {
                Surface(color = MaterialTheme.colors.primary) {
                    AuthUI()
                }
            }
        }

        val timer = Timer()
        // If authentication passes
        val intent = Intent(this, HomeActivity::class.java)
        // else: go to Session Expired
        // val intent = Intent(this, SessionExpiredActivity::class.java)
        timer.schedule(timerTask { startActivity(intent) }, 0)
    }
}

@Preview
@Composable
fun AuthUI(){

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.primary)
        .paint(
            painter = painterResource(R.drawable.threelines),
            contentScale = ContentScale.FillWidth
        )
    ){ Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
        ) {
        CircularProgressIndicator(
            modifier = Modifier
                .height(height = 250.dp)
                .width(width = 250.dp),
            color = MaterialTheme.colors.secondary,
            strokeWidth = 25.dp
        )

        Spacer(modifier = Modifier.padding(top = 50.dp))

        Text(text = "Loading\nProfile",
            color = MaterialTheme.colors.surface,
            textAlign = TextAlign.Center,
            fontSize = 48.sp)
        }
    }
}