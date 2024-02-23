package utils.extenstion

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

fun Project.isFromIDE() = this.properties["android.injected.invoked.from.ide"] == "true"
fun Project.isNotFromIDE() = !isFromIDE()

val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

val Project.info: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("info")
