pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ComposePlayground"
include(":app-mobile")
include(":feature-ghcrange-mobile")
include(":feature-ghcrange-mobile-api")
include(":core-designsystem")
include(":feature-maplibremaps")
include(":feature-maplibremaps-api")
include(":core-arch")
include(":feature-dashboard")
include(":feature-dashboard-api")
