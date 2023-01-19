package at.fh.mappdev.rootnavigator

import android.content.SharedPreferences
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView
import at.fh.mappdev.rootnavigator.database.GlobalVarHolder
import at.fh.mappdev.rootnavigator.ui.theme.RootNavigatorTheme

/*class TimetableActivity  : ComponentActivity() {
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
}*/

@Composable
fun TimetableUI(preferences: SharedPreferences){
    val baseUrl = "https://cur.tk-dev.at/"
    //val cookieString = "config=programme:${preferences.getString(GlobalVarHolder.PROGRAMME, "ima21")}-group:${preferences.getString(GlobalVarHolder.GROUP, "G1")}-view:false-VO:#00a800-G1:#ff5c00-G2:#004eeb-G3:#FF00FF-PR:#FF0000;jwt=${GlobalVarHolder.userIdToken}; path=/"
    val cookieString2 = "jwt=IXcCHqRYaZ0LoyDU0RJ52FjfTSEwesMdJ7dEzNNE3gc2AbeMDGYD68vkm1iCaLGKs+kDdRf/ub1yg3N4SoMzyPFZkoP5CLTWTN+p4TqMn7guaomApH434U+EbVoJGzt98t++OI380dikuq1Dg28+SM5usymdIBiKEFsdADajpVeCvua5B6rmuC6Hem7e/FZoD7FMPUbidUQoh21A"
    val cm = CookieManager.getInstance()//.setCookie(baseUrl, cookieString)
    cm.removeAllCookies(null)
    cm.acceptCookie()
    //cm.setCookie(baseUrl, cookieString)
    cm.setCookie(baseUrl, cookieString2)
    cm.flush()
    Log.e("Cookie", cm.getCookie(baseUrl))

    Column(modifier = Modifier
        .fillMaxSize()
        //.testTag("TimetableTag")
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
                // addJavascriptInterface()
            }
        }, update = {
            it.loadUrl(baseUrl)
        })
    }
}