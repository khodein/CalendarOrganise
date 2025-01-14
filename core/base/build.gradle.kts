plugins {
    id("lib-config-plugin")
}

android {
    namespace = "ru.calendar.core.base"
}

dependencies {
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.viewbinding)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(project(path = ":core:navigator"))
}