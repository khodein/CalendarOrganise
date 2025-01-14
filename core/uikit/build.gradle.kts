plugins {
    id("lib-config-plugin")
}

android {
    namespace = "ru.calendar.core.uikit"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(project(path = ":core:res"))
    implementation(project(path = ":core:recycler"))
    implementation(project(path = ":core:tools"))
}