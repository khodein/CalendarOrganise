package ru.calendar.organise.plugin

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import ru.calendar.organise.model.MethodConfig
import ru.calendar.organise.base.BasePlugin

class KoinPlugin : BasePlugin() {

    override fun apply(target: Project) {
        checkAppOrLib(target)
        val versionCatalogs = target.extensions.getByType<VersionCatalogsExtension>()
        val libs = versionCatalogs.named("libs")
        val koinBomLib = libs.findLibrary("koin-bom").get()
        val koinBoomModule = koinBomLib.get().module
        val koinBoomVersion = koinBomLib.get().version

        val koinCore = libs.findLibrary("koin-core").get()
        val koinCoreModule = koinCore.get().module

        val koinAndroid = libs.findLibrary("koin-android").get()
        val koinAndroidModule = koinAndroid.get().module

        target.dependencies {
            add(
                MethodConfig.IMPL.value,
                target.dependencies.platform("$koinBoomModule:$koinBoomVersion")
            )
            add(MethodConfig.IMPL.value, "$koinCoreModule")
            add(MethodConfig.IMPL.value, "$koinAndroidModule")
        }
    }
}