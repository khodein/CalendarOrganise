package ru.calendar.organise.lib

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import ru.calendar.organise.base.BaseLib
import ru.calendar.organise.model.DepConfig
import ru.calendar.organise.model.MethodConfig

object CiceroneLib : BaseLib() {

    override fun invoke(libs: VersionCatalog, target: Project): DepConfig {
        val ciceroneLib = libs.findLibrary("cicerone").get()
        val dependencyCicerone = getDependency(
            module = "${ciceroneLib.get().module}",
            version = "${ciceroneLib.get().version}"
        )
        return DepConfig(
            method = MethodConfig.IMPL,
            dependency = dependencyCicerone
        )
    }
}