import androidx.build.gradle.gcpbuildcache.GcpBuildCache
import androidx.build.gradle.gcpbuildcache.GcpBuildCacheServiceFactory

apply(from = "repositories.gradle.kts")

plugins {
    id("androidx.build.gradle.gcpbuildcache") version "1.0.0-beta01"
    id("com.google.cloud.tools.jib") version "3.3.1" apply false
    id("com.gradle.enterprise") version "3.9"
}

buildCache {
    registerBuildCacheService(GcpBuildCache::class, GcpBuildCacheServiceFactory::class)

    remote(GcpBuildCache::class) {
        bucketName = "playground-build-cache"
        projectId = "playground-1a136"
        isPush = true
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishAlways()
    }
}

include(
    ":app-check:app-check-client",
    ":app-check:app-check-compose",
    ":app-check:app-check-sdk",
    ":app-launcher:android",
    ":app-launcher:common",
    ":app-launcher:desktop",
    ":auth-oauth",
    ":cloud-functions",
    ":cloud-run",
    ":compose-constructor:plugin-common",
    // ":compose-constructor:plugin-ide",
    // ":compose-constructor:plugin-native",
    ":compose-constructor:plugin-runtime",
    ":compose-locals",
    ":dominion-app",
    ":events-aggregator",
    ":events-app",
    ":events-function",
    ":firebase-compose",
    ":google-cloud:cloud-run",
    ":google-cloud:gh-oidc",
    ":local-remote",
    ":local-storage",
    ":notion-console",
    ":paging-compose",
    ":parcelable-support",
    ":playground-app",
    ":version-catalog",
)

includeBuild("build-plugins")

includeBuild("compose-constructor/plugin-gradle") {
    dependencySubstitution {
        substitute(module("io.ashdavies.playground:plugin-gradle")).using(project(":"))
    }
}

rootProject.name = "playground"
