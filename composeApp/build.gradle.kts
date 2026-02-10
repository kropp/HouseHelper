import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import java.util.Locale

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.androidKmpLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

val javaFxClassifier = run {
    val osName = System.getProperty("os.name").lowercase(Locale.getDefault())
    val osArch = System.getProperty("os.arch").lowercase(Locale.getDefault())
    when {
        osName.contains("linux") && osArch.contains("aarch64") -> "linux-aarch64"
        osName.contains("linux") -> "linux"
        osName.contains("windows") -> "win"
        osName.contains("mac") && osArch.contains("aarch64") -> "mac-aarch64"
        osName.contains("mac") -> "mac"
        else -> throw IllegalStateException("Unsupported OS: $osName")
    }
}

kotlin {
    androidLibrary {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }

        namespace = "com.kotlinconf.workshop.househelper.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()

        minSdk = libs.versions.android.minSdk.get().toInt()

        androidResources.enable = true

        withHostTest {
            isIncludeAndroidResources = true
        }
        withDeviceTest {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            execution = "HOST"
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm("desktop")

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName = "houseHelper"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "houseHelper.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        val desktopMain by getting

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.compose.ui.test)
        }

        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.ktor.client.okhttp)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.compose.material3)
            implementation(libs.compose.material.icons.core)

            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.navigation3.ui)
            implementation(libs.androidx.lifecycle.viewmodel.navigation3)
            implementation(libs.kotlinx.serialization)

            implementation(libs.koin.compose.viewmodel)

            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor3)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.okhttp)

            // JavaFx for video player
            libs.bundles.javafx.get().forEach {
                api(it) { artifact { classifier = javaFxClassifier } }
            }
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling)
}
