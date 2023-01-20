package at.fh.mappdev.rootnavigator

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import at.fh.mappdev.rootnavigator.auth.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegistrationActivityInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<LoginActivity>()

    private val signUpText = hasTestTag("SignUp")
    private val standardButton = hasTestTag("StandardButton") and hasClickAction()
    private val studentButton = hasTestTag("StudentButton") and hasClickAction()
    private val emailTextfield = hasTestTag("UserEmail") and hasClickAction()
    private val passwordTextfield = hasTestTag("UserPassword") and hasClickAction()
    private val passwordRepeatTextfield = hasTestTag("UserPasswordRepeat") and hasClickAction()
    private val secondButton = hasTestTag("RegistrationScreenTwoButton") and hasClickAction()
    private val rootPointTextfield = hasTestTag("RootPoint") and hasClickAction()
    private val linesTextfield = hasTestTag("Lines") and hasClickAction()
    private val destinationTextfield = hasTestTag("Destination") and hasClickAction()
    private val finishButton = hasTestTag("RegistrationFinishButton") and hasClickAction()

    @Test
    fun create_NewAccount() {
        composeTestRule.onNode(signUpText).assertExists()
        composeTestRule.onNode(signUpText).performClick()

        composeTestRule.waitUntil(timeoutMillis = 2000) {
            composeTestRule.onAllNodesWithText("Student").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNode(standardButton).assertExists()
        composeTestRule.onNode(studentButton).assertExists()
        composeTestRule.onNode(studentButton).performClick()

        composeTestRule.waitUntil(timeoutMillis = 2000) {
            composeTestRule.onAllNodesWithText("E-Mail").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNode(emailTextfield).assertExists()
        composeTestRule.onNode(emailTextfield).performClick()
        composeTestRule.onNode(emailTextfield).performTextClearance()
        composeTestRule.onNode(emailTextfield).performTextInput("test2@test.test")

        composeTestRule.onNode(passwordTextfield).assertExists()
        composeTestRule.onNode(passwordTextfield).performClick()
        composeTestRule.onNode(passwordTextfield).performTextClearance()
        composeTestRule.onNode(passwordTextfield).performTextInput("Tester123!")

        composeTestRule.onNode(passwordRepeatTextfield).assertExists()
        composeTestRule.onNode(passwordRepeatTextfield).performClick()
        composeTestRule.onNode(passwordRepeatTextfield).performTextClearance()
        composeTestRule.onNode(passwordRepeatTextfield).performTextInput("Tester123!")

        composeTestRule.onNode(secondButton).assertExists()
        composeTestRule.onNode(secondButton).performClick()

        composeTestRule.waitUntil(timeoutMillis = 2000) {
            composeTestRule.onAllNodesWithText("Enter Address Data").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNode(rootPointTextfield).assertExists()
        composeTestRule.onNode(rootPointTextfield).performClick()
        composeTestRule.onNode(rootPointTextfield).performTextClearance()
        composeTestRule.onNode(rootPointTextfield).performTextInput("Graz Hbf")

        composeTestRule.onNode(linesTextfield).assertExists()
        composeTestRule.onNode(linesTextfield).performClick()
        composeTestRule.onNode(linesTextfield).performTextClearance()
        composeTestRule.onNode(linesTextfield).performTextInput("Tram 7, Tram 4")

        composeTestRule.onNode(destinationTextfield).assertExists()
        composeTestRule.onNode(destinationTextfield).performClick()
        composeTestRule.onNode(destinationTextfield).performTextClearance()
        composeTestRule.onNode(destinationTextfield).performTextInput("FH Joanneum")

        composeTestRule.onNode(finishButton).assertExists()
        composeTestRule.onNode(finishButton).performClick()
    }
}