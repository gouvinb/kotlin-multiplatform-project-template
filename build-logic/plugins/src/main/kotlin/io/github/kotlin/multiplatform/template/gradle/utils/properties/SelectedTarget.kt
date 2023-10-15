package io.github.kotlin.multiplatform.template.gradle.utils.properties

enum class SelectedTarget {
    ALL, JS, JVM, NATIVE;

    fun matchWith(target: SelectedTarget) = if (this == ALL) true else this == target

    fun matchNotWith(target: SelectedTarget) = this != ALL && this != target

    companion object {
        fun getFromProperty() = SelectedTarget.valueOf(System.getProperty("target", "ALL"))
    }
}
