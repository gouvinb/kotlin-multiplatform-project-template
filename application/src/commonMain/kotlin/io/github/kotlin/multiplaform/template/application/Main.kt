package io.github.kotlin.multiplaform.template.application

import io.github.kotlin.multiplaform.template.lib.a.Greeting

fun main() {
    println(
        """
            |Application:
            |	${Greeting().hello()}
        """.trimMargin(),
    )
}
