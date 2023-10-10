package io.github.kotlin.multiplatform.template.gradle.utils.extenstion

import io.github.kotlin.multiplatform.template.gradle.utils.properties.Environment
import io.github.kotlin.multiplatform.template.gradle.utils.properties.SelectedTarget
import org.gradle.api.Action
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.targets.js.testing.KotlinJsTest

@Suppress("unused")
fun KotlinMultiplatformExtension.configureOrCreateNativePlatforms(
    jsCompilerType: KotlinJsCompilerType = KotlinJsCompilerType.IR,
) = ArrayList<KotlinTarget>().apply {
    val selectedTarget = SelectedTarget.getFromProperty()
    val environment = Environment.getFromProperty()

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
                testTask(Action<KotlinJsTest> {
                    useMocha {
                        timeout = "30s"
                    }
                })
            }
            if (environment == Environment.LOCAL) {
                browser {
                }
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
        // watchosX86().apply { add(this) }
        watchosX64().apply { add(this) }
        watchosSimulatorArm64().apply { add(this) }
        // Required to generate tests tasks: https://youtrack.jetbrains.com/issue/KT-26547
        linuxX64().apply { add(this) }
        macosX64().apply { add(this) }
        macosArm64().apply { add(this) }
        mingwX64().apply { add(this) }
    }
}
