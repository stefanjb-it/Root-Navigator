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
    surface = TextWhite, // NAVIGATOR, Settings-Info, Home Con-Time
    onSurface = TextWhite,
    secondaryVariant = TextWhiteTrans,
    background = StripeGray // = old onSurface
)

/*
    primary             Boxes / Components
    primaryVariant      Contrast to Boxes / Components in those
    secondary           Accent Color (Orange)
    secondaryVariant    ---
    surface             primary Font color
    onSurface           button Font color
    background          Background
 */

private val LightColorPalette = lightColors(
    primary = TextWhite,
    primaryVariant = LightGray,
    secondary = Orange,
    surface = DarkGray,
    onSurface = TextWhite,
    secondaryVariant = TestGreen,
    background = FullWhite, // = old onSurface

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