plugins {
    id("io.ashdavies.application")
}

kotlin {
    val commonMain by sourceSets.dependencies {
        implementation(libs.bundles.arkivanov.decompose)
        implementation(compose.materialIconsExtended)
    }

    val androidMain by sourceSets.dependencies {
        implementation(libs.androidx.compose.foundation)
        implementation(libs.androidx.core.splashscreen)
        implementation(libs.bundles.androidx.activity)
    }
}
