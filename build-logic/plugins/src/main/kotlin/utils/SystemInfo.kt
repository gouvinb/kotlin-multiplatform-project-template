package utils

object SystemInfo {
    val hostOs: String? = System.getProperty("os.name")

    val appleTargets = listOf(
        "iosArm64",
        "iosSimulatorArm64",
        "macosArm64",
        "tvosArm64",
        "tvosSimulatorArm64",
        "watchosArm64",
        "watchosSimulatorArm64",
    )

    val mingwTargets = listOf(
        "mingwX64",
    )

    val linuxTargets = listOf(
        "linuxArm64",
        "linuxX64",
    )

    val nativeTargets = appleTargets + linuxTargets + mingwTargets
}
