package io.github.kotlin.multiplatform.template.application

import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.printToLog
import org.junit.Rule
import org.junit.Test

class MainActivityTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun hello() {
        composeTestRule.onNodeWithText("Application", substring = true).apply {
            printToLog("${MainActivityTest::class.simpleName}")
            assertExists()
            assertTextContains("Android", substring = true)
        }
    }
}
