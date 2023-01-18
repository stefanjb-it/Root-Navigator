package at.fh.mappdev.rootnavigator

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import at.fh.mappdev.rootnavigator.database.Backend
import at.fh.mappdev.rootnavigator.database.GlobalVarHolder
import at.fh.mappdev.rootnavigator.database.Station
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

// IMPORTANT --> sequential tests to simplify expressions instead of callbacks
class APIRequestUnitTests{
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val jakominiId:Int = 691543
    private val jakominiLat = 47.0672718
    private val jakominiLong = 15.4420971

    @Before
    fun setup(){
        GlobalVarHolder.userIdToken = "_iouwozhAOPJUWz821jpiUpo&ai2woz1442hjpoijhwpqujjqpWAFR"
    }


        @Test
        fun getNearbyStations(){
            val backend = Backend()
            val backendService = backend.retrofitService
            val call = backendService.getNearbyStations(GlobalVarHolder.uidPrefix+GlobalVarHolder.userIdToken, jakominiLat, jakominiLong, 250)
            val response = call.execute()
            val body = response.body()
            assertEquals(true, response.isSuccessful)
            assertNotEquals(null, body)
            assert(body?.size == 5)
        }

        @Test
        fun getStationDeparture(){
            val backend = Backend()
            val backendService = backend.retrofitService
            val call = backendService.getStationDeparture(GlobalVarHolder.uidPrefix+GlobalVarHolder.userIdToken, jakominiId, GlobalVarHolder.localReqTime)
            val response = call.execute()
            val body = response.body()
            assertEquals(true, response.isSuccessful)
            assertNotEquals(null, body)
        }

        @Test
        fun getStationArrival(){
            val backend = Backend()
            val backendService = backend.retrofitService
            val call = backendService.getStationArrival(GlobalVarHolder.uidPrefix+GlobalVarHolder.userIdToken, jakominiId, GlobalVarHolder.localReqTime)
            val response = call.execute()
            val body = response.body()
            assertEquals(true, response.isSuccessful)
            assertNotEquals(null, body)
        }

}