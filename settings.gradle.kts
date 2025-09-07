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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ComposeStore"
include(":app")
include(":data")
include(":domain")
include(":feature")
include(":core")
include(":network")
include(":feature:home")
include(":feature:search")
include(":feature:detail")
include(":keystore")
include(":feature:user")
include(":feature:cart")
include(":feature:checkout")
include(":feature:admin")
include(":feature:manage_product")
include(":feature:dashboard")
include(":feature:address")
include(":feature:order")
