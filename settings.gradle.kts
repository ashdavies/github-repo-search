dependencyResolutionManagement {
    //repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
}

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "playground"

include(
    ":android",
    ":cloud-aggregator",
    ":cloud-functions",
    ":common-app",
    ":compose-local",
    ":local-storage",
    ":notion-console",
    ":version-catalog"
)

includeBuild("build-plugins")

enableFeaturePreview("VERSION_CATALOGS")
