package at.fh.mappdev.rootnavigator.hafas

import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import at.fh.mappdev.rootnavigator.R
import at.fh.mappdev.rootnavigator.database.BackendHandler
import at.fh.mappdev.rootnavigator.database.GlobalVarHolder
import at.fh.mappdev.rootnavigator.database.ResponseType

@Composable
fun Connections(preferences: SharedPreferences, bottomBarState: MutableState<Boolean>, topBarState: MutableState<Boolean>) {
    bottomBarState.value = true
    topBarState.value = true
    var currentLocation = GlobalVarHolder.location.observeAsState()
    //val lat = 47.0672718
    val latHbf = 47.0727551
    //val long = 15.4420971
    val longHbf = 15.4140822
    val stationsIdResponse : State<ResponseType?> = BackendHandler.getNearbyStations(currentLocation.value?.latitude ?: (-1).toDouble(), currentLocation.value?.longitude ?: (-1).toDouble(), 1000).observeAsState()//= BackendHandler.getNearbyStations(lat, long, 250).observeAsState()
    // var stationsIdResponse : State<ResponseType?> = BackendHandler.getNearbyStations(latHbf, longHbf, 1000).observeAsState()
    val finalMap = BackendHandler.getStationMap().observeAsState()

    when (stationsIdResponse.value?.done) {
        true -> {
            BackendHandler.loadStationDetails(stationsIdResponse.value?.content ?: listOf())
            Toast.makeText(LocalContext.current, "Your data is almost here!", Toast.LENGTH_LONG).show()
        }
        else -> Log.i("Data", "Data is being retrieved")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .paint(
                painter = painterResource(if (!isSystemInDarkTheme()) R.drawable.threelines_new_light else R.drawable.threelines_new),
                contentScale = ContentScale.FillWidth
            )
            .padding(
                start = 16.dp,
                end = 16.dp,
            ),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxHeight()
        )
        {
            if (finalMap.value != null) {
                finalMap.value?.forEach {
                    item {
                        Connection(station = it.value, preferences)
                    }
                }
            } else {
                item {}
            }

        }
    }
}