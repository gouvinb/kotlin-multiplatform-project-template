package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import utils.ProjectInfo

class CompileJavaPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<JavaPluginExtension> {
                // Up to Java 11 APIs are available through desugaring
                // https://developer.android.com/studio/write/java11-minimal-support-table
                sourceCompatibility = ProjectInfo.JAVA_VERSION
                targetCompatibility = ProjectInfo.JAVA_VERSION
            }
        }
    }
}
