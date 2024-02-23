package io.github.kotlin.multiplatform.template.application

import io.github.kotlin.multiplatform.template.lib.a.Greeting

fun greetingText() = """
    |Application:
    |	${Greeting().hello()}
""".trimMargin()
