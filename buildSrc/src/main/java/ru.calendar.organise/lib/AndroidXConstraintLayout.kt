package ru.calendar.organise.lib

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import ru.calendar.organise.base.BaseLib
import ru.calendar.organise.model.DepConfig
import ru.calendar.organise.model.MethodConfig

object AndroidXConstraintLayout: BaseLib() {

    override fun invoke(
        libs: VersionCatalog,
        target: Project
    ): DepConfig {
        val constraintLib = libs.findLibrary("androidx-constraintlayout").get()
        val dependencyConstraint = getDependency(
            module = "${constraintLib.get().group}",
            name = constraintLib.get().name,
            version = "${constraintLib.get().version}"
        )
        return DepConfig(
            method = MethodConfig.IMPL,
            dependency = dependencyConstraint,
        )
    }
}