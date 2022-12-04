import io.github.kotlin.multiplaform.template.gradle.project.utils.SelectedTarget
import io.github.kotlin.multiplaform.template.gradle.project.utils.SystemInfo.appleTargets
import io.github.kotlin.multiplaform.template.gradle.project.utils.SystemInfo.linuxTargets
import io.github.kotlin.multiplaform.template.gradle.project.utils.SystemInfo.mingwTargets
import io.github.kotlin.multiplaform.template.gradle.project.utils.extenstion.configureOrCreateNativePlatforms
import io.github.kotlin.multiplaform.template.gradle.project.utils.extenstion.createSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.js.KotlinJsTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka")

    id("io.github.kotlin.multiplaform.template.gradle.project.base.spotless.java")
}

group = "io.github.kotlin.multiplaform.template.application"
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
    configureOrCreateNativePlatforms(jsCompilerType = IR)
        .forEach { kotlinTarget ->
            when (kotlinTarget) {
                is KotlinNativeTarget -> kotlinTarget.apply {
                    binaries {
                        executable {
                            baseName = rootProject.name
                            entryPoint = "io.github.kotlin.multiplaform.template.application.main"
                        }
                    }
                }
                is KotlinJsTarget -> kotlinTarget.apply {
                    binaries {
                        executable()
                    }
                }
                is KotlinJvmTarget -> kotlinTarget.apply { /* no-op */ }
                else -> { /* no-op */ }
            }
        }

    sourceSets {
        val selectedTarget = SelectedTarget.getFromProperty()

        val commonMain by getting {
            dependencies {
                implementation(project(":library-lib-a"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        val hashFunctions by creating {
            dependsOn(commonMain)
        }

        val nonAppleMain by creating {
            dependsOn(hashFunctions)
        }

        val nonJvmMain by creating {
            dependsOn(hashFunctions)
            dependsOn(commonMain)
        }
        val nonJvmTest by creating {
            dependsOn(commonTest)
        }

        if (selectedTarget.matchWith(SelectedTarget.JVM) || selectedTarget.matchWith(SelectedTarget.NATIVE)) {
            val jvmMain by getting {
            }
            val jvmTest by getting {
                kotlin.srcDir("src/jvmTest/hashFunctions")
                dependencies {}
            }
        }

        if (selectedTarget.matchWith(SelectedTarget.JS)) {
            val jsMain by getting {
                dependsOn(nonJvmMain)
                dependsOn(nonAppleMain)
            }
            val jsTest by getting {
                dependsOn(nonJvmTest)
            }
        }

        if (selectedTarget.matchWith(SelectedTarget.NATIVE)) {
            createSourceSet("nativeMain", parent = nonJvmMain) { nativeMain ->
                createSourceSet("mingwMain", parent = nativeMain, children = mingwTargets) { mingwMain ->
                    mingwMain.dependsOn(nonAppleMain)
                }
                createSourceSet("unixMain", parent = nativeMain) { unixMain ->
                    createSourceSet("linuxMain", parent = unixMain, children = linuxTargets) { linuxMain ->
                        linuxMain.dependsOn(nonAppleMain)
                    }
                    createSourceSet("appleMain", parent = unixMain, children = appleTargets)
                }
            }

            createSourceSet("nativeTest", parent = commonTest, children = mingwTargets + linuxTargets) { nativeTest ->
                nativeTest.dependsOn(nonJvmTest)
                createSourceSet("appleTest", parent = nativeTest, children = appleTargets)
            }
        }
    }
}
