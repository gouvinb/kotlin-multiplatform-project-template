import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import nl.littlerobots.vcu.plugin.versionCatalogUpdate
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.gradle.api.tasks.testing.logging.TestLogEvent.STARTED
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.nio.charset.StandardCharsets

plugins {
    `version-catalog`
}

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        classpath(libs.android.gradlePlugin)
        classpath(libs.kotlin.gradlePlugin)
        classpath(libs.dokka.gradlePlugin)
        classpath(libs.spotless.gradlePlugin)
        classpath(libs.dependencies.versions.gradlePlugin)
        classpath(libs.dependencies.versions.update.gradlePlugin)
    }
}

apply(plugin = "com.github.ben-manes.versions")
apply(plugin = "nl.littlerobots.version-catalog-update")

// https://github.com/ben-manes/gradle-versions-plugin
tasks.withType<DependencyUpdatesTask> {
    fun isNonStable(version: String): Boolean {
        val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
        val regex = "^[0-9,.v-]+(-r)?$".toRegex()
        val isStable = stableKeyword || regex.matches(version)
        return isStable.not()
    }

    rejectVersionIf {
        isNonStable(candidate.version)
    }
}

versionCatalogUpdate {
    keep {
        version
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }

    tasks.withType(DokkaTask::class).configureEach {
        dokkaSourceSets.configureEach {
            // includes.from("README.md")
            // samples.from("samples/**.kt")

            reportUndocumented.set(true)
            skipDeprecated.set(true)
            jdkVersion.set(11)
            perPackageOption {
                matchingRegex.set("io\\.github\\.kotlin\\.multiplaform\\.template\\..*\\.internal.*")
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
                """.trimMargin()
                )
            )
        }
    }
}

subprojects {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_11.toString()
            @Suppress("SuspiciousCollectionReassignment")
            freeCompilerArgs += "-Xjvm-default=all"
        }
    }

    tasks.withType<JavaCompile> {
        options.encoding = StandardCharsets.UTF_8.toString()
        sourceCompatibility = JavaVersion.VERSION_11.toString()
        targetCompatibility = JavaVersion.VERSION_11.toString()
    }

    tasks.withType<Test> {
        testLogging {
            events(STARTED, PASSED, SKIPPED, FAILED)
            exceptionFormat = TestExceptionFormat.FULL
            showStandardStreams = false
        }
    }
}
