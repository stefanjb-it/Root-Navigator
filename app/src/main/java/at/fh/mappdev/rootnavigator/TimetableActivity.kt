package at.fh.mappdev.rootnavigator

import android.os.Bundle
import android.util.Log
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

                }
            }
        }
    }
}

@Composable
fun TimetableUI(){
    val baseUrl = "https://cur.tk-dev.at/"
    val cookieString2 = "jwt=IXcCHqRYaZ0LoyDU0RJ52FjfTSEwesMdJ7dEzNNE3gc2AbeMDGYD68vkm1iCaLGKs+kDdRf/ub1yg3N4SoMzyPFZkoP5CLTWTN+p4TqMn7guaomApH434U+EbVoJGzt98t++OI380dikuq1Dg28+SM5usymdIBiKEFsdADajpVeCvua5B6rmuC6Hem7e/FZoD7FMPUbidUQoh21A"
    val cm = CookieManager.getInstance()
    cm.removeAllCookies(null)
    cm.acceptCookie()
    cm.setCookie(baseUrl, cookieString2)
    cm.flush()
    Log.e("Cookie", cm.getCookie(baseUrl))

    Column(modifier = Modifier
        .fillMaxSize()
    ) {
        AndroidView(factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = WebViewClient()
                loadUrl(baseUrl)
                settings.javaScriptEnabled = true
            }
        }, update = {
            it.loadUrl(baseUrl)
        })
    }
}