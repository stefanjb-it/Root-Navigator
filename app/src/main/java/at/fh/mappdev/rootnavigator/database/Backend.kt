package at.fh.mappdev.rootnavigator.database

import com.squareup.moshi.Moshi
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

class Backend {
    val retrofit: Retrofit
    val retrofitService: BackendService
    init {
        val moshi = Moshi.Builder().build()
        retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://root-nv-backend.fastcloud-it.net/")
            .build()
        retrofitService = retrofit.create(BackendService::class.java)
    }
}

interface BackendService {
    @GET("/api/hafas/v1/locations/nearby/")
    @Headers("Cookie: {tokenId}")
    fun getNearbyStations(@Header("Cookie") tokenId:String, @Query("latitude") latitude: Double, @Query("longitude") longitude: Double, @Query("distance") distance: Int):Call<List<Station>>
    @GET("/api/hafas/v1/stops/{id}/departures")
    @Headers("Cookie: {tokenId}")
    fun getStationDeparture(@Header("Cookie") tokenId:String,@Path("id") id:Int, @Query("duration") duration:Int): Call<List<Departure>>
    @GET("/api/hafas/v1/stops/{id}/arrivals")
    @Headers("Cookie: {tokenId}")
    fun getStationArrival(@Header("Cookie") tokenId:String,@Path("id") id:Int, @Query("duration") duration:Int): Call<List<Arrival>>
    @GET("/api/hafas/v1/locations")
    @Headers("Cookie: {tokenId}")
    fun getLocations(@Header("Cookie") tokenId:String, @Query("query") query:String, @Query("results") results:Int, @Query("addresses") addresses:Boolean, @Query("poi") poi:Boolean):Call<List<Station>>
}