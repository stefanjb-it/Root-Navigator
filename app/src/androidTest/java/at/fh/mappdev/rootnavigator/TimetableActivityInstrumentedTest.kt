package at.fh.mappdev.rootnavigator

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TimetableActivityInstrumentedTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<LoginActivity>()

    @Test
    fun click_Timetable() {
        composeTestRule.onNodeWithContentDescription("Timetable").assertExists()
        composeTestRule.onNodeWithContentDescription("Timetable").performClick()

        /*
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule.onNodeWithTag("TimetableTag")
        }
        */

        //composeTestRule.onRoot().printToLog("TAG")
    }
}