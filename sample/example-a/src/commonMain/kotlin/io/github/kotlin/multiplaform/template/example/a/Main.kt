package io.github.kotlin.multiplaform.template.example.a

import io.github.kotlin.multiplaform.template.lib.a.Greeting

fun main() {
    println(
        """
            |Sample A:
            |\t${Greeting().hello()}
        """.trimMargin()
    )
}
