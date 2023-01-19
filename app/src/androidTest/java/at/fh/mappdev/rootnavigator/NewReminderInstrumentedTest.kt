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

    val emailTextfield = hasTestTag("UserEmail") and hasClickAction()
    val passwordTextfield = hasTestTag("UserPassword") and hasClickAction()
    val loginButton = hasText("Sign In") and hasClickAction()
    val dropDownPriority = hasText("Priority") and hasClickAction()
    val priorityValue = hasText("High") and hasClickAction()
    val descriptionTextfield = hasTestTag("Description") and hasClickAction()

    @Test
    fun create_newReminder() {

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
                .onAllNodesWithContentDescription("Reminder")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithContentDescription("Reminder").assertExists()
        composeTestRule.onNodeWithContentDescription("Reminder").performClick()

        composeTestRule.onNodeWithText("Create new Reminder").assertExists()
        composeTestRule.onNodeWithText("Create new Reminder").performClick()

        composeTestRule.onNodeWithText("Pick Date").assertExists()
        composeTestRule.onNodeWithText("Pick Time").assertExists()

        composeTestRule.onNode(dropDownPriority).assertExists()
        composeTestRule.onNode(dropDownPriority).performClick()
        composeTestRule.onAllNodes(priorityValue)[0].performClick()

        composeTestRule.onNode(descriptionTextfield).assertExists()
        composeTestRule.onNode(descriptionTextfield).performClick()
        composeTestRule.onNode(descriptionTextfield).performTextClearance()
        composeTestRule.onNode(descriptionTextfield).performTextInput("Test")

        composeTestRule.onNodeWithText("Save").assertExists()
        composeTestRule.onNodeWithText("Save").performClick()

        //composeTestRule.onRoot().printToLog("TAG")
    }
}