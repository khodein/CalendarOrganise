package ru.calendar.organise.lib

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import ru.calendar.organise.base.BaseLib
import ru.calendar.organise.model.DepConfig
import ru.calendar.organise.model.MethodConfig

object AndroidXCoreKtx : BaseLib() {

    override fun invoke(libs: VersionCatalog, target: Project): DepConfig {
        val coreLib = libs.findLibrary("androidx-core-ktx").get()
        val dependencyCoreKts = getDependency(
            module = "${coreLib.get().group}",
            name = coreLib.get().name,
            version = "${coreLib.get().version}"
        )
        return DepConfig(
            method = MethodConfig.IMPL,
            dependency = dependencyCoreKts,
        )
    }
}