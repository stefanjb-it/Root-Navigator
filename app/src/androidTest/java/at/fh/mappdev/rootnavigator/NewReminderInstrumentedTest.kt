package at.fh.mappdev.rootnavigator

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewReminderInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<LoginActivity>()

    @Test
    fun create_newReminder() {
        composeTestRule.onNodeWithContentDescription("Reminder").assertExists()
        composeTestRule.onNodeWithContentDescription("Reminder").performClick()

        composeTestRule.onNodeWithText("Create new Reminder").assertExists()
        composeTestRule.onNodeWithText("Create new Reminder").performClick()

        composeTestRule.onNodeWithText("Pick Date").assertExists()
        //composeTestRule.onNodeWithText("Pick Date").performClick()

        //Todo: pick date

        //composeTestRule.onNodeWithText("OK").assertExists()
        //composeTestRule.onNodeWithText("OK").performClick()

        composeTestRule.onNodeWithText("Pick Time").assertExists()
        //composeTestRule.onNodeWithText("Pick Time").performClick()

        //Todo: pick time

        composeTestRule.onNodeWithContentDescription("Dropdown menu").assertExists()
        composeTestRule.onNodeWithContentDescription("Dropdown menu").performClick()

        //Todo: select priority

        composeTestRule.onNodeWithText("Save").assertExists()
        composeTestRule.onNodeWithText("Save").performClick()

        composeTestRule.onRoot().printToLog("TAG")
    }

}