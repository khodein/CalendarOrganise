package ru.calendar.organise.lib

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import ru.calendar.organise.base.BaseLib
import ru.calendar.organise.model.DepConfig
import ru.calendar.organise.model.MethodConfig

object AndroidXActivityKtx : BaseLib() {

    override fun invoke(libs: VersionCatalog, target: Project): DepConfig {
        val activityLib = libs.findLibrary("androidx-activity-ktx").get()
        val dependency = getDependency(
            module = "${activityLib.get().module}",
            version = "${activityLib.get().version}"
        )
        return DepConfig(
            method = MethodConfig.IMPL,
            dependency = dependency,
        )
    }
}