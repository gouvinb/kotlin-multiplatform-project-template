package utils.extenstion

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import utils.ProjectInfo

fun CommonExtension<*, *, *, *, *>.configureAndroidCommonOptions() {
    compileSdk = ProjectInfo.COMPILE_SDK
    compileSdkExtension = ProjectInfo.COMPILE_SDK_EXTENSION
    buildToolsVersion = ProjectInfo.BUILD_TOOLS_VERSION
    ndkVersion = ProjectInfo.NDK_VERSION

    defaultConfig {
        minSdk = ProjectInfo.MIN_SDK
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = ProjectInfo.JAVA_VERSION
        targetCompatibility = ProjectInfo.JAVA_VERSION
    }
}

fun CommonExtension<*, *, *, *, *>.configureAndroidCompose(target: Project) {
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = target.libs.findVersion("androidx-compose-compiler").get().toString()
    }

    @Suppress("UnstableApiUsage")
    testOptions {
        unitTests {
            // For Robolectric
            isIncludeAndroidResources = true
        }
    }
}
