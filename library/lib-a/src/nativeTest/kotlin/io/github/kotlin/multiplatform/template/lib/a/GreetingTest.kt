package io.github.kotlin.multiplatform.template.lib.a

import kotlin.test.Test
import kotlin.test.assertTrue

class GreetingTest : GreetingTestCommon() {

    @Test
    override fun hello() {
        super.hello()
        assertTrue("Native" in greeting.hello())
    }
}
