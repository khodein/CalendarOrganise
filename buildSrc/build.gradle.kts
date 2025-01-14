plugins {
    `kotlin-dsl`
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

repositories {
    mavenCentral()
    google()

    gradlePlugin {
        plugins {
            register("app-config-plugin") {
                description = "AppConfigPlugin"
                id = "app-config-plugin"
                implementationClass = "ru.calendar.organise.plugin.AppConfigPlugin"
            }

            register("lib-config-plugin") {
                description = "LibConfigPlugin"
                id = "lib-config-plugin"
                implementationClass = "ru.calendar.organise.plugin.LibConfigPlugin"
            }

            register("feature-config-plugin") {
                description = "FeatureConfigPlugin"
                id = "feature-config-plugin"
                implementationClass = "ru.calendar.organise.plugin.FeatureConfigPlugin"
            }

            register("koin-plugin") {
                description = "KoinPlugin"
                id = "koin-plugin"
                implementationClass = "ru.calendar.organise.plugin.KoinPlugin"
            }
        }
    }
}

dependencies {
    implementation(libs.gradle)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.gradle.plugin)
    implementation(gradleApi())
}