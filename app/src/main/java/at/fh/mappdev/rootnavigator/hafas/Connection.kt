package at.fh.mappdev.rootnavigator.hafas

import android.content.SharedPreferences
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.fh.mappdev.rootnavigator.database.SafeStationDetails

@Composable
fun Connection(station: SafeStationDetails, preferences: SharedPreferences) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp),
        backgroundColor = MaterialTheme.colors.primary,
        shape = RoundedCornerShape(25.dp)
    ) {
        CardContent(station, preferences)
    }
}