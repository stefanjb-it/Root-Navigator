package at.fh.mappdev.rootnavigator

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import at.fh.mappdev.rootnavigator.auth.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsActivityInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<LoginActivity>()

    private val emailTextfield = hasTestTag("UserEmail") and hasClickAction()
    private val passwordTextfield = hasTestTag("UserPassword") and hasClickAction()
    private val loginButton = hasText("Sign In") and hasClickAction()
    private val dropDownType = hasText("Type") and hasClickAction()
    private val typeValue = hasText("Normal") and hasClickAction()
    private val textFieldDegreeProgram = hasText("Degree Program") and hasClickAction()
    private val textFieldGroup = hasText("Group") and hasClickAction()
    private val textFieldRootpoint = hasText("Rootpoint") and hasClickAction()
    private val textFieldLine= hasText("Line") and hasClickAction()
    private val textFieldMinutes = hasText("Minutes") and hasClickAction()

    @Test
    fun click_saveSettings() {

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
                .onAllNodesWithContentDescription("Settings")
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