package at.fh.mappdev.rootnavigator.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.location.Location

object BackendHandler {
    private val stationMap : MutableLiveData<MutableMap<Int, SafeStationDetails>> = MutableLiveData(
        mutableMapOf()
    )

    private val safeNewMap : SafeSwitchMap = SafeSwitchMap(0, mutableMapOf())

    private val switchMaps = {
        stationMap.value = safeNewMap.map.toMutableMap()
        finishedActionCount = 0
    }

    private var finishedActionCount :Int = 0
    private fun lowerActionCount(allowedId:Int){
        if (safeNewMap.id != allowedId) return
        finishedActionCount--
        if(finishedActionCount == 0){
            switchMaps()
        }
    }

    private fun stationToMap(newStation:Station, allowedId:Int){
        if (safeNewMap.id != allowedId) return
        safeNewMap.map[newStation.id] = SafeStationDetails(newStation, mutableListOf(), mutableListOf())
        lowerActionCount(allowedId)
    }

    private fun departureToMap(id:Int, dp : List<Departure>, allowedId:Int){
        if (safeNewMap.id != allowedId) return
        safeNewMap.map[id]?.departures?.addAll(dp)
        lowerActionCount(allowedId)
    }

    private fun arrivalToMap(id:Int, ar : List<Arrival>, allowedId: Int){
        if (safeNewMap.id != allowedId) return
        safeNewMap.map[id]?.arrival?.addAll(ar)
        lowerActionCount(allowedId)
    }

    // PUBLIC ACCESS
    fun getStationMap() : LiveData<MutableMap<Int, SafeStationDetails>> {
        return stationMap
    }

    // get nearby stations
    fun getNearbyStations(latitude:Double, longitude:Double, distance:Int):LiveData<ResponseType>{
        Log.i("API getNearbyStations", "$latitude $longitude $distance")
        if (latitude < 0 || longitude < 0 || distance < 0){
            return MutableLiveData(ResponseType(false, null))
        }
        val returnData : MutableLiveData<ResponseType> = MutableLiveData<ResponseType>()
        Backend().retrofitService.getNearbyStations(GlobalVarHolder.uidPrefix+GlobalVarHolder.userIdToken, latitude, longitude, distance).enqueue(object : Callback<List<Station>>{
            override fun onResponse(
                call: Call<List<Station>>,
                response: Response<List<Station>>
            ) {
                println(response.body())
                returnData.value = ResponseType(true, response.body())
            }

            override fun onFailure(call: Call<List<Station>>, t: Throwable) {
                println("Cannot get location")
            }
        })
        return returnData
    }

    // add departures of station to map
    private fun getDepartures(stationId : Int, allowedId:Int){
        Backend().retrofitService.getStationDeparture(
            GlobalVarHolder.uidPrefix+GlobalVarHolder.userIdToken,
            stationId,
            GlobalVarHolder.requestTime
        ).enqueue(object :
            Callback<List<Departure>> {
            override fun onResponse(
                call: Call<List<Departure>>,
                response: Response<List<Departure>>
            ) {
                Log.v("API Departures onResponse", response.body().toString())
                departureToMap(stationId, response.body() ?: return, allowedId)
            }

            override fun onFailure(call: Call<List<Departure>>, t: Throwable) {
                Log.e("API Departures onFailure $stationId", t.toString())
                lowerActionCount(allowedId)
            }
        }
        )
    }

    // add arrivals of station to map
    private fun getArrivals(stationId : Int, allowedId:Int){
        Backend().retrofitService.getStationArrival(
            GlobalVarHolder.uidPrefix+GlobalVarHolder.userIdToken,
            stationId,
            GlobalVarHolder.requestTime
        ).enqueue(object :
            Callback<List<Arrival>> {
            override fun onResponse(
                call: Call<List<Arrival>>,
                response: Response<List<Arrival>>
            ) {
                Log.v("API Arrivals onResponse", response.body().toString())
                arrivalToMap(stationId, response.body() ?: return, allowedId)
            }

            override fun onFailure(call: Call<List<Arrival>>, t: Throwable) {
                Log.e("API Arrivals onFailure $stationId", t.toString())
                lowerActionCount(allowedId)
            }
        }
        )
    }

    // handle all requests for station details
    fun loadStationDetails(stations : List<Station>){
        safeNewMap.map = mutableMapOf()
        safeNewMap.id++
        val allowedId = safeNewMap.id
        finishedActionCount = 3 * stations.size
        stations.forEach {
            stationToMap(it, allowedId)
            getDepartures(it.id, allowedId)
            getArrivals(it.id, allowedId)
        }
        GlobalScope.launch {
            // wait for all requests to finish
            delay(1000)
            Log.i("API", getStationMap().value.toString())
        }
    }
}