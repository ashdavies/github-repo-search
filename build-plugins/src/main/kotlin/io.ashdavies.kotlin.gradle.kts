import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("plugin.serialization")
    kotlin("multiplatform")
}

kotlin {
    explicitApi()
    jvm()

    commonMain.dependencies {
        with(libs.jetbrains.kotlinx) {
            implementation(collections.immutable)
            implementation(coroutines.core)
            implementation(datetime)
            implementation(serialization.core)
            implementation(serialization.json)
        }
    }

    commonTest.dependencies {
        implementation(libs.app.cash.turbine)

        with(libs.jetbrains.kotlin) {
            implementation(test)
            implementation(test.annotations)
            implementation(test.common)
            implementation(test.junit)
        }

        with(libs.jetbrains.kotlinx) {
            implementation(coroutines.core)
            implementation(coroutines.test)
            implementation(datetime)
            implementation(serialization.core)
            implementation(serialization.json)
        }
    }

    jvmMain.dependencies {
        implementation(libs.jetbrains.kotlinx.coroutines.swing)
    }

    val jvmTargetVersion = libs.versions.kotlin.jvmTarget.get()

    tasks.withType<KotlinCompile> {
        kotlinOptions.freeCompilerArgs += "-Xexpect-actual-classes"
        kotlinOptions.jvmTarget = jvmTargetVersion
    }

    jvmToolchain(jvmTargetVersion.toInt())
}
