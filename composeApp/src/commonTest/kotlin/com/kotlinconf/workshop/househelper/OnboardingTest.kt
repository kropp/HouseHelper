import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.test.*
import com.kotlinconf.workshop.househelper.OnboardingScreen
import kotlin.test.Test
import kotlin.test.assertTrue
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
class OnboardingTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testOnboardingBasicFunctionality() = runComposeUiTest() {
        var nextClicked = false

        // Create test string resources
        setContent {
            OnboardingScreen(
                text = "Welcome to Test",
                subtitle = "This is a test subtitle",
                buttonText = "Next",
                icon = Icons.Default.Home,
                onNext = { nextClicked = true }
            )
        }

        // Verify the text content is displayed
        onNodeWithText("Welcome to Test").assertExists()
        onNodeWithText("This is a test subtitle").assertExists()

        // Verify the button exists and has correct text
        onNodeWithText("Next").assertExists()

        // Click the button and verify the callback was triggered
        onNodeWithText("Next").performClick()
        assertTrue(actual = nextClicked, message = "Next button click was not handled")
    }
}
