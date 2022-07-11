package com.example.tipcalculator

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.tipcalculator.ui.theme.TipCalculatorTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// write instructions to interact with the UI components so that
// the tip calculating process is tested through the UI.

@RunWith(AndroidJUnit4::class)
class TipCalculatorInstrumentedTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun calculate_20_percent_tip() {
        // sets the UI content of the composeTestRule
        composeTestRule.setContent {
            TipCalculatorTheme {
                TipCalculatorScreen()
            }
        }

        /*
        UI components can be accessed as nodes through the composeTestRule.
        A common way to do this is to access a node that contains a
        particular text with the onNodeWithText() method.
         */

        // Populating TextFields
        composeTestRule.onNodeWithText("Bill Amount").performTextInput("100")
        composeTestRule.onNodeWithText("Tip (%)").performTextInput("20")

        // Checking
        composeTestRule.onNodeWithText("Tip amount: $20.00").assertExists()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.tipcalculator", appContext.packageName)
    }

}