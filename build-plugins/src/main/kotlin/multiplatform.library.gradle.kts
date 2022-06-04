import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    id("org.jetbrains.compose")

    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

extensions.configure<LibraryExtension>(CommonExtension<*, *, *, *>::configureCommon)

extensions.configure<KotlinMultiplatformExtension> {
    configureKotlinMultiplatform(project)
    configureKotlinProject(project)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs += Playground.freeCompilerArgs
    kotlinOptions.jvmTarget = Playground.jvmTarget
}
