package ru.calendar.organise.plugin

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import ru.calendar.organise.app.AppConfig
import ru.calendar.organise.lib.AndroidXAppCompat
import ru.calendar.organise.lib.AndroidXConstraintLayout
import ru.calendar.organise.lib.AndroidXCoreKtx
import ru.calendar.organise.lib.AndroidXFragmentKtx
import ru.calendar.organise.lib.MaterialLib
import ru.calendar.organise.base.BasePlugin
import ru.calendar.organise.lib.KotlinXDateTime

class FeatureConfigPlugin : BasePlugin() {

    override fun apply(target: Project) {
        val versionCatalogs = target.extensions.getByType<VersionCatalogsExtension>()
        val libs = versionCatalogs.named("libs")

        val libsPlugin = libs.findPlugin("android-library").get()
        val libsId = libsPlugin.get().pluginId

        val jetbrainsKotlinAndroidPlugin = libs.findPlugin("jetbrains-kotlin-android").get()
        val jetbrainsKotlinAndroidId = jetbrainsKotlinAndroidPlugin.get().pluginId

        target.plugins.apply(libsId)
        target.plugins.apply(jetbrainsKotlinAndroidId)
        target.plugins.apply("koin-plugin")

        checkAppOrLib(target)

        AppConfig.configureAndroid(target)

        val list = listOf(
            AndroidXCoreKtx.invoke(libs, target),
            KotlinXDateTime.invoke(libs, target),
            AndroidXAppCompat.invoke(libs, target),
            AndroidXFragmentKtx.invoke(libs, target),
            AndroidXConstraintLayout.invoke(libs, target),
            MaterialLib.invoke(libs, target)
        )

        setDependency(
            list = list,
            target = target,
        )
    }
}