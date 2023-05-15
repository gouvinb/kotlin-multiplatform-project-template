package io.github.kotlin.multiplatform.template.gradle.utils.properties

enum class Environment {
    CI, LOCAL;

    fun matchWith(target: Environment) = this == target

    companion object {
        fun getFromProperty() = Environment.valueOf(System.getProperty("environment", "LOCAL"))
    }
}
