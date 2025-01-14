package ru.calendar.organise.lib

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import ru.calendar.organise.base.BaseLib
import ru.calendar.organise.model.DepConfig
import ru.calendar.organise.model.MethodConfig

object AndroidXAppCompat : BaseLib() {

    override fun invoke(libs: VersionCatalog, target: Project): DepConfig {
        val appCompatLib = libs.findLibrary("androidx-appcompat").get()
        val dependencyAppCompat = getDependency(
            module = "${appCompatLib.get().group}",
            name = appCompatLib.get().name,
            version = "${appCompatLib.get().version}"
        )
        return DepConfig(
            method = MethodConfig.IMPL,
            dependency = dependencyAppCompat,
        )
    }
}