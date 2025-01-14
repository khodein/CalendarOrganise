plugins {
    id("feature-config-plugin")
}

android {
    namespace = "ru.calendar.feature.calendar"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(path = ":core:base"))
    implementation(project(path = ":core:res"))
    implementation(project(path = ":core:tools"))
    implementation(project(path = ":core:navigator"))
    implementation(project(path = ":core:recycler"))
    implementation(project(path = ":core:uikit"))
    implementation(project(path = ":core:res"))
}