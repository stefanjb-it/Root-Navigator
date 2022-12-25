package at.fh.mappdev.rootnavigator

import androidx.compose.ui.graphics.painter.Painter

data class BarItem(
    val title: String,
    val image: Painter,
    val route: String
)