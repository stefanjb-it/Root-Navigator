package at.fh.mappdev.rootnavigator.bars

import androidx.compose.ui.graphics.vector.ImageVector

data class BarItem(
    val title: String,
    val image: ImageVector,
    val route: String
)