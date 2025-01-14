package ru.calendar.organise.lib

import org.gradle.api.Project
import ru.calendar.organise.model.DepConfig
import ru.calendar.organise.model.MethodConfig
import org.gradle.api.artifacts.VersionCatalog
import ru.calendar.organise.base.BaseLib

object MaterialLib : BaseLib() {

    override fun invoke(libs: VersionCatalog, target: Project): DepConfig {
        val materialLib = libs.findLibrary("material").get()
        val dependencyMaterial = getDependency(
            module = "${materialLib.get().group}",
            name = materialLib.get().name,
            version = "${materialLib.get().version}"
        )
        return DepConfig(
            method = MethodConfig.IMPL,
            dependency = dependencyMaterial
        )
    }
}