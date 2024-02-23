package utils

import org.gradle.api.JavaVersion

object ProjectInfo {
    const val BUILD_TOOLS_VERSION = "34.0.0"
    const val NDK_VERSION = "26.2.11394342"

    const val COMPILE_SDK = 34
    val COMPILE_SDK_EXTENSION: Int? = null

    const val TARGET_SDK = COMPILE_SDK

    const val MIN_SDK = 33

    val JAVA_VERSION = JavaVersion.VERSION_11
}
