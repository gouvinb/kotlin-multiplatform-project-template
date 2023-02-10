import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.nio.charset.StandardCharsets

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "io.github.kotlin.multiplaform.template.gradle"

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.dokka.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.spotless.gradlePlugin)
}

gradlePlugin {
    plugins {
        // register("javaApplication") {
        //     id = "io.github.kotlin.multiplaform.template.gradle.project.application.java"
        //     implementationClass = "io.github.kotlin.multiplaform.template.gradle.project.application.JavaApplicationPlugin"
        // }
        // register("androidApplication") {
        //     id = "io.github.kotlin.multiplaform.template.gradle.project.application.android"
        //     implementationClass = "io.github.kotlin.multiplaform.template.gradle.project.application.AndroidApplicationPlugin"
        // }

        // Dokka
        register("dokka") {
            id = "io.github.kotlin.multiplaform.template.gradle.project.base.dokka"
            implementationClass = "io.github.kotlin.multiplaform.template.gradle.project.base.DokkaPlugin"
        }

        // Spotless
        register("javaSpotless") {
            id = "io.github.kotlin.multiplaform.template.gradle.project.base.spotless.java"
            implementationClass = "io.github.kotlin.multiplaform.template.gradle.project.base.SpotlessJavaPlugin"
        }
        register("androidSpotless") {
            id = "io.github.kotlin.multiplaform.template.gradle.project.base.spotless.android"
            implementationClass = "io.github.kotlin.multiplaform.template.gradle.project.base.SpotlessAndroidPlugin"
        }
    }
}
