package io.github.kotlin.multiplaform.template.lib.a

import kotlin.test.Test
import kotlin.test.assertTrue

open class GreetingTestCommon {

    internal val greeting = Greeting()

    @Test
    open fun hello() {
        assertTrue(greeting.hello() matches """Hello [A-Za-z]+!""".toRegex())
    }
}
