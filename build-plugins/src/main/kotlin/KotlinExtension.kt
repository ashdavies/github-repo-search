import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

public object Playground {

    const val jvmTarget = "11"

    val freeCompilerArgs = listOf(
        "-opt-in=kotlin.RequiresOptIn",
        "-Xallow-result-return-type",
        "-Xmulti-platform"
    )
}

internal fun CommonExtension<*, *, *, *>.configureCommon() {
    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    defaultConfig {
        compileSdk = 33
        minSdk = 21
    }

    sourceSets.configureEach {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }
}

internal fun KotlinProjectExtension.configureKotlinProject(target: Project) = target.run {
    sourceSets.all { languageSettings.optIn("kotlin.RequiresOptIn") }
    explicitApiWarning()
}

@Suppress("UNUSED_VARIABLE")
@OptIn(ExperimentalComposeLibrary::class)
internal fun KotlinMultiplatformExtension.configureKotlinMultiplatform(target: Project) = target.run {
    android()
    jvm()

    val compose = ComposePlugin.Dependencies

    val commonMain by sourceSets.dependencies {
        implementation(compose.foundation)
        implementation(compose.material3)
        implementation(compose.runtime)
        implementation(compose.uiTooling)
        implementation(compose.ui)

        implementation(libs.bundles.arkivanov.decompose)
        implementation(libs.bundles.jetbrains.kotlinx)
        implementation(libs.oolong)
    }

    val commonTest by sourceSets.dependencies {
        implementation(libs.bundles.jetbrains.kotlin.test)
    }

    val androidMain by sourceSets.dependencies {
        implementation(libs.androidx.annotation)
        implementation(libs.androidx.core.ktx)
        implementation(libs.google.android.material)
        implementation(libs.jetbrains.kotlinx.coroutines.android)
    }

    val androidTest by sourceSets.getting {
        val androidAndroidTestRelease by sourceSets.getting
        dependsOn(androidAndroidTestRelease)
    }

    val androidTestFixtures by sourceSets.getting
    val androidTestFixturesDebug by sourceSets.getting
    val androidTestFixturesRelease by sourceSets.getting

    val jvmMain by sourceSets.dependencies {
        implementation(compose.desktop.currentOs)
        implementation(libs.jetbrains.kotlinx.coroutines.swing)
    }
}

public fun NamedDomainObjectCollection<KotlinSourceSet>.dependencies(
    configure: KotlinDependencyHandler.() -> Unit
) = getting { dependencies(configure) }
