package io.github.kotlin.multiplatform.template.gradle.utils

object SystemInfo {
    val hostOs: String? = System.getProperty("os.name")

    val appleTargets = listOf(
        "iosArm64",
        "iosX64",
        "iosSimulatorArm64",
        "macosX64",
        "macosArm64",
        "tvosArm64",
        "tvosX64",
        "tvosSimulatorArm64",
        "watchosArm32",
        "watchosArm64",
        // "watchosX86",
        "watchosX64",
        "watchosSimulatorArm64"
    )

    val mingwTargets = listOf(
        "mingwX64"
    )

    val linuxTargets = listOf(
        "linuxX64"
    )

    val nativeTargets = appleTargets + linuxTargets + mingwTargets
}
