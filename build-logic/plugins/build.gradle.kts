import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.nio.charset.StandardCharsets

plugins {
    `kotlin-dsl`
}

group = "io.github.kotlin.multiplaform.template.gradle"

java {
    sourceCompatibility = JavaVersion.VERSION_19
    targetCompatibility = JavaVersion.VERSION_19
}

dependencies {
    implementation(libs.android.gradlePlugin)
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

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs = freeCompilerArgs + "-Xjvm-default=all"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"

    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = StandardCharsets.UTF_8.toString()
    sourceCompatibility = JavaVersion.VERSION_1_8.toString()
    targetCompatibility = JavaVersion.VERSION_1_8.toString()
}

tasks.withType<Test>().configureEach {
    testLogging {
        events(
            TestLogEvent.STARTED,
            TestLogEvent.PASSED,
            TestLogEvent.SKIPPED,
            TestLogEvent.FAILED
        )
        exceptionFormat = TestExceptionFormat.FULL
        showStandardStreams = false
    }
}
