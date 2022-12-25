package at.fh.mappdev.rootnavigator

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme

class SessionExpiredActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RootNavigatorTheme {
                Surface(color = MaterialTheme.colors.background) {
                    SessionExpiredUI()
                }
            }
        }
    }
}

@Preview
@Composable
fun SessionExpiredUI(){
    Column(modifier = Modifier.padding(top = 250.dp, start = 20.dp, end = 20.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Your Offline Session has expired!",
        textAlign = TextAlign.Center,
        fontSize = 48.sp,
        )

        Button(
            onClick = { /*TODO Implement:Back to Login Screen*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 250.dp),

            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.orange)),
        ) {
            Text(text = "Back to Login", color = Color.White, fontSize = 18.sp)
        }
    }
}