import utils.extenstion.configureOrCreateNativePlatforms
import utils.extenstion.configureSourceSetHierarchy
import utils.properties.SelectedTarget

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("plugin.android.library")
    kotlin("multiplatform")

    id("plugin.android.library.compose")

    id("plugins.compile.kotlin")
    id("plugins.test")

    id("plugins.dokka")
    id("plugins.spotless.java")
}

group = "io.github.kotlin.multiplatform.template.lib.a"
version = "0.1.0"

android {
    namespace = group.toString()

    buildFeatures {
        compose = true
    }
}

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
 *       |   |   |-- macosArm64
 *       |   |   |-- tvosArm64
 *       |   |   '-- watchosArm64
 *       |   '-- linux
 *       |       |-- linuxArm64
 *       |       '-- linuxX64
 *       '-- mingw
 *           '-- mingwX64
 * ```
 *
 * The `nonJvm` source set excludes that platform.
 *
 * The `hashFunctions` source set builds on all platforms. It ships as a main source set on non-JVM
 * platforms and as a test source set on the JVM platform.
 * ```
 *
 * The `nonJvm` source set excludes that platform.
 *
 * The `hashFunctions` source set builds on all platforms. It ships as a main source set on non-JVM
 * platforms and as a test source set on the JVM platform.
 */
kotlin {
    configureOrCreateNativePlatforms(isAndroidProject = true)

    sourceSets {
        configureSourceSetHierarchy(isAndroidProject = true)
        val selectedTarget = SelectedTarget.getFromProperty()

        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        if (selectedTarget.matchWith(SelectedTarget.ANDROID)) {
            val androidMain by getting {
                dependencies {
                    implementation(libs.androidx.compose.runtime)
                }
            }
            val androidUnitTest by getting {}
            val androidInstrumentedTest by getting {}
        }

        if (selectedTarget.matchWith(SelectedTarget.JVM)) {
            val jvmMain by getting {}
            val jvmTest by getting {}
        }

        if (selectedTarget.matchNotWith(SelectedTarget.JVM)) {
            val hashFunctionsMain by getting {}
            val hashFunctionsTest by getting {}

            val nonAppleMain by getting {}
            val nonAppleTest by getting {}

            val nonJvmMain by getting {}
            val nonJvmTest by getting {}

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
}
