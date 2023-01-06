package at.fh.mappdev.rootnavigator

import android.os.Bundle
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme

class TimetableActivity  : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RootNavigatorTheme {
                Surface(color = MaterialTheme.colors.primary) {
                    TimetableUI()
                }
            }
        }
    }
}

@Composable
fun TimetableUI(){
    val baseUrl = "https://cur.tk-dev.at/"
    // ToDo: Add Cookie URL
    // val cookieString = "cookie_name=cookie_value; path=/"
    // CookieManager.getInstance().setCookie(baseUrl, cookieString)

    Column(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = WebViewClient()
                loadUrl(baseUrl)
                settings.javaScriptEnabled = true
                // addJavascriptInterface()
            }
        }, update = {
            it.loadUrl(baseUrl)
        })
    }
}