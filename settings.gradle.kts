pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "TastyTrade"

include(":app")
include(":core")
include(":core-ui")
include(":feature-instruments-cryptocurrencies")
include(":feature-login")
include(":feature-authentication")
