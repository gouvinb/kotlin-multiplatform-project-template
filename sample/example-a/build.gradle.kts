import io.github.kotlin.multiplatform.template.gradle.utils.properties.SelectedTarget
import io.github.kotlin.multiplatform.template.gradle.utils.extenstion.configureOrCreateNativePlatforms
import io.github.kotlin.multiplatform.template.gradle.utils.extenstion.configureSourceSetHierarchy
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.js.KotlinJsTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("multiplatform")

    id("io.github.kotlin.multiplatform.template.gradle.plugins.compile")
    id("io.github.kotlin.multiplatform.template.gradle.plugins.test")

    id("io.github.kotlin.multiplatform.template.gradle.plugins.dokka")
    id("io.github.kotlin.multiplatform.template.gradle.plugins.spotless.java")
}

group = "io.github.kotlin.multiplatform.template.example.a"
version = "0.1.0"

repositories {
    mavenCentral()
    google()
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
    configureOrCreateNativePlatforms(IR)
        .forEach { kotlinTarget ->
            when (kotlinTarget) {
                is KotlinNativeTarget -> kotlinTarget.apply {
                    binaries {
                        executable {
                            baseName = rootProject.name
                            entryPoint = "io.github.kotlin.multiplatform.template.example.a.main"
                        }
                    }
                }

                is KotlinJsTarget -> kotlinTarget.apply {
                    binaries {
                        executable()
                    }
                }

                is KotlinJvmTarget -> kotlinTarget.apply { /* no-op */ }
                else -> { /* no-op */
                }
            }
        }

    sourceSets {
        configureSourceSetHierarchy()
        val selectedTarget = SelectedTarget.getFromProperty()

        val commonMain by getting {
            dependencies {
                implementation(project(":library:lib-a"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        val hashFunctions by getting {}

        val nonAppleMain by getting {}

        val nonJvmMain by getting {}
        val nonJvmTest by getting {}

        if (selectedTarget.matchWith(SelectedTarget.JVM) || selectedTarget.matchWith(SelectedTarget.NATIVE)) {
            val jvmMain by getting {}
            val jvmTest by getting {}
        }

        if (selectedTarget.matchWith(SelectedTarget.JS)) {
            val jsMain by getting {}
            val jsTest by getting {}
        }
    }
}
