package io.github.kotlin.multiplatform.template.lib.a

actual class Greeting {
    private val helloWorld: String = "Hello JVM!"

    actual fun hello() = helloWorld
}
