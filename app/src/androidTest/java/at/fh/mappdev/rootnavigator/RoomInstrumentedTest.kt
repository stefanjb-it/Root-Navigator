package at.fh.mappdev.rootnavigator

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import at.fh.mappdev.rootnavigator.database.ReminderDatabase
import at.fh.mappdev.rootnavigator.database.ReminderItemRoom
import kotlinx.coroutines.runBlocking

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.FixMethodOrder

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@FixMethodOrder(org.junit.runners.MethodSorters.NAME_ASCENDING)
class RoomInstrumentedTest {
    var context: Context? = null
    var db:ReminderDatabase? = null
    val firstItem = ReminderItemRoom(996, "19/1/2023", "15:43", "Medium", "hehe", true)
    val secondItem = ReminderItemRoom(997, "23/1/2023", "19:43", "High", "whaddup", false)

    @Before
    fun useAppContext() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        db = ReminderDatabase.getDatabase(context!!.applicationContext)
    }

    @Test
    fun aa_insertionTest(){
        var ex : Exception? = null
        try {
        runBlocking {
            db?.reminderDao?.newReminder(firstItem)
        }
        } catch (e: Exception) {
            ex = e
        }
        assertEquals(null, ex)
    }

    @Test
    fun ab_readTest(){
        var data: List<ReminderItemRoom>? = null
        runBlocking {
            data = db?.reminderDao?.getAllReminders()
        }
        assertNotEquals(listOf<ReminderItemRoom>(), data)
        assertEquals(true, data?.contains(firstItem))
    }

    @Test
    fun ac_deleteTest(){
        var ex : Exception? = null
        try {
            runBlocking {
                db?.reminderDao?.deleteReminder(firstItem)
            }
        } catch (e: Exception) {
            ex = e
        }
        assertEquals(null, ex)
    }

    @Test
    fun zz_fullReminderTest() {
        // Insertion
        var ex : Exception? = null
        try {
            runBlocking {
                db?.reminderDao?.newReminder(secondItem)
            }
        } catch (e: Exception) {
            ex = e
        }
        assertEquals(null, ex)

        // Reading
        var data: List<ReminderItemRoom>? = null
        runBlocking {
            data = db?.reminderDao?.getAllReminders()
        }
        assertNotEquals(listOf<ReminderItemRoom>(), data)
        assertEquals(true, data?.contains(secondItem))

        // Deletion
        try {
            runBlocking {
                db?.reminderDao?.deleteReminder(secondItem)
            }
        } catch (e: Exception) {
            ex = e
        }
        assertEquals(null, ex)
    }
}