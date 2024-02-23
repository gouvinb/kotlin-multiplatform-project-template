package io.github.kotlin.multiplatform.template.application

import org.junit.Assert.assertTrue
import org.junit.Test

class GreetingText {
    @Test fun hello() {
        assertTrue("Android" in greetingText())
    }
}
