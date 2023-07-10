plugins {
    id("io.ashdavies.default")
    id("kotlin-parcelize")
}

android {
    namespace = "io.ashdavies.common"
}

kotlin {
    commonMain.dependencies {
        implementation(compose.materialIconsExtended)

        with(projects) {
            implementation(dominionApp)
            implementation(eventsApp)
            implementation(galleryApp)
            implementation(ratingsApp)
        }

        implementation(libs.arkivanov.parcelable)
        implementation(libs.slack.circuit.foundation)
    }

    commonTest.dependencies {
        implementation(libs.slack.circuit.test)
    }

    androidMain.dependencies {
        implementation(libs.androidx.activity.compose)
        implementation(libs.androidx.activity.ktx)
    }
}
