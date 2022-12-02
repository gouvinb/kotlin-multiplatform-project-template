package io.github.kotlin.multiplaform.template.lib.a

actual class Greeting {
    private val helloWorld: String = "Hello JS!"

    actual fun hello() {
        println(helloWorld)
    }
}
