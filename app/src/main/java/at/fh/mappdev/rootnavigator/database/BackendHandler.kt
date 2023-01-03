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

@Deprecated("only present for testing")
object NewBEHandler {
    private val stationMap : MutableLiveData<UIReturnType> = MutableLiveData(
        UIReturnType(
            mutableMapOf())
    )

    private fun stationToMap(newStation:Station){
        stationMap.value?.contentMap?.put(newStation.id, SafeStationDetails(newStation, mutableListOf(), mutableListOf()))
    }

    private fun departureToMap(id:Int, dp : List<Departure>){
        stationMap.value?.contentMap?.get(id)?.departures?.addAll(dp)
        stationMap.value = stationMap.value
    }

    private fun arrivalToMap(id:Int, ar : List<Arrival>){
        stationMap.value?.contentMap?.get(id)?.arrival?.addAll(ar)
        stationMap.value = stationMap.value
    }

    // PUBLIC ACCESS
    public fun getStationMap() : LiveData<UIReturnType> {
        return stationMap
    }

    // get nearby stations
    fun getNearbyStations(latitude:Double, longitude:Double, distance:Int): LiveData<ResponseType> {
        val returnData : MutableLiveData<ResponseType> = MutableLiveData<ResponseType>()
        Backend().retrofitService.getNearbyStations(latitude, longitude, distance).enqueue(object :
            Callback<List<Station>> {
            override fun onResponse(
                call: Call<List<Station>>,
                response: Response<List<Station>>
            ) {
                Log.v("API Nearby onResponse", response.body().toString())
                returnData.value = ResponseType(true, response.body())
                response.body()?.forEach {
                    stationToMap(it)
                }
            }

            override fun onFailure(call: Call<List<Station>>, t: Throwable) {
                Log.e("API Nearby onFailure", t.toString())
            }
        })
        return returnData
    }

    // add departures of station to map
    private fun getDepartures(stationId : Int){
        Backend().retrofitService.getStationDeparture(
            stationId
        ).enqueue(object :
            Callback<List<Departure>> {
            override fun onResponse(
                call: Call<List<Departure>>,
                response: Response<List<Departure>>
            ) {
                Log.v("API Departures onResponse", response.body().toString())
                departureToMap(stationId, response.body() ?: return)
            }

            override fun onFailure(call: Call<List<Departure>>, t: Throwable) {
                Log.e("API Departures onFailure", t.toString())
            }
        }
        )
    }

    // add arrivals of station to map
    private fun getArrivals(stationId : Int){
        Backend().retrofitService.getStationArrival(
            stationId
        ).enqueue(object :
            Callback<List<Arrival>> {
            override fun onResponse(
                call: Call<List<Arrival>>,
                response: Response<List<Arrival>>
            ) {
                Log.v("API Arrivals onResponse", response.body().toString())
                arrivalToMap(stationId, response.body() ?: return)
            }

            override fun onFailure(call: Call<List<Arrival>>, t: Throwable) {
                Log.e("API Arrivals onFailure", t.toString())
            }
        }
        )
    }

    // handle all requests for station details
    fun loadStationDetails(stations : List<Station>){
        stations.forEach {
            stationToMap(it)
            getDepartures(it.id)
            getArrivals(it.id)
        }
        GlobalScope.launch {
            // wait for all requests to finish
            delay(1000)
            Log.i("API", getStationMap().value?.contentMap.toString())
        }
    }
}

object BackendHandler {
    private val stationMap : MutableLiveData<MutableMap<Int, SafeStationDetails>> = MutableLiveData(
            mutableMapOf()
    )

    private fun stationToMap(newStation:Station){
        stationMap.value?.put(newStation.id, SafeStationDetails(newStation, mutableListOf(), mutableListOf()))
    }

    private fun departureToMap(id:Int, dp : List<Departure>){
        stationMap.value?.get(id)?.departures?.addAll(dp)
        stationMap.value = stationMap.value
    }

    private fun arrivalToMap(id:Int, ar : List<Arrival>){
        stationMap.value?.get(id)?.arrival?.addAll(ar)
        stationMap.value = stationMap.value
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
        Backend().retrofitService.getNearbyStations(latitude, longitude, distance).enqueue(object : Callback<List<Station>>{
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
    private fun getDepartures(stationId : Int){
        Backend().retrofitService.getStationDeparture(
            stationId
        ).enqueue(object :
            Callback<List<Departure>> {
            override fun onResponse(
                call: Call<List<Departure>>,
                response: Response<List<Departure>>
            ) {
                Log.v("API Departures onResponse", response.body().toString())
                departureToMap(stationId, response.body() ?: return)
            }

            override fun onFailure(call: Call<List<Departure>>, t: Throwable) {
                Log.e("API Departures onFailure $stationId", t.toString())
            }
        }
        )
    }

    // add arrivals of station to map
    private fun getArrivals(stationId : Int){
        Backend().retrofitService.getStationArrival(
            stationId
        ).enqueue(object :
            Callback<List<Arrival>> {
            override fun onResponse(
                call: Call<List<Arrival>>,
                response: Response<List<Arrival>>
            ) {
                Log.v("API Arrivals onResponse", response.body().toString())
                arrivalToMap(stationId, response.body() ?: return)
            }

            override fun onFailure(call: Call<List<Arrival>>, t: Throwable) {
                Log.e("API Arrivals onFailure $stationId", t.toString())
            }
        }
        )
    }

    // handle all requests for station details
    fun loadStationDetails(stations : List<Station>){
        stationMap.value = mutableMapOf()
        stations.forEach {
            stationToMap(it)
            getDepartures(it.id)
            getArrivals(it.id)
        }
        GlobalScope.launch {
            // wait for all requests to finish
            delay(1000)
            Log.i("API", getStationMap().value.toString())
        }
    }
}