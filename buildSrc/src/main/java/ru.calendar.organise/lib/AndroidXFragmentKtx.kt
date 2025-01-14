package ru.calendar.organise.lib

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import ru.calendar.organise.base.BaseLib
import ru.calendar.organise.model.DepConfig
import ru.calendar.organise.model.MethodConfig

object AndroidXFragmentKtx : BaseLib() {

    override fun invoke(libs: VersionCatalog, target: Project): DepConfig {
        val fragmentLib = libs.findLibrary("androidx-fragment-ktx").get()
        val dependencyFragment = getDependency(
            module = "${fragmentLib.get().module}",
            version = "${fragmentLib.get().version}"
        )
        return DepConfig(
            method = MethodConfig.IMPL,
            dependency = dependencyFragment
        )
    }
}