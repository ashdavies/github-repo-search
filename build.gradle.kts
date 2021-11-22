// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION")

import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

buildscript {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    dependencies {
        classpath(libs.android.gradlePlugin)
        classpath(libs.apache.batikExt)
        classpath(libs.apolloGraphQl.apolloGradlePlugin)
        classpath(libs.jetbrains.kotlin.gradlePlugin)
        classpath(libs.sqlDelight.kotlinGradlePlugin)
    }
}

plugins {
    alias(libs.plugins.anvil)
    alias(libs.plugins.gradle.doctor)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.versions)
}

allprojects {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

subprojects {
    tasks.withType<KotlinCompile<*>> {
        kotlinOptions.freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xallow-result-return-type",
            "-XXLanguage:+InlineClasses",
            "-Xmulti-platform"
        )
    }
}

doctor {
    disallowCleanTaskDependencies.set(false)
}
