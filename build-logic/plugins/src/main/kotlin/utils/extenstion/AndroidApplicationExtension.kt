package utils.extenstion

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.ManagedVirtualDevice
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.invoke
import utils.ProjectInfo
import utils.model.DeviceConfig

fun ApplicationExtension.configureAndroidApplicationOptions() {
    defaultConfig {
        targetSdk = ProjectInfo.TARGET_SDK
    }

    val pixel5 = DeviceConfig("Pixel 5", 30, "aosp-atd")
    val pixel7 = DeviceConfig("Pixel 7", 31, "aosp")
    val pixelC = DeviceConfig("Pixel C", 30, "aosp-atd")

    val allDevices = listOf(pixel5, pixel7, pixelC)
    val ciDevices = listOf(pixel5, pixelC)

    @Suppress("UnstableApiUsage")
    testOptions {
        managedDevices {
            devices {
                allDevices.forEach { deviceConfig ->
                    maybeCreate("$deviceConfig", ManagedVirtualDevice::class.java).apply {
                        device = deviceConfig.device
                        apiLevel = deviceConfig.apiLevel
                        systemImageSource = deviceConfig.systemImageSource
                    }
                }
            }
            groups {
                maybeCreate("ci").apply {
                    ciDevices.forEach { deviceConfig ->
                        targetDevices.add(devices["$deviceConfig"])
                    }
                }
            }
        }
    }
}

fun ApplicationAndroidComponentsExtension.configureAndroidApplicationComponents() {
}
