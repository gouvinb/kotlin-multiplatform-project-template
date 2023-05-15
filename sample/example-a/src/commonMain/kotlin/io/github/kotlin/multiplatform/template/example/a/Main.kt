package io.github.kotlin.multiplatform.template.example.a

import io.github.kotlin.multiplatform.template.lib.a.Greeting

fun main() {
    println(
        """
            |Sample A:
            |\t${Greeting().hello()}
        """.trimMargin(),
    )
}
