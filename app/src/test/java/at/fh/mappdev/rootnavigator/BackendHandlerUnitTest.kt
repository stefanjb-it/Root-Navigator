package at.fh.mappdev.rootnavigator

import android.content.Context
import android.os.Looper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import at.fh.mappdev.rootnavigator.database.BackendHandler
import at.fh.mappdev.rootnavigator.database.GlobalVarHolder
import at.fh.mappdev.rootnavigator.database.Station
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


/* Copyright 2019 Google LLC.
   SPDX-License-Identifier: Apache-2.0 */
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    // 0 = value as soon as returned, 1 = value on first change, 2 = value on second change, ...
    useThisUpdate : Int = 1,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(useThisUpdate)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}


class BackendHandlerUnitTest {
    val stationListResponseString:String = """
        [
            {
                "type": "stop",
                "id": "691543",
                "name": "Graz Jakominiplatz (Mitte)",
                "location": {
                    "type": "location",
                    "id": "691543",
                    "latitude": 47.067402,
                    "longitude": 15.442146
                },
                "products": {
                    "nationalExpress": false,
                    "national": false,
                    "interregional": false,
                    "regional": false,
                    "suburban": false,
                    "bus": true,
                    "ferry": false,
                    "subway": false,
                    "tram": true,
                    "onCall": false
                },
                "distance": 14
            },
            {
                "type": "stop",
                "id": "691544",
                "name": "Graz Jakominiplatz (Reitschulgasse)",
                "location": {
                    "type": "location",
                    "id": "691544",
                    "latitude": 47.067384,
                    "longitude": 15.443171
                },
                "products": {
                    "nationalExpress": false,
                    "national": false,
                    "interregional": false,
                    "regional": false,
                    "suburban": false,
                    "bus": true,
                    "ferry": false,
                    "subway": false,
                    "tram": false,
                    "onCall": false
                },
                "distance": 81
            },
            {
                "type": "stop",
                "id": "691011",
                "name": "Graz Jakominiplatz (Gleisdorfer Gasse)",
                "location": {
                    "type": "location",
                    "id": "691011",
                    "latitude": 47.067528,
                    "longitude": 15.443405
                },
                "products": {
                    "nationalExpress": false,
                    "national": false,
                    "interregional": false,
                    "regional": false,
                    "suburban": false,
                    "bus": true,
                    "ferry": false,
                    "subway": false,
                    "tram": false,
                    "onCall": false
                },
                "distance": 102
            },
            {
                "type": "stop",
                "id": "691040",
                "name": "Graz Opernring",
                "location": {
                    "type": "location",
                    "id": "691040",
                    "latitude": 47.068274,
                    "longitude": 15.442569
                },
                "products": {
                    "nationalExpress": false,
                    "national": false,
                    "interregional": false,
                    "regional": false,
                    "suburban": false,
                    "bus": true,
                    "ferry": false,
                    "subway": false,
                    "tram": false,
                    "onCall": false
                },
                "distance": 116
            },
            {
                "type": "stop",
                "id": "691025",
                "name": "Graz Jakominiplatz (Radetzkystraße)",
                "location": {
                    "type": "location",
                    "id": "691025",
                    "latitude": 47.066629,
                    "longitude": 15.440735
                },
                "products": {
                    "nationalExpress": false,
                    "national": false,
                    "interregional": false,
                    "regional": false,
                    "suburban": false,
                    "bus": true,
                    "ferry": false,
                    "subway": false,
                    "tram": false,
                    "onCall": false
                },
                "distance": 126
            }
        ]
    """.trimIndent()
    var stationListResponse:List<Station>? = null
    private lateinit var auth:FirebaseAuth

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        //mockkStatic(FirebaseAuth::class)
        //every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)

        GlobalVarHolder.userIdToken = "_iouwozhAOPJUWz821jpiUpo&ai2woz1442hjpoijhwpqujjqpWAFR"
        val moshi : Moshi = Moshi.Builder().build()
        val listType = Types.newParameterizedType(List::class.java, Station::class.java)
        val adapter = moshi.adapter<List<Station>>(listType)
        stationListResponse = adapter.fromJson(stationListResponseString)
    }

    @Test
    fun getNearbyStationsUnauthorized(){
        assertEquals(stationListResponse, BackendHandler.getNearbyStations(47.06727184602459, 15.442097181893473, 250).getOrAwaitValue(10, 1).content)
    }

}