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
        //     id = "application.java"
        //     implementationClass = "application.JavaApplicationPlugin"
        // }

        // Android
        register("androidApplication") {
            id = "plugin.android.application"
            implementationClass = "plugins.AndroidApplicationPlugin"
        }
        register("androidLibrary") {
            id = "plugin.android.library"
            implementationClass = "plugins.AndroidLibraryPlugin"
        }

        register("androidComposeApplication") {
            id = "plugin.android.application.compose"
            implementationClass = "plugins.AndroidComposeApplicationPlugin"
        }
        register("androidComposeLibrary") {
            id = "plugin.android.library.compose"
            implementationClass = "plugins.AndroidComposeLibraryPlugin"
        }

        // Compile
        register("compileJava") {
            id = "plugins.compile.java"
            implementationClass = "plugins.CompileJavaPlugin"
        }
        register("compileKotlin") {
            id = "plugins.compile.kotlin"
            implementationClass = "plugins.CompileKotlinPlugin"
        }

        // Dependencies
        register("dependencies") {
            id = "plugins.dependencies"
            implementationClass = "plugins.DependenciesPlugin"
        }

        // Dokka
        register("dokka") {
            id = "plugins.dokka"
            implementationClass = "plugins.DokkaPlugin"
        }

        // Spotless
        register("javaSpotless") {
            id = "plugins.spotless.java"
            implementationClass = "plugins.SpotlessJavaPlugin"
        }
        register("androidSpotless") {
            id = "plugins.spotless.android"
            implementationClass = "plugins.SpotlessAndroidPlugin"
        }

        // Test
        register("test") {
            id = "plugins.test"
            implementationClass = "plugins.TestPlugin"
        }
    }
}
