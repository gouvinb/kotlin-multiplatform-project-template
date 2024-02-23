package utils.extenstion

import utils.properties.Environment
import utils.properties.SelectedTarget
import org.gradle.api.Action
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.targets.js.testing.KotlinJsTest
import utils.ProjectInfo

@Suppress("unused")
fun KotlinMultiplatformExtension.configureOrCreateNativePlatforms(
    jsCompilerType: KotlinJsCompilerType = KotlinJsCompilerType.IR,
    isAndroidProject: Boolean = false,
) = ArrayList<KotlinTarget>().apply {
    val selectedTarget = SelectedTarget.getFromProperty()
    val environment = Environment.getFromProperty()

    if (selectedTarget.matchWith(SelectedTarget.ANDROID) && isAndroidProject) {
        androidTarget() {
        }
            .apply { add(this) }
    }

    if (selectedTarget.matchWith(SelectedTarget.JVM)) {
        jvm {
            if (!isAndroidProject) withJava()
            jvmToolchain(ProjectInfo.JAVA_VERSION.majorVersion.toInt())
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
        iosArm64().apply { add(this) }
        iosSimulatorArm64().apply { add(this) }
        tvosArm64().apply { add(this) }
        tvosSimulatorArm64().apply { add(this) }
        watchosArm64().apply { add(this) }
        watchosSimulatorArm64().apply { add(this) }
        // Required to generate tests tasks: https://youtrack.jetbrains.com/issue/KT-26547
        linuxX64().apply { add(this) }
        linuxArm64().apply { add(this) }
        macosArm64().apply { add(this) }
        mingwX64().apply { add(this) }
    }
}
