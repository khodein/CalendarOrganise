package ru.calendar.organise.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import ru.calendar.organise.app.AppConfig
import ru.calendar.organise.lib.AndroidXCoreKtx
import ru.calendar.organise.base.BasePlugin
import ru.calendar.organise.lib.KotlinXDateTime

class LibConfigPlugin : BasePlugin() {

    override fun apply(target: Project) {
        val versionCatalogs = target.extensions.getByType<VersionCatalogsExtension>()
        val libs = versionCatalogs.named("libs")
        val jetbrainsKotlinAndroidPlugin = libs.findPlugin("jetbrains-kotlin-android").get()
        val jetbrainsKotlinAndroidId = jetbrainsKotlinAndroidPlugin.get().pluginId

        val androidLibraryPlugin = libs.findPlugin("android-library").get()
        val androidLibraryId = androidLibraryPlugin.get().pluginId

        target.plugins.apply(androidLibraryId)
        target.plugins.apply(jetbrainsKotlinAndroidId)

        AppConfig.configureAndroid(target)

        val list = listOf(
            AndroidXCoreKtx.invoke(libs, target),
            KotlinXDateTime.invoke(libs, target),
        )

        setDependency(
            list = list,
            target = target,
        )
    }
}