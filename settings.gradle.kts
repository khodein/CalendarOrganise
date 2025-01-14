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

rootProject.name = "CalendarOrganise"

include(":app")
include(":core:navigator")
include(":core:res")
include(":core:tools")
include(":feature")
include(":feature:home")
include(":feature:calendar")
include(":feature:clock")
include(":feature:profile")
include(":core:recycler")
include(":core:uikit")
include(":core:base")
