import java.util.Locale

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    dependencies {
        implementation(projects.shared)
        implementation(compose.desktop.currentOs)

        implementation(libs.koin.compose.viewmodel)
    }
}

compose.desktop {
    application {
        mainClass = "com.kotlinconf.workshop.househelper.MainKt"
    }
}
