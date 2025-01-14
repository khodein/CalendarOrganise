package ru.calendar.organise.plugin

import ru.calendar.organise.lib.ParcelizeLib
import ru.calendar.organise.app.AppConfig
import ru.calendar.organise.lib.AndroidXConstraintLayout
import ru.calendar.organise.lib.AndroidXFragmentKtx
import ru.calendar.organise.lib.AndroidXCoreKtx
import ru.calendar.organise.lib.AndroidXActivityKtx
import ru.calendar.organise.lib.AndroidXAppCompat
import ru.calendar.organise.lib.MaterialLib
import ru.calendar.organise.lib.CiceroneLib
import ru.calendar.organise.lib.KotlinXDateTime
import ru.calendar.organise.base.BasePlugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

class AppConfigPlugin : BasePlugin() {

    override fun apply(target: Project) {
        val versionCatalogs = target.extensions.getByType<VersionCatalogsExtension>()
        val libs = versionCatalogs.named("libs")
        val androidAppPlugin = libs.findPlugin("android-application").get()
        val androidAppId = androidAppPlugin.get().pluginId
        val jetbrainsKotlinAndroidPlugin = libs.findPlugin("jetbrains-kotlin-android").get()
        val jetbrainsKotlinAndroidId = jetbrainsKotlinAndroidPlugin.get().pluginId
        val koinPluginId = "koin-plugin"

        target.plugins.apply(androidAppId)
        target.plugins.apply(jetbrainsKotlinAndroidId)
        target.plugins.apply(koinPluginId)

        ParcelizeLib.configure(
            libs = libs,
            target = target
        )
        AppConfig.configureAndroid(target)

        val list = listOf(
            AndroidXCoreKtx.invoke(libs, target),
            KotlinXDateTime.invoke(libs, target),
            AndroidXAppCompat.invoke(libs, target),
            AndroidXActivityKtx.invoke(libs, target),
            AndroidXFragmentKtx.invoke(libs, target),
            AndroidXConstraintLayout.invoke(libs, target),
            CiceroneLib.invoke(libs, target),
            MaterialLib.invoke(libs, target),
        )

        setDependency(
            list = list,
            target = target,
        )
    }
}