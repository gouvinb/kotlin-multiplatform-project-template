package plugins

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.TestedExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import utils.extenstion.configureAndroidCompose
import utils.extenstion.getPlugin
import utils.extenstion.libs
import kotlin.reflect.KClass

abstract class AndroidComposePlugin(
    private val testedExtensionKClass: KClass<out TestedExtension>,
    private val androidComponentsExtensionKClass: KClass<out AndroidComponentsExtension<*, *, *>>,
) : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            when {
                testedExtensionKClass == BaseAppModuleExtension::class && androidComponentsExtensionKClass == ApplicationAndroidComponentsExtension::class -> {
                    with(pluginManager) {
                        apply(libs.getPlugin("android-application").pluginId)
                    }

                    extensions.configure<BaseAppModuleExtension> {
                        configureAndroidCompose(target)
                    }
                }

                testedExtensionKClass == LibraryExtension::class && androidComponentsExtensionKClass == LibraryAndroidComponentsExtension::class -> {
                    with(pluginManager) {
                        apply(libs.getPlugin("android-library").pluginId)
                    }

                    extensions.configure<LibraryExtension> {
                        configureAndroidCompose(target)
                    }
                }
                else -> throw IllegalStateException(
                    "Only BaseAppModuleExtension and AndroidComponentsExtension, or LibraryExtension and LibraryAndroidComponentsExtension case are supported."
                )
            }

            target.tasks.withType<KotlinCompile>().configureEach {
                kotlinOptions {
                    freeCompilerArgs = freeCompilerArgs + buildComposeMetricsParameters()
                }
            }
            dependencies {
                val bom = libs.findLibrary("androidx-compose-bom").get()
                add("implementation", platform(bom))
                add("androidTestImplementation", platform(bom))
            }
        }
    }

    private fun Project.buildComposeMetricsParameters(): List<String> {
        val metricParameters = mutableListOf<String>()
        val enableMetricsProvider = project.providers.gradleProperty("enableComposeCompilerMetrics")
        val relativePath = projectDir.relativeTo(rootDir)
        val buildDir = layout.buildDirectory.get().asFile
        val enableMetrics = (enableMetricsProvider.orNull == "true")
        if (enableMetrics) {
            val metricsFolder = buildDir.resolve("compose-metrics").resolve(relativePath)
            metricParameters.add("-P")
            metricParameters.add(
                "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" + metricsFolder.absolutePath
            )
        }

        val enableReportsProvider = project.providers.gradleProperty("enableComposeCompilerReports")
        val enableReports = (enableReportsProvider.orNull == "true")
        if (enableReports) {
            val reportsFolder = buildDir.resolve("compose-reports").resolve(relativePath)
            metricParameters.add("-P")
            metricParameters.add(
                "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" + reportsFolder.absolutePath
            )
        }
        return metricParameters.toList()
    }
}

class AndroidComposeApplicationPlugin : AndroidComposePlugin(
    BaseAppModuleExtension::class,
    ApplicationAndroidComponentsExtension::class,
)

class AndroidComposeLibraryPlugin : AndroidComposePlugin(
    LibraryExtension::class,
    LibraryAndroidComponentsExtension::class,
)
