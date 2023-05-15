plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "io.github.kotlin.multiplatform.template.gradle"

dependencies {
    implementation(libs.gradlePlugin.android)
    implementation(libs.gradlePlugin.dokka)
    implementation(libs.gradlePlugin.kotlin)
    implementation(libs.gradlePlugin.spotless)
    implementation(libs.gradlePlugin.dependencies.versions)
    implementation(libs.gradlePlugin.dependencies.versions.update)
}

gradlePlugin {
    plugins {
        // register("javaApplication") {
        //     id = "io.github.kotlin.multiplatform.template.gradle.application.java"
        //     implementationClass = "io.github.kotlin.multiplatform.template.gradle.application.JavaApplicationPlugin"
        // }
        // register("androidApplication") {
        //     id = "io.github.kotlin.multiplatform.template.gradle.application.android"
        //     implementationClass = "io.github.kotlin.multiplatform.template.gradle.application.AndroidApplicationPlugin"
        // }

        // Compile
        register("compile") {
            id = "io.github.kotlin.multiplatform.template.gradle.plugins.compile"
            implementationClass = "io.github.kotlin.multiplatform.template.gradle.plugins.CompilePlugin"
        }

        // Dependencies
        register("dependencies") {
            id = "io.github.kotlin.multiplatform.template.gradle.plugins.dependencies"
            implementationClass = "io.github.kotlin.multiplatform.template.gradle.plugins.DependenciesPlugin"
        }

        // Dokka
        register("dokka") {
            id = "io.github.kotlin.multiplatform.template.gradle.plugins.dokka"
            implementationClass = "io.github.kotlin.multiplatform.template.gradle.plugins.DokkaPlugin"
        }

        // Spotless
        register("javaSpotless") {
            id = "io.github.kotlin.multiplatform.template.gradle.plugins.spotless.java"
            implementationClass = "io.github.kotlin.multiplatform.template.gradle.plugins.SpotlessJavaPlugin"
        }
        register("androidSpotless") {
            id = "io.github.kotlin.multiplatform.template.gradle.plugins.spotless.android"
            implementationClass = "io.github.kotlin.multiplatform.template.gradle.plugins.SpotlessAndroidPlugin"
        }

        // Test
        register("test") {
            id = "io.github.kotlin.multiplatform.template.gradle.plugins.test"
            implementationClass = "io.github.kotlin.multiplatform.template.gradle.plugins.TestPlugin"
        }
    }
}
