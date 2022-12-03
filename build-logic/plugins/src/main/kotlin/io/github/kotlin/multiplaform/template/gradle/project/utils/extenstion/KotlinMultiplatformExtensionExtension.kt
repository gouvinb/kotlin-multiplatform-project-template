package io.github.kotlin.multiplaform.template.gradle.project.utils.extenstion

import io.github.kotlin.multiplaform.template.gradle.project.utils.SelectedTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

@Suppress("unused")
fun KotlinMultiplatformExtension.configureOrCreateNativePlatforms(
    jsCompilerType: KotlinJsCompilerType = KotlinJsCompilerType.BOTH,
) = ArrayList<KotlinTarget>().apply {
    val selectedTarget = SelectedTarget.getFromProperty()

    if (selectedTarget.matchWith(SelectedTarget.JVM) || selectedTarget.matchWith(SelectedTarget.NATIVE)) {
        jvm {
            withJava()
        }.apply { add(this) }
    }

    if (selectedTarget.matchWith(SelectedTarget.JS)) {
        js(jsCompilerType) {
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
    }

    if (selectedTarget.matchWith(SelectedTarget.NATIVE)) {
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
