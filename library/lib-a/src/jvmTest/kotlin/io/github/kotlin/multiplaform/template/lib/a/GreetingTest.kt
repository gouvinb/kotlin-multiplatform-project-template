package io.github.kotlin.multiplaform.template.lib.a

import kotlin.test.Test
import kotlin.test.assertTrue

class GreetingTest : GreetingTestCommon() {

    @Test
    override fun hello() {
        super.hello()
        assertTrue("JVM" in greeting.hello())
    }
}
