package at.fh.mappdev.rootnavigator

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import at.fh.mappdev.rootnavigator.auth.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class HomeActivityCardInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<LoginActivity>()

    private val emailTextfield = hasTestTag("UserEmail") and hasClickAction()
    private val passwordTextfield = hasTestTag("UserPassword") and hasClickAction()
    private val loginButton = hasText("Sign In") and hasClickAction()

    @Test
    fun clickCard_Dropdown() {

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
                .onAllNodesWithContentDescription("route type")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Dep.").assertDoesNotExist()
        composeTestRule.onAllNodesWithContentDescription("Show more").onFirst().performClick()
        composeTestRule.onNodeWithText("Dep.").assertExists()

        composeTestRule.onRoot().printToLog("TAG")
    }
}