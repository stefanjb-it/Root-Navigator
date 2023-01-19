package at.fh.mappdev.rootnavigator

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsActivityInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<LoginActivity>()

    val dropDownType = hasText("Type") and hasClickAction()
    val textFieldDegreeProgram = hasText("Degree Program") and hasClickAction()
    val textFieldGroup = hasText("Group") and hasClickAction()
    val textFieldRootpoint = hasText("Rootpoint") and hasClickAction()
    val textFieldLine= hasText("Line") and hasClickAction()
    val textFieldMinutes = hasText("Minutes") and hasClickAction()

    @Test
    fun click_saveSettings() {
        // Settings, edit settings and save
        // composeTestRule.onRoot().printToLog("TAG")

        composeTestRule.onNodeWithContentDescription("Settings").assertExists()
        composeTestRule.onNodeWithContentDescription("Settings").performClick()

        composeTestRule.onNode(dropDownType).assertExists()
        composeTestRule.onNode(dropDownType).performClick()
        //Espresso.onView(withText("Student")).perform(click())
        //Espresso.onData(withText(containsString("Student"))).perform(click())
        // Todo: select Type

        composeTestRule.onNode(textFieldDegreeProgram).assertExists()
        composeTestRule.onNode(textFieldDegreeProgram).performClick()
        composeTestRule.onNode(textFieldDegreeProgram).performTextClearance()
        composeTestRule.onNode(textFieldDegreeProgram).performTextInput("IMA21")

        composeTestRule.onNode(textFieldGroup).assertExists()
        composeTestRule.onNode(textFieldGroup).performClick()
        composeTestRule.onNode(textFieldGroup).performTextClearance()
        composeTestRule.onNode(textFieldGroup).performTextInput("G1")

        composeTestRule.onNode(textFieldRootpoint).assertExists()
        composeTestRule.onNode(textFieldRootpoint).performClick()
        composeTestRule.onNode(textFieldRootpoint).performTextClearance()
        composeTestRule.onNode(textFieldRootpoint).performTextInput("FH Joanneum")

        composeTestRule.onNode(textFieldLine).assertExists()
        composeTestRule.onNode(textFieldLine).performClick()
        composeTestRule.onNode(textFieldLine).performTextClearance()
        composeTestRule.onNode(textFieldLine).performTextInput("S1")

        composeTestRule.onNode(textFieldMinutes).assertExists()
        composeTestRule.onNode(textFieldMinutes).performClick()
        composeTestRule.onNode(textFieldMinutes).performTextClearance()
        composeTestRule.onNode(textFieldMinutes).performTextInput("30")

        composeTestRule.onNodeWithText("Save").assertExists()
        composeTestRule.onNodeWithText("Save").performClick()
    }

}