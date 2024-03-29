package at.fh.mappdev.rootnavigator.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.fh.mappdev.rootnavigator.R
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

@Composable
fun SessionExpiredUI(){
    val context = LocalContext.current

    if (FirebaseAuth.getInstance().currentUser != null){
        FirebaseAuth.getInstance().signOut()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(
                horizontal = 32.dp,
                vertical = 60.dp
            )
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Your Offline Session\nhas expired!",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.surface,
                fontSize = 48.sp,
            )
        }

        Button(
            onClick = {
                val intent = Intent(context, LoginActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 60.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.secondary
            ),
        ) {
            Text(
                text = stringResource(id = R.string.button_back),
                color = MaterialTheme.colors.onSurface,
                fontSize = 18.sp
            )
        }
    }
}