package at.fh.mappdev.rootnavigator.hafas

import android.content.SharedPreferences
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.fh.mappdev.rootnavigator.R
import at.fh.mappdev.rootnavigator.database.GlobalVarHolder
import at.fh.mappdev.rootnavigator.database.SafeStationDetails
import at.fh.mappdev.rootnavigator.home.firstPrefLine

@Composable
fun CardContent(station: SafeStationDetails, preferences: SharedPreferences) {
    var expanded by remember { mutableStateOf(false) }
    val infoList: List<String> = firstPrefLine(station.departures, preferences)

    val sharedLines = preferences.getString(GlobalVarHolder.PREFERREDLINE, "")
    val prefLines : MutableList<String> = mutableListOf()

    if (sharedLines?.contains(",") == true){
        val lines = sharedLines.split(",")
        for (line in lines){
            prefLines.add(line.trim())
        }
    } else {
        prefLines.add(sharedLines.toString())
    }

    Column(
        modifier = Modifier
            .padding(
                start = 4.dp,
                end = 4.dp,
                top = 8.dp,
                bottom = 8.dp
            )
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            when(infoList[0]) {
                "suburban" -> Icon(
                    imageVector = Icons.Filled.Train,
                    tint = if (infoList[2].toBoolean()) { MaterialTheme.colors.secondary } else { MaterialTheme.colors.surface },
                    contentDescription = "route type",
                    modifier = Modifier
                        .weight(2f)
                )
                "bus" -> Icon(
                    imageVector = Icons.Filled.DirectionsBus,
                    tint = if (infoList[2].toBoolean()) { MaterialTheme.colors.secondary } else { MaterialTheme.colors.surface },
                    contentDescription = "route type",
                    modifier = Modifier
                        .weight(2f)
                )
                else -> Icon(
                    imageVector = Icons.Filled.Tram,
                    tint = if (infoList[2].toBoolean()) { MaterialTheme.colors.secondary } else { MaterialTheme.colors.surface },
                    contentDescription = "route type",
                    modifier = Modifier
                        .weight(2f)
                )
            }

            Text(
                text = station.station.name,
                color = MaterialTheme.colors.surface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(8f)
            )
            Row(modifier = Modifier
                .weight(3f),
            ) {
                Text(
                    text = infoList[1],
                    color = MaterialTheme.colors.surface,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = " min",
                    color = MaterialTheme.colors.surface,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    tint = MaterialTheme.colors.secondary,
                    contentDescription = if (expanded) {
                        stringResource(id = R.string.show_less)
                    } else {
                        stringResource(id = R.string.show_more)
                    },
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
        if (expanded) {
            Column(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Line",
                        color = MaterialTheme.colors.primaryVariant,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(1.5f)
                    )
                    Text(
                        text = "Dep.",
                        color = MaterialTheme.colors.primaryVariant,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(1.25f)
                    )
                    Text(
                        text = "Direction",
                        color = MaterialTheme.colors.primaryVariant,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(5f)
                    )
                    Text(
                        text = "Delay",
                        color = MaterialTheme.colors.primaryVariant,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(1.25f)
                    )
                }

                Spacer(modifier = Modifier.padding(bottom = 8.dp))

                for (depature in station.departures) {

                    var isPref = false
                    for (prefLine in prefLines){
                        if (depature.line.name.contains(prefLine)) {
                            isPref = true
                            break
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = depature.line.name,
                            color = if (isPref) {
                                MaterialTheme.colors.secondary} else { MaterialTheme.colors.primaryVariant},
                            fontSize = 14.sp,
                            modifier = Modifier
                                .weight(1.5f)
                        )
                        Text(
                            text = depature.whenThere.substring(11, 16),
                            color = if (isPref) {
                                MaterialTheme.colors.surface} else { MaterialTheme.colors.primaryVariant},
                            fontSize = 14.sp,
                            modifier = Modifier
                                .weight(1.25f)
                        )
                        Text(
                            text = depature.direction,
                            color = if (isPref) {
                                MaterialTheme.colors.surface} else { MaterialTheme.colors.primaryVariant},
                            fontSize = 14.sp,
                            modifier = Modifier
                                .weight(5f)
                        )

                        Text(
                            modifier = Modifier
                                .weight(1.25f),
                            text = if (depature.delay == null) { 0 } else { depature.delay.toInt().div(60) }.toString() + " min",
                            color = if (isPref) {
                                MaterialTheme.colors.surface} else { MaterialTheme.colors.primaryVariant},
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}