package ru.calendar.organise.lib

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import ru.calendar.organise.base.BaseLib
import ru.calendar.organise.model.DepConfig
import ru.calendar.organise.model.MethodConfig

object KotlinXDateTime : BaseLib() {

    override fun invoke(
        libs: VersionCatalog,
        target: Project
    ): DepConfig {
        val kotlinXDateTimeLib = libs.findLibrary("kotlinx-datetime").get()
        val dependencyCicerone = getDependency(
            module = "${kotlinXDateTimeLib.get().module}",
            version = "${kotlinXDateTimeLib.get().version}"
        )
        return DepConfig(
            method = MethodConfig.IMPL,
            dependency = dependencyCicerone
        )
    }
}