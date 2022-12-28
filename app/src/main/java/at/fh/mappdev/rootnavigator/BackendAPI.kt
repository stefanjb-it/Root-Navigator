package at.fh.mappdev.rootnavigator

import android.util.Log
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

// the Request Singleton
// SET userId BEFORE USAGE
object BackendAPI {
    val retrofit: Retrofit
    val retrofitService: API_BACKENDSERVICE
    init {
        val moshi = Moshi.Builder().build()
        retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://root-nv-backend.fastcloud-it.net")
            .build()
        retrofitService = retrofit.create(API_BACKENDSERVICE::class.java)
    }
}

// contains Request Details
interface API_BACKENDSERVICE {
    @GET("/fhData/{study}/{start}/{end}")
    @Headers("Cookie: {tokenId}")
    fun getFHData(@Header("Cookie") tokenId:String, @Path("study") studyProgramme:String, @Path("start") from:String, @Path("end") to:String) : Call<List<FHDataInstance>>
}

// return Data Structure
@JsonClass(generateAdapter = true)
class FHDataInstance(val title:String, val prof:String?, val typ:String?, val room:String?, val start:String, val end:String){
}

@JsonClass(generateAdapter = true)
class FirstFHDataResponse(val code:Int, val data:List<FHDataInstance>?)

// use these Callbacks for correct Data Mapping
object API_BACKEND_CallbackHolder{
    // use when failed Retrieval
    fun error():Unit{
        Log.v("API_BACKEND", "ran into controlled error")
    }

    // TO ALTER FOR USAGE
    const val myToken = "userId=eyJhbGciOiJSUzI1NiIsImtpZCI6Ijk1MWMwOGM1MTZhZTM1MmI4OWU0ZDJlMGUxNDA5NmY3MzQ5NDJhODciLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vaG9zdGluZy0zNTE0MTkiLCJhdWQiOiJob3N0aW5nLTM1MTQxOSIsImF1dGhfdGltZSI6MTY3MDE1MDg4MSwidXNlcl9pZCI6IklZRWx2dDRscVRlRzNqYWxySE1qZXZ3ODVkaDIiLCJzdWIiOiJJWUVsdnQ0bHFUZUczamFsckhNamV2dzg1ZGgyIiwiaWF0IjoxNjcwMTUwODgxLCJleHAiOjE2NzAxNTQ0ODEsImVtYWlsIjoidGltby5rYXBwZWxAZWR1LmZoLWpvYW5uZXVtLmF0IiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJmaXJlYmFzZSI6eyJpZGVudGl0aWVzIjp7ImVtYWlsIjpbInRpbW8ua2FwcGVsQGVkdS5maC1qb2FubmV1bS5hdCJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.fhkaIuuiDzl_tdUjy3qR5KVTINdG7W82HDZhKK1X20tKDzNCh8dEKLsIej1aewvX2DRBnEoegaS0Pf3D7th7WI9Tibhl5933MT2UXMhb2tW7JYnthNXf4vAEGdcLDizDXYWsL0J6yJoaqvVsTr0qQfifdd0S8jROUShhttNQUcGO73JUZ_fCvup-cBQ1Y7cpTERmaw-P_KqzAZ4pCfamblctz8uDV-zYzeWNacS32se9vyLJaN3kTJAgnijjrp1sIxIfVWSetCMeYjmF_-GNJhAhY0z1Wqhlz_oOEKymAHQ8j1Crr6EUX-Tq42NWKT4FAG-JUCIX2FXB2x6WwyfWXQ"

    fun instanceList(success: (instanceList: List<FHDataInstance>) -> Unit, error: (errorMessage: String) -> Unit) {
        BackendAPI.retrofitService.getFHData(myToken, "ima21", "2022-12-03", "2022-12-23").enqueue(object: Callback<List<FHDataInstance>> {
            override fun onFailure(call: Call<List<FHDataInstance>>, t: Throwable) {
                Log.e("api error", "onFailure")
            }

            override fun onResponse(call: Call<List<FHDataInstance>>, response: Response<List<FHDataInstance>>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    success(responseBody)
                } else {
                    Log.e("api semi error", response.toString())
                    error(responseBody.toString())
                }
            }
        })
    }
}
