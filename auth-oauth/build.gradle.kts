    plugins {
    id("io.ashdavies.library")
    id("android-manifest")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.bundles.ktor.client)
                implementation(libs.bundles.ktor.server)
            }
        }
    }
}
