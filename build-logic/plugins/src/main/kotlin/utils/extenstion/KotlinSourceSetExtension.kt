package utils.extenstion

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import utils.SystemInfo

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

/**
 * Here's the main hierarchy of variants. Any `expect` functions in one level of the tree are
 * `actual` functions in a (potentially indirect) child node.
 *
 * ```
 *   common
 *   |-- jvm
 *   |-- js
 *   '-- native
 *       |- unix
 *       |   |-- apple
 *       |   |   |-- iosArm64
 *       |   |   |-- macosArm64
 *       |   |   |-- tvosArm64
 *       |   |   '-- watchosArm64
 *       |   '-- linux
 *       |       |-- linuxArm64
 *       |       '-- linuxX64
 *       '-- mingw
 *           '-- mingwX64
 * ```
 *
 * The `nonJvm` source set excludes that platform.
 *
 * The `hashFunctions` source set builds on all platforms. It ships as a main source set on non-JVM
 * platforms and as a test source set on the JVM platform.
 * ```
 *
 * The `nonJvm` source set excludes that platform.
 *
 * The `hashFunctions` source set builds on all platforms. It ships as a main source set on non-JVM
 * platforms and as a test source set on the JVM platform.
 */
fun NamedDomainObjectContainer<KotlinSourceSet>.configureSourceSetHierarchy(
    enableAndroidProject: Boolean = false,
    enableJvmProject: Boolean = false,
    enableJsProject: Boolean = false,
    enableNativeProject: Boolean = false,
) {
    val commonMain = get("commonMain")
    val commonTest = get("commonTest")

    if (enableAndroidProject) {
        getByName("androidMain") {
        }
        getByName("androidUnitTest") {
            kotlin.srcDir("src/jvmTest/hashFunctions")
            dependencies {}
        }
        getByName("androidInstrumentedTest") {
            kotlin.srcDir("src/jvmTest/hashFunctions")
            dependencies {}
        }
    }

    if (enableJvmProject) {
        getByName("jvmMain") {
        }
        getByName("jvmTest") {
            kotlin.srcDir("src/jvmTest/hashFunctions")
            dependencies {}
        }
    }

    if (enableJsProject || enableNativeProject) {
        val hashFunctionsMain = create("hashFunctionsMain") {
            dependsOn(commonMain)
        }
        val hashFunctionsTest = create("hashFunctionsTest") {
            dependsOn(commonTest)
        }

        val nonAppleMain = create("nonAppleMain") {
            dependsOn(hashFunctionsMain)
        }
        val nonAppleTest = create("nonAppleTest") {
            dependsOn(hashFunctionsTest)
        }

        val nonJvmMain = create("nonJvmMain") {
            dependsOn(hashFunctionsMain)
            dependsOn(commonMain)
        }
        val nonJvmTest = create("nonJvmTest") {
            dependsOn(commonTest)
        }

        if (enableJsProject) {
            getByName("jsMain") {
                dependsOn(nonJvmMain)
                dependsOn(nonAppleMain)
            }
            getByName("jsTest") {
                dependsOn(nonJvmTest)
                dependsOn(nonAppleTest)
            }
        }

        if (enableNativeProject) {
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
}
