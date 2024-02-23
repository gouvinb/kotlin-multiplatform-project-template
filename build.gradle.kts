buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        classpath(libs.gradlePlugin.android)
        classpath(libs.gradlePlugin.kotlin)
        classpath(libs.gradlePlugin.dokka)
        classpath(libs.gradlePlugin.spotless)
        classpath(libs.gradlePlugin.dependencies.versions)
        classpath(libs.gradlePlugin.dependencies.versions.update)
    }
}

plugins {
    `version-catalog`
}
