package at.fh.mappdev.rootnavigator.database

import com.squareup.moshi.Moshi
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

class Backend {
    val retrofit: Retrofit
    val retrofitService: BackendService
    init {
        val moshi = Moshi.Builder().build()
        retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://oebb-hafas.fastcloud-it.net")
            .build()
        retrofitService = retrofit.create(BackendService::class.java)
    }
}

interface BackendService {
    @GET("/locations/nearby/")
    //@Headers("Cookie: {tokenId}")
    fun getNearbyStations(/*@Header("Cookie") tokenId:String,*/ @Query("latitude") latitude: Double, @Query("longitude") longitude: Double, @Query("distance") distance: Int):Call<List<Station>>
    @GET("/stops/{id}/departures")
    //@Headers("Cookie: {tokenId}")
    fun getStationDeparture(/*@Header("Cookie") tokenId:String,*/@Path("id") id:Int): Call<List<Departure>>
    @GET("/stops/{id}/arrivals")
    //@Headers("Cookie: {tokenId}")
    fun getStationArrival(/*@Header("Cookie") tokenId:String,*/@Path("id") id:Int): Call<List<Arrival>>
    @GET("/locations")
    //@Headers("Cookie: {tokenId}")
    fun getLocations(/*@Header("Cookie") tokenId:String,*/ @Query("query") query:String, @Query("results") results:Int, @Query("addresses") addresses:Boolean, @Query("poi") poi:Boolean):Call<List<Station>>
}