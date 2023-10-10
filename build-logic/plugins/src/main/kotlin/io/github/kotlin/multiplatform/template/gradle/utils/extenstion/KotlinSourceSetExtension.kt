package io.github.kotlin.multiplatform.template.gradle.utils.extenstion

import io.github.kotlin.multiplatform.template.gradle.utils.properties.SelectedTarget
import io.github.kotlin.multiplatform.template.gradle.utils.SystemInfo
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

/**
 * Creates a source set for a directory that isn't already a built-in platform. Use this to create
 * custom shared directories like `nonJvmMain` or `unixMain`.
 */
fun NamedDomainObjectContainer<KotlinSourceSet>.createSourceSet(
    name: String,
    parent: KotlinSourceSet? = null,
    children: List<String> = listOf(),
): KotlinSourceSet {
    val result = create(name)

    if (parent != null) {
        result.dependsOn(parent)
    }

    val suffix = when {
        name.endsWith("Main") -> "Main"
        name.endsWith("Test") -> "Test"
        else -> error("unexpected source set name: $name")
    }

    children
        .asSequence()
        .map { get("$it$suffix") }
        .forEach { it.dependsOn(result) }

    return result
}

fun NamedDomainObjectContainer<KotlinSourceSet>.createSourceSet(
    name: String,
    parent: KotlinSourceSet? = null,
    children: List<String> = listOf(),
    block: (KotlinSourceSet) -> Unit,
) = createSourceSet(name, parent, children).also(block)

fun NamedDomainObjectContainer<KotlinSourceSet>.configureSourceSetHierarchy() {
    val selectedTarget = SelectedTarget.getFromProperty()

    val commonMain = get("commonMain")
    val commonTest = get("commonTest")

    val hashFunctions = create("hashFunctions") {
        dependsOn(commonMain)
    }

    val nonAppleMain = create("nonAppleMain") {
        dependsOn(hashFunctions)
    }
    val nonAppleTest = create("nonAppleTest") {
        dependsOn(hashFunctions)
    }

    val nonJvmMain = create("nonJvmMain") {
        dependsOn(hashFunctions)
        dependsOn(commonMain)
    }
    val nonJvmTest = create("nonJvmTest") {
        dependsOn(commonTest)
    }

    if (selectedTarget.matchWith(SelectedTarget.JVM)) {
        getByName("jvmMain") {
        }
        getByName("jvmTest") {
            kotlin.srcDir("src/jvmTest/hashFunctions")
            dependencies {}
        }
    }

    if (selectedTarget.matchWith(SelectedTarget.JS)) {
        getByName("jsMain") {
            dependsOn(nonJvmMain)
            dependsOn(nonAppleMain)
        }
        getByName("jsTest") {
            dependsOn(nonJvmTest)
        }
    }

    if (selectedTarget.matchWith(SelectedTarget.NATIVE)) {
        createSourceSet("nativeMain", parent = nonJvmMain) { nativeMain ->
            createSourceSet("mingwMain", parent = nativeMain, children = SystemInfo.mingwTargets) { mingwMain ->
                mingwMain.dependsOn(nonAppleMain)
            }
            createSourceSet("unixMain", parent = nativeMain) { unixMain ->
                createSourceSet("linuxMain", parent = unixMain, children = SystemInfo.linuxTargets) { linuxMain ->
                    linuxMain.dependsOn(nonAppleMain)
                }
                createSourceSet("appleMain", parent = unixMain, children = SystemInfo.appleTargets)
            }
        }
        createSourceSet("nativeTest", parent = nonJvmTest) { nativeTest ->
            createSourceSet("mingwTest", parent = nativeTest, children = SystemInfo.mingwTargets) { mingwTest ->
                mingwTest.dependsOn(nonAppleTest)
            }
            createSourceSet("unixTest", parent = nativeTest) { unixTest ->
                createSourceSet("linuxTest", parent = unixTest, children = SystemInfo.linuxTargets) { linuxTest ->
                    linuxTest.dependsOn(nonAppleTest)
                }
                createSourceSet("appleTest", parent = unixTest, children = SystemInfo.appleTargets)
            }
        }
    }
}
