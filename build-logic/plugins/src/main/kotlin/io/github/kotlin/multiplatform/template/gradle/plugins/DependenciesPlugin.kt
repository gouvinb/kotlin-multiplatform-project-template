package io.github.kotlin.multiplatform.template.gradle.plugins

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import nl.littlerobots.vcu.plugin.VersionCatalogUpdateExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

class DependenciesPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.github.ben-manes.versions")
            pluginManager.apply("nl.littlerobots.version-catalog-update")

            // https://github.com/ben-manes/gradle-versions-plugin
            tasks.withType<DependencyUpdatesTask>().configureEach {
                fun isNonStable(version: String): Boolean {
                    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
                    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
                    val isStable = stableKeyword || regex.matches(version)
                    return isStable.not()
                }

                rejectVersionIf {
                    isNonStable(candidate.version)
                }
            }

            extensions.configure<VersionCatalogUpdateExtension> {
                keep {
                    version
                }
            }
        }
    }
}
