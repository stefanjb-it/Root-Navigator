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

    val emailTextfield = hasTestTag("UserEmail") and hasClickAction()
    val passwordTextfield = hasTestTag("UserPassword") and hasClickAction()
    val loginButton = hasText("Sign In") and hasClickAction()
    val dropDownType = hasText("Type") and hasClickAction()
    val typeValue = hasText("Normal") and hasClickAction()
    val textFieldDegreeProgram = hasText("Degree Program") and hasClickAction()
    val textFieldGroup = hasText("Group") and hasClickAction()
    val textFieldRootpoint = hasText("Rootpoint") and hasClickAction()
    val textFieldLine= hasText("Line") and hasClickAction()
    val textFieldMinutes = hasText("Minutes") and hasClickAction()

    @Test
    fun click_saveSettings() {
        // Settings, edit settings and save
        // composeTestRule.onRoot().printToLog("TAG")

        composeTestRule.onNode(emailTextfield).assertExists()
        composeTestRule.onNode(emailTextfield).performClick()
        composeTestRule.onNode(emailTextfield).performTextClearance()
        composeTestRule.onNode(emailTextfield).performTextInput("test@test.test")

        composeTestRule.onNode(passwordTextfield).assertExists()
        composeTestRule.onNode(passwordTextfield).performClick()
        composeTestRule.onNode(passwordTextfield).performTextClearance()
        composeTestRule.onNode(passwordTextfield).performTextInput("Tester123!")

        composeTestRule.onNode(loginButton).assertExists()
        composeTestRule.onNode(loginButton).performClick()

        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule
                .onAllNodesWithContentDescription("Setting")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithContentDescription("Settings").assertExists()
        composeTestRule.onNodeWithContentDescription("Settings").performClick()

        composeTestRule.onNode(dropDownType).assertExists()
        composeTestRule.onNode(dropDownType).performClick()
        composeTestRule.onAllNodes(typeValue)[0].performClick()

        composeTestRule.onAllNodes(isRoot()).onFirst().printToLog("TAG")
        composeTestRule.onAllNodes(isRoot()).onLast().printToLog("TAG")

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