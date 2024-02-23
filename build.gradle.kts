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
    }
}

plugins {
    `version-catalog`
}
