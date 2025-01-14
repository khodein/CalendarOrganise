plugins {
    id("app-config-plugin")
}

android {
    namespace = "ru.calendar.organise"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(path = ":core:res"))
    implementation(project(path = ":core:tools"))
    implementation(project(path = ":core:navigator"))
    implementation(project(path = ":core:base"))

    implementation(project(path = ":feature:home"))
    implementation(project(path = ":feature:calendar"))
    implementation(project(path = ":feature:clock"))
    implementation(project(path = ":feature:profile"))
}