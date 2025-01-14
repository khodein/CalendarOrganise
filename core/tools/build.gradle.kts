plugins {
    id("lib-config-plugin")
}

android {
    namespace = "ru.calendar.core.tools"
}

dependencies {
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout)

    implementation(project(path = ":core:res"))
}