plugins {
    id("lib-config-plugin")
}

android {
    namespace = "ru.calendar.core.recycler"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.recyclerview)
}