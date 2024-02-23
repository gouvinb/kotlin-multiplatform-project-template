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
import utils.extenstion.configureAndroidApplicationComponents
import utils.extenstion.configureAndroidApplicationOptions
import utils.extenstion.configureAndroidCommonOptions
import utils.extenstion.configureAndroidLibraryComponents
import utils.extenstion.configureAndroidLibraryOptions
import utils.extenstion.getBundle
import utils.extenstion.getLibrary
import utils.extenstion.getPlugin
import utils.extenstion.libs
import kotlin.reflect.KClass

abstract class AndroidPlugin(
    private val testedExtensionKClass: KClass<out TestedExtension>,
    private val androidComponentsExtensionKClass: KClass<out AndroidComponentsExtension<*, *, *>>,
) : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            when {
                testedExtensionKClass == BaseAppModuleExtension::class && androidComponentsExtensionKClass == ApplicationAndroidComponentsExtension::class -> {
                    with(pluginManager) {
                        apply(libs.getPlugin("android-application").pluginId)
                        apply(libs.getPlugin("kotlin-multiplatform").pluginId)
                    }

                    extensions.configure<BaseAppModuleExtension> {
                        configureAndroidCommonOptions()
                        configureAndroidApplicationOptions()
                    }
                    extensions.configure<ApplicationAndroidComponentsExtension> {
                        configureAndroidApplicationComponents()
                    }
                }

                testedExtensionKClass == LibraryExtension::class && androidComponentsExtensionKClass == LibraryAndroidComponentsExtension::class -> {
                    with(pluginManager) {
                        apply(libs.getPlugin("android-library").pluginId)
                    }

                    extensions.configure<LibraryExtension> {
                        configureAndroidCommonOptions()
                        configureAndroidLibraryOptions(target)
                    }
                    extensions.configure<LibraryAndroidComponentsExtension> {
                        configureAndroidLibraryComponents(target)
                    }
                }
                else -> throw IllegalStateException(
                    "Only BaseAppModuleExtension and AndroidComponentsExtension, or LibraryExtension and LibraryAndroidComponentsExtension case are supported."
                )
            }

            dependencies {
                "coreLibraryDesugaring"(libs.getLibrary("android-desugar-jdk"))

                "androidTestImplementation"(libs.getBundle("instrumentation.test"))
                "testImplementation"(libs.getBundle("unit.test"))

                "debugImplementation"(libs.getLibrary("androidx.compose.ui.testManifest"))
            }
        }
    }
}

class AndroidApplicationPlugin : AndroidPlugin(
    BaseAppModuleExtension::class,
    ApplicationAndroidComponentsExtension::class,
)

class AndroidLibraryPlugin : AndroidPlugin(
    LibraryExtension::class,
    LibraryAndroidComponentsExtension::class,
)
