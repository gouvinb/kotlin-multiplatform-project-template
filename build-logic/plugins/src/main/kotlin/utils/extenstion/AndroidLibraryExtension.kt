package utils.extenstion

import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project

fun LibraryExtension.configureAndroidLibraryOptions(target: Project) {
    buildTypes {
        maybeCreate("debug").apply {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                target.rootProject.file("config/proguard/proguard-rules.pro")
            )
        }

        getByName("release").apply {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                target.rootProject.file("config/proguard/proguard-rules.pro")
            )
        }
    }
}

fun LibraryAndroidComponentsExtension.configureAndroidLibraryComponents(target: Project) {
    beforeVariants { variant ->
        variant.enableAndroidTest = variant.enableAndroidTest && (
                target.projectDir.resolve("src/androidTest").exists() || target.projectDir.resolve("src/androidInstrumentedTest").exists()
                )
        variant.enableUnitTest = variant.enableUnitTest && (
                target.projectDir.resolve("src/test").exists() || target.projectDir.resolve("src/androidUnitTest").exists()
                )
    }
}
