package at.fh.mappdev.rootnavigator.hafas

import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
    val lat = 47.0727551
    //val long = 15.4420971
    val long = 15.4140822
    val stationsIdResponse : State<ResponseType?> = BackendHandler.getNearbyStations(currentLocation.value?.latitude ?: (-1).toDouble(), currentLocation.value?.longitude ?: (-1).toDouble(), 1000).observeAsState()//= BackendHandler.getNearbyStations(lat, long, 250).observeAsState()
    //var stationsIdResponse : State<ResponseType?> = BackendHandler.getNearbyStations(lat, long, 1000).observeAsState()
    val finalMap = BackendHandler.getStationMap().observeAsState()
    Log.v("OUT", GlobalVarHolder.userIdToken)
    //Log.e("StationsList", stationsIdResponse.value?.content.toString())

    when (stationsIdResponse.value?.done) {
        true -> {
            Log.v("IN", GlobalVarHolder.userIdToken)
            //Log.e("finalMap", stationsIdResponse.value?.content?.map { it.name }.toString())
            //Log.e("StationsList", stationsIdResponse.value?.content?.size.toString())
            BackendHandler.loadStationDetails(stationsIdResponse.value?.content ?: listOf())
            Toast.makeText(LocalContext.current, "Your data is almost here!", Toast.LENGTH_LONG).show()
        }
        else -> Toast.makeText(
            LocalContext.current,
            "Data is being retrieved...",
            Toast.LENGTH_SHORT
        ).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            //.background(MaterialTheme.colors.primary)
            .background(MaterialTheme.colors.background)
            /*.paint(
                painter = painterResource(R.drawable.threelines),
                contentScale = ContentScale.FillWidth
            )*/
            .padding(
                start = 16.dp,
                end = 16.dp,
            ),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn()
        {
            if (finalMap.value != null) {
                //Log.v("finalMap", finalMap.value?.map { it.value.station.name }.toString())
                //Log.v("finalMap", finalMap.value?.size.toString())
                finalMap.value?.forEach {
                    item {
                        Connection(station = it.value, preferences)
                    }
                }
            } else {
                item {

                }
            }

        }
    }
}