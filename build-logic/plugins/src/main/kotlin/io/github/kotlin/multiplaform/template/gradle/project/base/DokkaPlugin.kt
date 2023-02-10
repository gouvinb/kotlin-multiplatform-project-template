package io.github.kotlin.multiplaform.template.gradle.project.base

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.dokka.gradle.DokkaTask

class DokkaPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.dokka")
            tasks.withType(DokkaTask::class).configureEach {
                dokkaSourceSets.configureEach {
                    // includes.from("README.md")
                    // samples.from("samples/**.kt")

                    reportUndocumented.set(true)
                    skipDeprecated.set(true)
                    jdkVersion.set(JavaVersion.VERSION_11.toString().toInt())
                    perPackageOption {
                        matchingRegex.set("""io\.github\.kotlin\.multiplaform\.template\..*\.internal.*""")
                        suppress.set(true)
                    }
                }

                if (name == "dokkaHtml") {
                    outputDirectory.set(file("${rootDir}/docs/$version/${project.name}"))
                    pluginsMapConfiguration.set(
                        mapOf(
                            "org.jetbrains.dokka.base.DokkaBase" to """
                            |{
                            |  "customStyleSheets": [
                            |    "${rootDir.toString().replace('\\', '/')}/docs/css/dokka-logo.css"
                            |  ],
                            |  "customAssets" : [
                            |    "${rootDir.toString().replace('\\', '/')}/docs/images/ic_project.svg"
                            |  ]
                            |}
                            """.trimMargin(),
                        )
                    )
                }
            }
        }
    }
}
