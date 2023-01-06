package at.fh.mappdev.rootnavigator.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = DarkGray,
    primaryVariant = LightGray,
    secondary = Orange,
    surface = TextWhite,
    onSurface = StripeGray,
    secondaryVariant = TextWhiteTrans
)

private val LightColorPalette = lightColors(
    primary = DarkGray,
    primaryVariant = TextWhite,
    secondary = Orange,
    surface = LightGray,
    onSurface = DarkGray,
    secondaryVariant = TextWhiteTrans
    // surface = TextWhite

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun RootNavigatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}