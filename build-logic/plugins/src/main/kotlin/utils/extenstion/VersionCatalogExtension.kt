package utils.extenstion

import org.gradle.api.artifacts.VersionCatalog

fun VersionCatalog.getVersion(libraryName: String) = findVersion(libraryName).get()

fun VersionCatalog.getLibrary(libraryName: String) = findLibrary(libraryName).get().get()

fun VersionCatalog.getPlugin(pluginName: String) = findPlugin(pluginName).get().get()

fun VersionCatalog.getBundle(bundleName: String) = findBundle(bundleName).get()

fun VersionCatalog.getStrictVersion(libraryName: String) = findVersion(libraryName).get().strictVersion
