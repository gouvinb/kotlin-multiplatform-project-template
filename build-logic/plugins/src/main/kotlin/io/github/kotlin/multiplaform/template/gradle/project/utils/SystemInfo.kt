package io.github.kotlin.multiplaform.template.gradle.project.utils

object SystemInfo {
    val hostOs: String? = System.getProperty("os.name")
    val isMingwX64 = hostOs?.startsWith("Windows")

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
        "watchosX86",
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
