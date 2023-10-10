import io.github.kotlin.multiplatform.template.gradle.utils.properties.SelectedTarget
import io.github.kotlin.multiplatform.template.gradle.utils.extenstion.configureOrCreateNativePlatforms
import io.github.kotlin.multiplatform.template.gradle.utils.extenstion.configureSourceSetHierarchy

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("multiplatform")

    id("io.github.kotlin.multiplatform.template.gradle.plugins.compile")
    id("io.github.kotlin.multiplatform.template.gradle.plugins.test")

    id("io.github.kotlin.multiplatform.template.gradle.plugins.dokka")
    id("io.github.kotlin.multiplatform.template.gradle.plugins.spotless.java")
}

group = "io.github.kotlin.multiplatform.template.lib.a"
version = "0.1.0"

/*
 * Here's the main hierarchy of variants. Any `expect` functions in one level of the tree are
 * `actual` functions in a (potentially indirect) child node.
 *
 * ```
 *   common
 *   |-- jvm
 *   |-- js
 *   '-- native
 *       |- unix
 *       |   |-- apple
 *       |   |   |-- iosArm64
 *       |   |   |-- iosX64
 *       |   |   |-- macosX64
 *       |   |   |-- tvosArm64
 *       |   |   |-- tvosX64
 *       |   |   |-- watchosArm32
 *       |   |   |-- watchosArm64
 *       |   |   '-- watchosX86
 *       |   '-- linux
 *       |       '-- linuxX64
 *       '-- mingw
 *           '-- mingwX64
 * ```
 *
 * The `nonJvm` source set excludes that platform.
 *
 * The `hashFunctions` source set builds on all platforms. It ships as a main source set on non-JVM
 * platforms and as a test source set on the JVM platform.
 */
kotlin {
    configureOrCreateNativePlatforms()

    sourceSets {
        configureSourceSetHierarchy()
        val selectedTarget = SelectedTarget.getFromProperty()

        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        val hashFunctionsMain by getting {}
        val hashFunctionsTest by getting {}

        val nonAppleMain by getting {}
        val nonAppleTest by getting {}

        val nonJvmMain by getting {}
        val nonJvmTest by getting {}

        if (selectedTarget.matchWith(SelectedTarget.JVM)) {
            val jvmMain by getting {}
            val jvmTest by getting {}
        }

        if (selectedTarget.matchWith(SelectedTarget.JS)) {
            val jsMain by getting {}
            val jsTest by getting {}
        }

        if (selectedTarget.matchWith(SelectedTarget.NATIVE)) {
            val nativeMain by getting {}
            val nativeTest by getting {}

            val appleMain by getting {}
            val appleTest by getting {}

            val linuxMain by getting {}
            val linuxTest by getting {}

            val mingwMain by getting {}
            val mingwTest by getting {}
        }
    }
}
