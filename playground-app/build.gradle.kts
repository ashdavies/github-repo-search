plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.base"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.appCheck.appCheckClient)

        implementation(projects.cloudBackend.httpClient)
        implementation(projects.localStorage)

        implementation(libs.bundles.ktor.client)
        implementation(libs.jetbrains.kotlinx.collections.immutable)
    }

    androidMain.dependencies {
        implementation(projects.firebaseCompose)

        implementation(libs.bundles.androidx.activity)
        implementation(libs.jetbrains.kotlinx.coroutines.play)

        with(libs.google) {
            implementation(accompanist.placeholderMaterial)
            implementation(accompanist.systemuicontroller)
        }
    }
}
