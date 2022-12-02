package io.github.kotlin.multiplaform.template.gradle.project.utils.extenstion

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

@Suppress("unused")
fun KotlinMultiplatformExtension.configureOrCreateNativePlatforms(): ArrayList<KotlinTarget> {
    return ArrayList<KotlinTarget>().apply {
        jvm {
            withJava()
        }.apply { add(this) }

        js {
            compilations.all {
                kotlinOptions {
                    moduleKind = "umd"
                    sourceMap = true
                    metaInfo = true
                }
            }
            nodejs {
                testTask {
                    useMocha {
                        timeout = "30s"
                    }
                }
            }
            browser {
            }
        }.apply { add(this) }

        iosX64().apply { add(this) }
        iosArm64().apply { add(this) }
        iosSimulatorArm64().apply { add(this) }
        tvosX64().apply { add(this) }
        tvosArm64().apply { add(this) }
        tvosSimulatorArm64().apply { add(this) }
        watchosArm32().apply { add(this) }
        watchosArm64().apply { add(this) }
        watchosX86().apply { add(this) }
        watchosX64().apply { add(this) }
        watchosSimulatorArm64().apply { add(this) }
        // Required to generate tests tasks: https://youtrack.jetbrains.com/issue/KT-26547
        linuxX64().apply { add(this) }
        macosX64().apply { add(this) }
        macosArm64().apply { add(this) }
        mingwX64().apply { add(this) }
    }
}
